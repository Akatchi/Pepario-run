package com.pepario.run.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.pepario.run.Config.Config;
import com.pepario.run.Screens.BaseGameScreen;
import com.pepario.run.Sprites.TileObjects.Coin;
import com.pepario.run.Sprites.TileObjects.Goal;

public class B2WorldCreator
{
    public B2WorldCreator(BaseGameScreen screen)
    {
        World world = screen.getWorld();
        TiledMap map = screen.getTiledMap();

        loadGroundObjects(world, map);
        loadCoinObjects(screen, map);
        loadGoalObjects(screen, map);
    }

    private void loadGroundObjects(World world, TiledMap map)
    {
        loadLayer(world, map, Config.GROUND_BIT, Config.GROUND_LAYER);
    }

    private void loadCoinObjects(BaseGameScreen screen, TiledMap map)
    {
        // Create point objects
        for(MapObject object : map.getLayers().get(Config.COIN_LAYER).getObjects().getByType(RectangleMapObject.class)) {
            new Coin(screen, object);
        }
    }

    private void loadGoalObjects(BaseGameScreen screen, TiledMap map)
    {
        // Create goal objects
        // TODO clean this if and add it for all layers
        if(map.getLayers().get(Config.GOAL_LAYER) != null) {
            for (MapObject object : map.getLayers().get(Config.GOAL_LAYER).getObjects().getByType(RectangleMapObject.class)) {
                new Goal(screen, object);
            }
        }
    }

    private void loadLayer(World world, TiledMap map, short categoryBit, String layerName)
    {
        for(MapObject object : map.getLayers().get(layerName).getObjects()) {
            Shape shape;
            Body body;
            BodyDef bDef = new BodyDef();
            FixtureDef fDef = new FixtureDef();

            bDef.type = BodyDef.BodyType.StaticBody;

            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject)object);
            } else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject)object);
            } else if (object instanceof PolylineMapObject) {
                shape = getPolyline((PolylineMapObject)object);
            } else {
                // Unsupported format so we will skip that one
                continue;
            }

            fDef.shape = shape;
            fDef.filter.categoryBits = categoryBit;

            body = world.createBody(bDef);
            body.createFixture(fDef);
        }
    }

    private Shape getRectangle(RectangleMapObject rectangleObject)
    {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();

        Vector2 size = new Vector2(
                (rectangle.x + rectangle.width * 0.5f) / Config.PPM,
                (rectangle.y + rectangle.height * 0.5f ) / Config.PPM
        );

        polygon.setAsBox(
                rectangle.width * 0.5f / Config.PPM,
                rectangle.height * 0.5f / Config.PPM,
                size,
                0.0f
        );

        return polygon;
    }

    private Shape getPolygon(PolygonMapObject polygonObject)
    {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / Config.PPM;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private Shape getPolyline(PolylineMapObject polylineObject)
    {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / Config.PPM;
            worldVertices[i].y = vertices[i * 2 + 1] / Config.PPM;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }
}
