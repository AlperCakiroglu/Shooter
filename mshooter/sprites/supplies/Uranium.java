package com.itujoker.mshooter.sprites.supplies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.tools.Main;

public class Uranium extends Sprite {

    private GameScreen screen;
    private Animation uraniumAnimation;

    public Body b2body;
    private float bodySize;
    private float stateTimer = 0;

    public boolean isExploded;
    private boolean isBodyChanged;
    public boolean canRemove;

    public boolean isBigExplosion;
    //private Sprite[] bigExplosionSprites;
    private Sprite bigExplosionSprite,bigExplosionBucketSprite;
    private Animation bigExplosionAnimation,bigExplosionBucketAnimation;

    public Uranium(GameScreen screen, Vector2 pos, boolean isBigExplosion) {
        this.screen = screen;
        this.isBigExplosion = isBigExplosion;

        initExplosionAnimation();

        if (!isBigExplosion) {
            bodySize = Main.tileWidth / 3 / Main.PPM;
            createBody(pos, bodySize);
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("misc/uranium")));
            setBounds(b2body.getPosition().x - bodySize, b2body.getPosition().y - bodySize, 2 * bodySize, 2 * bodySize);
        }else{

            createBigExplosion(new Vector2(screen.gameCam.position.x - Main.V_WIDTH/ 2.1f / Main.PPM
                    ,screen.gameCam.position.y));

            bigExplosionSprite = new Sprite();
            bigExplosionSprite.setRegion((TextureRegion)bigExplosionAnimation.getKeyFrame(0));
            bigExplosionSprite.setBounds(0
                    ,0,
                    Main.V_HEIGHT/10/Main.PPM, Main.V_HEIGHT*0.95f/Main.PPM);

            bigExplosionBucketSprite = new Sprite();
            bigExplosionBucketSprite.setRegion((TextureRegion)bigExplosionBucketAnimation.getKeyFrame(0));
            bigExplosionBucketSprite.setBounds(bigExplosionSprite.getWidth()/5
                    ,Main.V_HEIGHT*0.9f/Main.PPM,
                    Main.V_HEIGHT/8/Main.PPM, Main.V_HEIGHT/8/Main.PPM);

        }
    }

    public void update(float dt){

        if(!isBigExplosion) {
            setPosition(b2body.getPosition().x - bodySize, b2body.getPosition().y - bodySize);

            if ((isExploded || b2body.getPosition().y < -1) && !isBodyChanged) {

                isBodyChanged = true;

                Filter filter = new Filter();
                filter.categoryBits = Main.DESTROYED_BIT;
                filter.maskBits = Main.GROUND_BIT;
                b2body.getFixtureList().first().setFilterData(filter);

                Vector2 currentpos = b2body.getPosition();
                screen.items.removeBodyArray.add(b2body);
                createBody(currentpos, 4 * bodySize);

                if(screen.game.soundOn){

                    screen.game.assets.get("music/explosion.ogg", Sound.class).stop();
                    screen.game.assets.get("music/explosion.ogg", Sound.class).play(0.5f);

                }


            } else if (isExploded && isBodyChanged) {

                b2body.setGravityScale(0);
                b2body.setLinearVelocity(0, 0);

                stateTimer += dt;
                setBounds(b2body.getPosition().x - 4 * bodySize, b2body.getPosition().y - 4 * bodySize, 8 * bodySize, 8 * bodySize);
                setRegion((TextureRegion) uraniumAnimation.getKeyFrame(stateTimer));
                if (uraniumAnimation.isAnimationFinished(stateTimer))
                    canRemove = true;

            }

        }else{
            b2body.setTransform(new Vector2(screen.gameCam.position.x - Main.V_WIDTH/ 2.1f / Main.PPM
                    ,screen.gameCam.position.y),0);

            stateTimer+=dt;
            bigExplosionSprite.setRegion((TextureRegion)bigExplosionAnimation.getKeyFrame(stateTimer,true));
            bigExplosionSprite.setPosition(screen.gameCam.position.x - Main.V_WIDTH/2/Main.PPM  ,0);

            bigExplosionBucketSprite.setRegion((TextureRegion)bigExplosionBucketAnimation.getKeyFrame(stateTimer,true));
            bigExplosionBucketSprite.setPosition(screen.gameCam.position.x - Main.V_WIDTH/2/Main.PPM+ bigExplosionSprite.getWidth()/6 ,Main.V_HEIGHT*0.9f/Main.PPM);
        }
    }
    private void createBody(Vector2 pos,float size){

        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = screen.getWorld().createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        fdef.density = 1.f;
        fdef.filter.categoryBits = Main.URANIUM_BIT;
        if(!isExploded)
            fdef.filter.maskBits = Main.GROUND_BIT | Main.BULLET_BIT | Main.PLAYER_BIT | Main.BOMB_BIT | Main.SANDBAG_BIT | Main.SPIKE_BIT | Main.LEG_BIT;
        else
            fdef.filter.maskBits = Main.GROUND_BIT | Main.ENEMY_BIT  | Main.PLAYER_BIT  | Main.SANDBAG_BIT | Main.SPIKE_BIT;
        CircleShape circle = new CircleShape();
        circle.setRadius(size);
        fdef.shape = circle;
        if(isExploded)
            fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
    }
    private void initExplosionAnimation(){

        Array<TextureRegion> frames = new Array();
        for (int i = 0; i <= 11; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("green_bomb/"+(i+1))));
        uraniumAnimation = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 0; i <= 3; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("bigexplosion/"+(i+1))));
        bigExplosionAnimation = new Animation(0.2f, frames);
        frames.clear();

        for(int i=5; i<=8;i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("bigexplosion/"+(i+1))));
        bigExplosionBucketAnimation = new Animation(0.2f, frames);
        frames.clear();
    }

    private void createBigExplosion(Vector2 pos){

        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = screen.getWorld().createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        fdef.density = 100;
        fdef.filter.categoryBits = Main.URANIUM_BIT;
        fdef.filter.maskBits = Main.PLAYER_BIT | Main.ENEMY_BIT;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10/Main.PPM,Main.V_HEIGHT/ 2.2f / Main.PPM);
        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void draw(Batch batch) {
        if(!isBigExplosion)
            super.draw(batch);
        else{

            bigExplosionSprite.draw(batch);
            bigExplosionBucketSprite.draw(batch);
        }
    }
}