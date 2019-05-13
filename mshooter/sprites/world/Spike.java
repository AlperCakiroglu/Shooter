package com.itujoker.mshooter.sprites.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.tools.Main;

public class Spike extends Sprite {

    private GameScreen screen;
    public Body b2body;
    private Vector2 bodySize;
    public boolean canRemove;
    public boolean isKaput,isDestroyed;

    public Spike (GameScreen screen, Vector2 pos) {
        this.screen = screen;
        bodySize = new Vector2(Main.tileWidth / 2 / Main.PPM, Main.tileWidth / 4 / Main.PPM);

        createBody(pos,bodySize);

        setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                TextureAtlas.class).findRegion("misc/spike")));
        setBounds(b2body.getPosition().x - bodySize.x, b2body.getPosition().y - bodySize.y/1.5f,2*bodySize.x,bodySize.y*1.2f);
    }

    public void update() {
        if (screen.gameCam.position.x > b2body.getPosition().x + Ground.groundSize)
            canRemove = true;

        if(isKaput && !isDestroyed){
            isDestroyed = true;
            Filter filter = new Filter();
            filter.categoryBits = Main.DESTROYED_BIT;
            filter.maskBits = Main.GROUND_BIT;
            for (Fixture fixture: b2body.getFixtureList())
                fixture.setFilterData(filter);

            screen.score +=50;
        }
        else if (isKaput && isDestroyed) {
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("misc/kaputspike")));
        }
    }

    private void createBody(Vector2 pos,Vector2 size){

        BodyDef bdef = new BodyDef();
        bdef.position.set(new Vector2(pos.x, pos.y - Main.tileWidth / 8 / Main.PPM));
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = screen.getWorld().createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = Main.SPIKE_BIT;
        fdef.filter.maskBits =  Main.PLAYER_BIT | Main.ENEMY_BIT | Main.BOMB_BIT | Main.URANIUM_BIT | Main.DESTROYED_BIT | Main.LEG_BIT;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x*3/5,size.y/4);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        shape.setAsBox(size.x/6,size.y/4,new Vector2(size.x*4/5,0),0);
        fdef.filter.categoryBits = Main.GROUND_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        shape.setAsBox(size.x/6,size.y/4,new Vector2(-size.x*4/5,0),0);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }
}
