package com.itujoker.mshooter.sprites.supplies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.sprites.MyBody;
import com.itujoker.mshooter.tools.Main;

public class Bullet extends MyBody {

    public boolean isContact;
    public int contactedBodyType;
    private boolean isContactSoundRun;

    public boolean isGoingRight;
    public boolean isPlayersBullet;
    public boolean isRocket, rocketExplosion;
    private boolean isHeli;
    private Animation rocketAnimation;

    private boolean rocketSound;

    public Bullet(GameScreen screen, Vector2 pos, boolean isGoingRight, boolean isPlayersBullet, boolean isRocket, boolean isHeli) {
        super(screen);

        this.isHeli = isHeli;
        this.isRocket = isRocket;
        this.isGoingRight = isGoingRight;
        this.isPlayersBullet = isPlayersBullet;

        initAnimation();

        if(isRocket)
            size = Main.tileWidth / 4 / Main.PPM;
        else
            size = Main.tileWidth / 10 / Main.PPM;

        createBody(pos, size);


        if (!isRocket) {
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("bullet")));
            setBounds(b2body.getPosition().x - size, b2body.getPosition().y - size, 2 * size, 2 * size);

        }else{
            setRegion((TextureRegion) rocketAnimation.getKeyFrame(0,true));
            setBounds(b2body.getPosition().x - size, b2body.getPosition().y - size, 2 * size, 2 * size);
        }
    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public void update(float dt) {

        stateTimer+= dt;
        if(!rocketExplosion) {

            if(isRocket)
                setRegion((TextureRegion) rocketAnimation.getKeyFrame(stateTimer,true));

            if (isGoingRight) {
                if(!isHeli)
                    b2body.setLinearVelocity(5, b2body.getLinearVelocity().y);
                else
                    b2body.setLinearVelocity(5, -1);
            }else {
                if(!isHeli)
                    b2body.setLinearVelocity(-5, b2body.getLinearVelocity().y);
                else
                    b2body.setLinearVelocity(-5,-1);

                flip(true,false);
            }

        }else{
            b2body.setLinearVelocity(0,0);
            if(!rocketSound){
                rocketSound = true;
                if(screen.game.soundOn){

                    screen.game.assets.get("music/explosion.ogg", Sound.class).stop();
                    screen.game.assets.get("music/explosion.ogg", Sound.class).play(0.5f);

                }
            }
        }

        setPosition(b2body.getPosition().x-size,b2body.getPosition().y-size);

        if(screen.game.soundOn && !isContactSoundRun && isContact && !isRocket){
            isContactSoundRun = true;
            //bullet contact sesleri
            if((contactedBodyType == 2 || contactedBodyType == 3 || contactedBodyType == 4) && isPlayersBullet){
                //metal sesi
                screen.game.assets.get("music/vehicle_hit.ogg", Sound.class).play(0.5f);
            }else if(contactedBodyType == 5 && isPlayersBullet){
                //enemy1 sesi
                screen.game.assets.get("music/enemy_hit.ogg", Sound.class).play(0.3f);
            }else if(contactedBodyType == 1 && !isPlayersBullet){
                //player sesi
                screen.game.assets.get("music/bullet_hit.ogg", Sound.class).play(0.3f);
            }

        }

        if((isContact || Math.abs(b2body.getPosition().x - screen.gameCam.position.x) >  screen.gamePort.getWorldWidth() / 2)
                && !isRocket)
            canRemove = true;
        else if(isRocket &&  Math.abs(b2body.getPosition().x - screen.gameCam.position.x) >  screen.gamePort.getWorldWidth() / 2)
            canRemove = true;
        else if(isRocket && isContact && !rocketExplosion){
            rocketExplosion = true;
            stateTimer = 0;

            Filter filter = new Filter();
            filter.categoryBits = Main.DESTROYED_BIT;
            filter.maskBits = Main.GROUND_BIT;
            b2body.getFixtureList().first().setFilterData(filter);

            Vector2 currentpos = b2body.getPosition();
            screen.items.removeBodyArray.add(b2body);
            createBody(currentpos,4*size);

        }

        if(rocketExplosion){
            stateTimer += dt;
            setBounds(b2body.getPosition().x - 4*size, b2body.getPosition().y - 4*size,8*size,8*size);
            setRegion((TextureRegion) screen.player.bombAnimation.getKeyFrame(stateTimer));
            if(screen.player.bombAnimation.isAnimationFinished(stateTimer))
                canRemove = true;
        }
    }

    @Override
    public void createBody(Vector2 pos,float bodysize) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setGravityScale(0);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        fdef.density = 1.f;
        fdef.filter.categoryBits = Main.BULLET_BIT;
        if(isRocket) {
            fdef.filter.maskBits = Main.ENEMY_BIT | Main.URANIUM_BIT | Main.SANDBAG_BIT | Main.ROPE_BIT;

        }else if (isPlayersBullet)
            fdef.filter.maskBits = Main.BULLET_BIT | Main.GROUND_BIT | Main.ENEMY_BIT | Main.URANIUM_BIT | Main.SANDBAG_BIT | Main.ROPE_BIT;
        else
            fdef.filter.maskBits = Main.BULLET_BIT | Main.GROUND_BIT | Main.PLAYER_BIT | Main.SANDBAG_BIT;
        CircleShape circle = new CircleShape();
        circle.setRadius(bodysize);
        fdef.shape = circle;

        if(rocketExplosion)
            fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }


    private void initAnimation(){

        Array<TextureRegion> frames = new Array();
        for (int i = 0; i <= 3; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("rocket/"+(i+1))));
        rocketAnimation = new Animation(0.1f, frames);
        frames.clear();
    }
}

