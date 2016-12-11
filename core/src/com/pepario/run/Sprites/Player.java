package com.pepario.run.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.pepario.run.Config.Config;
import com.pepario.run.Screens.BaseGameScreen;

public class Player extends PeparioSprite
{
    private enum State { FALLING, JUMPING, RUNNING, STANDING, DEAD }
    private State currentState;
    private State previousState;

    private BaseGameScreen screen;
    private World world;
    private Body body;

    private TextureRegion playerStand;
    private Animation playerRun;
    private TextureRegion playerJump;
    private TextureRegion playerDead;

    private float stateTimer;
    private float maxRunningSpeed;
    private float deltaTimeRunningSpeedUpdateWeight;
    private float movementSpeed;

    private boolean isDead = false;
    private boolean completedLevel = false;

    public Player(BaseGameScreen screen)
    {
        this.screen = screen;
        this.world = screen.getWorld();

        // TODO add explanation to variables below
        stateTimer = 0;
        maxRunningSpeed = 0.7f;
        deltaTimeRunningSpeedUpdateWeight = 150;
        movementSpeed = 0.1f;

        currentState = State.STANDING;
        previousState = currentState;

        loadTextures();
        definePlayer();

        setBounds(0, 0, 16 / Config.PPM, 16 / Config.PPM);
        setRegion(playerStand);
    }

    private void loadTextures()
    {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 2; i < 5; i++) {
            frames.add(new TextureRegion(screen.getTextureAtlas().findRegion("char_1_east"), i * 16, 0, 16, 16));
        }

        playerRun = new Animation(0.2f, frames);
        playerJump = new TextureRegion(screen.getTextureAtlas().findRegion("char_1_east"), 16, 0, 16, 16);
        playerStand = new TextureRegion(screen.getTextureAtlas().findRegion("char_1_east"), 32, 0, 16, 16);
        playerDead = new TextureRegion(screen.getTextureAtlas().findRegion("char_1_east"), 0, 0, 16, 16);
    }

    private void definePlayer()
    {
        BodyDef bDef = new BodyDef();
        bDef.position.set(32 / Config.PPM, 32 / Config.PPM);
        bDef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Config.PPM);

        fDef.shape = shape;
        fDef.filter.categoryBits = Config.PLAYER_BIT;
        fDef.filter.maskBits = Config.COIN_BIT |
                Config.GROUND_BIT |
                Config.GOAL_BIT;

        body.createFixture(fDef).setUserData(this);
    }

    public void jump()
    {
        // No input when dead or when the level is completed
        if(!canProcessInput()) { return; }

        if(body.getLinearVelocity().y == 0) {
            body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
        }
    }

    public void moveRight()
    {
        // No input when dead or when the level is completed
        if(!canProcessInput()) { return; }

        // Limit the max speed to a certain value
        if(body.getLinearVelocity().x <= maxRunningSpeed)
        {
            body.applyLinearImpulse(new Vector2(movementSpeed, 0), body.getLinearVelocity(), true);
        }
    }

    public void update(float deltaTime)
    {
        stateTimer += deltaTime;

        // Slowly increment the max running speed to make mario keep running faster
        // The higher the deltaTimeRunningSpeedUpdateWeight, the slower the maxRunningSpeed increases
        maxRunningSpeed += deltaTime / deltaTimeRunningSpeedUpdateWeight;

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(deltaTime));

        if(isGameOver()) {
            gameOver();
        }
    }

    private TextureRegion getFrame(float deltaTime)
    {
        currentState = getState();

        TextureRegion region;

        switch(currentState) {
            case DEAD:
                region = playerDead;
                break;
            case JUMPING:
                region = playerJump;
                break;
            case RUNNING:
                region = playerRun.getKeyFrame(stateTimer, true);
                break;
            case STANDING:
            case FALLING:
            default:
                region = playerStand;
                break;
        }

        // Reset the timer if we have transitioned to a new state
        stateTimer = currentState == previousState ? stateTimer + deltaTime : 0;

        previousState = currentState;

        return region;
    }

    private State getState()
    {
        if(isDead) {
            return State.DEAD;
        } else if(body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if(body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if(body.getLinearVelocity().x > movementSpeed) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public boolean canProcessInput()
    {
        return !isDead() && !completedLevel();
    }

    public Body getBody()
    {
        return body;
    }

    public boolean isDead()
    {
        return isDead;
    }

    public boolean completedLevel()
    {
        return completedLevel;
    }

    public void setCompletedLevel()
    {
        this.completedLevel = true;
        stateTimer = 0;
        screen.getAssetManager().get("audio/sounds/levelup.ogg", Sound.class).play();
    }

    private boolean isGameOver()
    {
        // It is game over either when you are falling outside the screen or when you are standing still
        // for to long. (2 seconds currently)
        return(body.getPosition().y < 0 || currentState == State.STANDING && stateTimer > 2 && !completedLevel()) && !isDead();
    }

    /**
     * This method sets the isDead flag to true and plays the gameover sound
     * to indicate that the game is over for this player
     */
    private void gameOver()
    {
        // Make a funny jump when we die
        body.applyLinearImpulse(new Vector2(0, 8f), body.getWorldCenter(), true);

        // Make sure that we can't have any collisions with other objects
        Filter filter = new Filter();
        filter.maskBits = Config.NOTHING_BIT;

        for(Fixture fixture : body.getFixtureList()) {
            fixture.setFilterData(filter);
        }

        stateTimer = 0;
        isDead = true;

        screen.getAssetManager().get("audio/sounds/gameover.ogg", Sound.class).play();
    }

    public float getStateTimer()
    {
        return stateTimer;
    }

    public Player setMaxRunningSpeed(float maxRunningSpeed)
    {
        this.maxRunningSpeed = maxRunningSpeed;
        return this;
    }

    public Player setDeltaTimeRunningSpeedUpdateWeight(float deltaTimeRunningSpeedUpdateWeight)
    {
        this.deltaTimeRunningSpeedUpdateWeight = deltaTimeRunningSpeedUpdateWeight;
        return this;
    }

    public Player setMovementSpeed(float movementSpeed)
    {
        this.movementSpeed = movementSpeed;
        return this;
    }
}
