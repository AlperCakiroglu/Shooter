package com.itujoker.mshooter.sprites.world;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.tools.Main;

public class MyPool extends Sprite {

    private GameScreen screen;
    private Animation poolAnimation, lavaFallAnimation, splashAnimation;
    private float stateTimer = 0;

    private Sprite poolSprite, lavaFallSprite, splashSprite;
    private float poolSize = Bridge.bridgeLength;

    public Body outBody;
    public float splashX;
    public boolean showSplash,splashSound;
    public float splashTimer = 0;

    public MyPool(GameScreen screen, Vector2 pos) {

        this.screen = screen;
        initPoolAnimation();



        setBounds(pos.x,pos.y,poolSize,poolSize/3);
        setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                TextureAtlas.class).findRegion("pool/pool")));

        poolSprite = new Sprite();
        poolSprite.setSize(poolSize*0.95f,poolSize*0.3f);
        poolSprite.setPosition(pos.x+ poolSprite.getWidth()/20,pos.y - poolSprite.getHeight()/30 );

        lavaFallSprite = new Sprite();
        lavaFallSprite.setSize(poolSize * 0.2f,getY());
        lavaFallSprite.setPosition(pos.x + poolSize/2,0);

        splashSprite = new Sprite();
        splashSprite.setSize(poolSize * 0.2f, poolSize * 0.2f);

        createBody(pos);
    }

    private void createBody(Vector2 pos){

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(pos.x + poolSize/2, pos.y );
        outBody = screen.getWorld().createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(poolSize/2,poolSize/60);
        fdef.shape = shape;
        fdef.filter.categoryBits = Main.GROUND_BIT;
        outBody.createFixture(fdef).setUserData(this);

        shape.setAsBox(poolSize/60,poolSize/6,new Vector2(-poolSize/2,poolSize/6),0);
        fdef.shape = shape;
        outBody.createFixture(fdef).setUserData(this);
        shape.setAsBox(poolSize/60,poolSize/6,new Vector2(poolSize/2,poolSize/6),0);
        fdef.shape = shape;
        outBody.createFixture(fdef).setUserData(this);

        shape.setAsBox(poolSize/2,poolSize / 10, new Vector2(0,poolSize/7),0);
        fdef.shape = shape;
        fdef.isSensor = true;
        fdef.filter.categoryBits = Main.WATER_BIT;
        outBody.createFixture(fdef).setUserData(this);


    }

    public void update(float dt){

        stateTimer += dt;
        poolSprite.setRegion((TextureRegion)poolAnimation.getKeyFrame(stateTimer,true));
        lavaFallSprite.setRegion((TextureRegion)lavaFallAnimation.getKeyFrame(stateTimer,true));

        if(showSplash){
            splashTimer+=dt;
            splashSprite.setPosition(splashX,getY()+getHeight()*0.5f);
            splashSprite.setRegion((TextureRegion)splashAnimation.getKeyFrame(splashTimer));
            if(splashAnimation.isAnimationFinished(splashTimer))
                showSplash = false;

        }
        if(splashSound && screen.game.soundOn){
            screen.game.assets.get("music/bubbles.ogg", Sound.class).stop();
            screen.game.assets.get("music/bubbles.ogg", Sound.class).play();
            splashSound = false;
        }
    }
    private void initPoolAnimation(){


        Array<TextureRegion> frames = new Array();
        for (int i = 0; i <= 15; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("pool/lava orange/"+(i+1))));
        poolAnimation = new Animation(0.1f, frames);
        frames.clear();

        for(int i=0;i<=9;i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("pool/lavafall/"+(i+1))));
        lavaFallAnimation = new Animation(0.1f, frames);
        frames.clear();

        for(int i=0;i<=4;i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("pool/splash/"+(i+1))));
        splashAnimation = new Animation(0.1f, frames);
        frames.clear();

    }

    @Override
    public void draw(Batch batch) {
        try{
            poolSprite.draw(batch);
            super.draw(batch);
            lavaFallSprite.draw(batch);
            if(showSplash)
                splashSprite.draw(batch);}
        catch (Exception e){}
    }
}
