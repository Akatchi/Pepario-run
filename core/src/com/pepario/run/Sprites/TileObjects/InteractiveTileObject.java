package com.pepario.run.Sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.pepario.run.Config.Config;
import com.pepario.run.Screens.BaseGameScreen;
import com.pepario.run.Sprites.Player;

public abstract class InteractiveTileObject
{
    private Fixture fixture;
    private TiledMap map;
    private Body body;

    public InteractiveTileObject(BaseGameScreen screen, MapObject mapObject)
    {
        map = screen.getTiledMap();
        fixture = defineInteractiveTileObject(screen.getWorld(), ((RectangleMapObject) mapObject).getRectangle());
        body = fixture.getBody();
    }

    protected Fixture defineInteractiveTileObject(World world, Rectangle bounds)
    {
        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set(
                (bounds.getX() + (bounds.getWidth() / 2)) / Config.PPM,
                (bounds.getY() + (bounds.getHeight() / 2)) / Config.PPM
        );

        Body body = world.createBody(bDef);

        shape.setAsBox(
                (bounds.getWidth() / 2) / Config.PPM,
                (bounds.getHeight() / 2) / Config.PPM
        );
        fDef.shape = shape;

        return body.createFixture(fDef);
    }

    protected Fixture getFixture()
    {
        return fixture;
    }

    protected void setCategoryFilter(short filterBit)
    {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;

        getFixture().setFilterData(filter);
    }

    protected short getCategoryFilter()
    {
        return getFixture().getFilterData().categoryBits;
    }

    protected TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(Config.INTERACTIVE_GRAPHICS_LAYER);

        return layer.getCell(
                (int) (body.getPosition().x * Config.PPM / 16),
                (int) (body.getPosition().y * Config.PPM / 16)
        );
    }

    public abstract void onHit(Player player);
}
