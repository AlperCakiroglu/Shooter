package com.itujoker.mshooter.sprites.supplies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.tools.Main;

public class Bomb extends Sprite {

    private GameScreen screen;
    public Body b2body;
    private float bodysize = Main.tileWidth / 4 / Main.PPM;
    private float stateTimer = 0;

    public boolean isExploded;
    public boolean canRemove;

    public Bomb(GameScreen screen, Vector2 pos) {
        this.screen = screen;
        if(screen.player.runningRight)
            pos.x += 2*(Main.tileWidth / 2 / Main.PPM - Main.tileWidth / 30 / Main.PPM);
        generateBody(pos,bodysize);

        setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                TextureAtlas.class).findRegion("bomba")));

        setBounds(b2body.getPosition().x - bodysize, b2body.getPosition().y - bodysize,2*bodysize,2*bodysize);
        if(screen.player.runningRight)
            b2body.applyLinearImpulse(new Vector2(1.f, 1.f), b2body.getWorldCenter(), true);
        else
            b2body.applyLinearImpulse(new Vector2(-1.f, 1.f), b2body.getWorldCenter(), true);
    }

    public void update(float dt){

        stateTimer += dt;
        setPosition(b2body.getPosition().x - bodysize, b2body.getPosition().y - bodysize);
        if(stateTimer> 2 && !isExploded){
            stateTimer = 0;
            isExploded = true;

            Filter filter = new Filter();
            filter.categoryBits = Main.DESTROYED_BIT;
            filter.maskBits = Main.GROUND_BIT;
            b2body.getFixtureList().first().setFilterData(filter);

            Vector2 currentpos = b2body.getPosition();
            screen.items.removeBodyArray.add(b2body);
            generateBody(currentpos,4*bodysize);

            if(screen.game.soundOn){

                screen.game.assets.get("music/explosion.ogg", Sound.class).stop();
                screen.game.assets.get("music/explosion.ogg", Sound.class).play(0.5f);

            }

        }

        if(isExploded){
            b2body.setGravityScale(0);
            b2body.setLinearVelocity(0,0);

            setBounds(b2body.getPosition().x - 4*bodysize, b2body.getPosition().y - 4*bodysize,8*bodysize,8*bodysize);
            setRegion((TextureRegion) screen.player.bombAnimation.getKeyFrame(stateTimer));
            if(screen.player.bombAnimation.isAnimationFinished(stateTimer))
                canRemove = true;
        }
    }
    private void generateBody(Vector2 pos,float size){

        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = screen.getWorld().createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = Main.BOMB_BIT;
        fdef.filter.maskBits =  Main.ENEMY_BIT | Main.GROUND_BIT | Main.URANIUM_BIT | Main.SANDBAG_BIT | Main.SPIKE_BIT| Main.ROPE_BIT;
        fdef.density = 1f;
        CircleShape circle = new CircleShape();
        circle.setRadius(size);
        fdef.shape = circle;
        if(isExploded)
            fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);


    }
}