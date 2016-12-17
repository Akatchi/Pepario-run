package com.pepario.run.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.pepario.run.Config.Config;
import com.pepario.run.Sprites.Player;
import com.pepario.run.Sprites.TileObjects.Coin;
import com.pepario.run.Sprites.TileObjects.Goal;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // Get the collision definition
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case Config.PLAYER_BIT | Config.GOAL_BIT:
                if (fixA.getFilterData().categoryBits == Config.PLAYER_BIT) {
                    ((Goal) fixB.getUserData()).onHit((Player) fixA.getUserData());
                } else {
                    ((Goal) fixA.getUserData()).onHit((Player) fixB.getUserData());
                }
                break;

            case Config.PLAYER_BIT | Config.GROUND_BIT:
                contact.setFriction(0);

        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // Get the collision definition
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case Config.PLAYER_BIT | Config.COIN_BIT:
                // Disable the contact so the the player and the coin have no 'bounce' feeling
                // when they collide
                contact.setEnabled(false);

                if (fixA.getFilterData().categoryBits == Config.PLAYER_BIT) {
                    ((Coin) fixB.getUserData()).onHit((Player) fixA.getUserData());
                } else {
                    ((Coin) fixA.getUserData()).onHit((Player) fixB.getUserData());
                }

                break;
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
