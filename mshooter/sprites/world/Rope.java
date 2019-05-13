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

public class Rope extends Sprite {

    private GameScreen screen;

    public Body[] segments;
    private RevoluteJoint[] revoluteJoints;
    private Sprite[] sprites;
    private float width = Main.tileWidth / 6 / Main.PPM;
    private static float height = Main.tileWidth / 3 / Main.PPM;

    public static final float ropeLength = 5 * height;

    public Rope(GameScreen screen, Vector2 pos) {
        this.screen = screen;

        createRope(5,pos);
    }

    private void createRope(int length,Vector2 pos){

        segments = new Body[length];
        revoluteJoints = new RevoluteJoint[length-1];
        sprites = new Sprite[length];
        for(int i=0;i<sprites.length;i++){
            sprites[i] = new Sprite();
            sprites[i].setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("rope")));
        }

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(pos);


        FixtureDef fdef = new FixtureDef();
        fdef.density = 1.f;
        fdef.filter.categoryBits = Main.ROPE_BIT;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, height/2);
        fdef.shape = shape;

        for(int i= 0 ;i< segments.length;  i++){
            if(i!= 0)
                bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.position.set(pos.x,pos.y - i*height);
            segments[i] = screen.getWorld().createBody(bodyDef);
            segments[i].createFixture(fdef).setUserData(this);
        }

        RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
        revoluteJointDef.localAnchorA.set(0, -height/2);
        revoluteJointDef.localAnchorB.set(0, height/2);
        //ropeJointDef.maxLength = height/10;
        for(int i= 0;i<revoluteJoints.length;i++){
            revoluteJointDef.bodyA = segments[i];
            revoluteJointDef.bodyB = segments[i+1];
            revoluteJoints[i] = (RevoluteJoint)screen.getWorld().createJoint(revoluteJointDef);

        }

    }


    public void update(){

        for(int i= 0 ;i< segments.length;  i++){

            sprites[i].setBounds(segments[i].getPosition().x - width/2, segments[i].getPosition().y - height/2 ,width,1.2f*height);

            sprites[i].setRotation((float)(segments[i].getAngle() * 180/Math.PI));



        }

    }

    @Override
    public void draw(Batch batch) {

        for(int i=0;i<sprites.length;i++)
            sprites[i].draw(batch);
    }
}
