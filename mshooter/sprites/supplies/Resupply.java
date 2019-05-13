package com.itujoker.mshooter.sprites.supplies;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.sprites.world.Ground;
import com.itujoker.mshooter.tools.Main;

public class Resupply extends Sprite {


    private GameScreen screen;
    private int type;
    public Body b2body;
    private float bodysize = Main.tileWidth / 4 / Main.PPM;
    public boolean canRemove;
    public boolean playerContact;

    public Resupply(GameScreen screen, Vector2 pos, int type) { // health , bomb
        this.screen = screen;
        this.type = type;
        generateBody(pos);

        if(type == 1)////health
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("resupply1")));
        else if(type == 2)///bomb
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("resupply2")));

        setBounds(b2body.getPosition().x - bodysize, b2body.getPosition().y - bodysize,2*bodysize,2*bodysize);
    }

    public void update(){

        if(playerContact ){
            if(type == 2) {
                screen.player.bombnumber+=5;
                screen.player.rocketNumber+=5;
                if(screen.player.bombnumber>9)
                    screen.player.bombnumber = 9;
                if(screen.player.rocketNumber > 9)
                    screen.player.rocketNumber = 9;
            }else if(type == 1)
                screen.player.takenBullet = 0;

            canRemove = true;
        }

        if(screen.gameCam.position.x > b2body.getPosition().x + Ground.groundSize)
            canRemove = true;

    }

    private void generateBody(Vector2 pos){

        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = screen.getWorld().createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = Main.SUPPLY_BIT;
        fdef.filter.maskBits =  Main.PLAYER_BIT ;
        CircleShape circle = new CircleShape();
        circle.setRadius(bodysize);
        fdef.shape = circle;
        b2body.createFixture(fdef).setUserData(this);


    }

}
