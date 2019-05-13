package com.itujoker.mshooter.sprites.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.tools.Main;

public class Ladder extends Sprite {

    private GameScreen screen;
    public Body b2body;
    public Ground start,end;
    private float ladderwidth =  5  / Main.PPM;

    public Ladder(GameScreen screen, Ground start, Ground end) {
        this.screen = screen;
        this.start = start;
        this.end = end;
        generateLadder();

        setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                TextureAtlas.class).findRegion("ladder")));
        setBounds(b2body.getPosition().x - screen.player.size
                ,b2body.getPosition().y - (end.b2body.getPosition().y - start.b2body.getPosition().y)/2
                ,screen.player.size*2,
                end.b2body.getPosition().y - start.b2body.getPosition().y);
    }

    private void generateLadder(){

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;

        float posy = start.b2body.getPosition().y + (end.b2body.getPosition().y - start.b2body.getPosition().y) / 2;

        bdef.position.set(end.b2body.getPosition().x - screen.player.size - Ground.groundSize/2, posy);
        b2body = screen.getWorld().createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(ladderwidth, posy - start.b2body.getPosition().y);
        fdef.shape = shape;
        fdef.filter.categoryBits = Main.LADDER_BIT;
        fdef.filter.maskBits = Main.PLAYER_LADDER_BIT;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

    }


}
