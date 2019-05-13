package com.itujoker.mshooter.sprites.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.tools.Main;

public class Bridge extends Sprite {

    private GameScreen screen;
    public Body[] segments;
    private RevoluteJoint[] joints;
    private Sprite[] sprites;

    public static final float width = Main.tileWidth / 3 / Main.PPM;
    public static final float height =  Main.tileWidth / 6 / Main.PPM;

    public static final float bridgeLength = 10 * width;

    public Bridge(GameScreen screen, Vector2 pos) {
        this.screen = screen;

        createBridge(10,pos);
    }

    private void createBridge(int length,Vector2 pos){

        segments = new Body[length];
        joints = new RevoluteJoint[length-1];
        sprites = new Sprite[length];

        for(int i=0;i<sprites.length;i++){
            sprites[i] = new Sprite();
            sprites[i].setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("bridge")));
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(pos);


        FixtureDef fdef = new FixtureDef();
        fdef.density = 5.f;
        fdef.filter.categoryBits = Main.GROUND_BIT;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);
        fdef.shape = shape;

        for(int i= 0 ;i< segments.length;  i++){

            if(i!= 0 && i!= segments.length-1)
                bodyDef.type = BodyDef.BodyType.DynamicBody;
            else if(i== segments.length-1)
                bodyDef.type = BodyDef.BodyType.StaticBody;

            bodyDef.position.set(new Vector2(pos.x+i*width,pos.y));

            segments[i] = screen.getWorld().createBody(bodyDef);
            segments[i].createFixture(fdef).setUserData(this);

        }

        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.localAnchorA.set(width/2, 0);
        revoluteJointDef.localAnchorB.set(-width/2, 0);

        for(int i= 0;i<joints.length;i++){
            revoluteJointDef.bodyA = segments[i];
            revoluteJointDef.bodyB = segments[i+1];
            joints[i] = (RevoluteJoint) screen.getWorld().createJoint(revoluteJointDef);

        }
    }


    public void update(){

        for(int i= 0 ;i< segments.length;  i++){

            sprites[i].setBounds(segments[i].getPosition().x - width/2, segments[i].getPosition().y - height/2 ,1.2f*width,1.2f*height);

            sprites[i].setRotation((float)(segments[i].getAngle() * 180/Math.PI));
        }
    }

    @Override
    public void draw(Batch batch) {

        for(int i=0;i<sprites.length;i++)
            sprites[i].draw(batch);
    }
}

