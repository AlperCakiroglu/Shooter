package com.itujoker.mshooter.sprites.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.tools.Main;

public class SandBag extends Sprite {

    private GameScreen screen;
    public Body b2body;
    private float bodySize;
    public boolean canRemove;

    public boolean isKaput,isDestroyed;
    private float stateTimer = 0;
    private Animation kaputAnimation;

    public SandBag(GameScreen screen, Vector2 pos) {
        this.screen = screen;
        bodySize = Main.tileWidth / 3 / Main.PPM;

        initAnimaition();

        createBody(pos, bodySize);

        setRegion((TextureRegion) kaputAnimation.getKeyFrame(0));
        setBounds(b2body.getPosition().x - bodySize, b2body.getPosition().y - 1.1f*bodySize,2*bodySize,2*bodySize);
    }

    public void update(float dt){

        if(isKaput && !isDestroyed){

            isDestroyed = true;

            Filter filter = new Filter();
            filter.categoryBits = Main.DESTROYED_BIT;
            filter.maskBits = Main.GROUND_BIT;
            b2body.getFixtureList().first().setFilterData(filter);

            screen.score += 50;

        }
        else if(isDestroyed){
            stateTimer += dt;
            setRegion((TextureRegion) kaputAnimation.getKeyFrame(stateTimer));
            if(kaputAnimation.isAnimationFinished(stateTimer)){
                setSize(getWidth(),2*bodySize/5);
                setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                        TextureAtlas.class).findRegion("misc/sandbag/"+5)));
            }
        }

        if(screen.gameCam.position.x > b2body.getPosition().x + Ground.groundSize)
            canRemove = true;

    }

    private void createBody(Vector2 pos,float size){

        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = screen.getWorld().createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = Main.SANDBAG_BIT;
        CircleShape circle = new CircleShape();
        circle.setRadius(size);
        fdef.shape = circle;
        b2body.createFixture(fdef).setUserData(this);
    }

    private void initAnimaition(){

        Array<TextureRegion> frames = new Array();
        for (int i = 0; i <= 3; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("misc/sandbag/"+(i+1))));
        kaputAnimation = new Animation(0.1f, frames);
        frames.clear();
    }
}
