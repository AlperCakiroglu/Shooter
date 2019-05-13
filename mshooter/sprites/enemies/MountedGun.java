package com.itujoker.mshooter.sprites.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.sprites.MyBody;
import com.itujoker.mshooter.sprites.supplies.Bullet;
import com.itujoker.mshooter.sprites.world.Ground;
import com.itujoker.mshooter.tools.Main;

public class MountedGun extends MyBody {


    public MountedGun(GameScreen screen, Vector2 pos) {
        super(screen);

        type = 4;
        size = Main.tileWidth / 3 / Main.PPM;
        bodyhealth = 3;
        createBody(pos,size);

        setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                TextureAtlas.class).findRegion("mounted_gun")));
        setBounds(b2body.getPosition().x - size
                ,b2body.getPosition().y - size
                , 2*size, 2*size);

    }

    @Override
    public State getState() {
        return null;
    }

    @Override
    public void update(float dt) {

        stateTimer += dt;
        if(!isDead) {
            if (bulletContact) {
                takenBullet++;
                bulletContact = false;
            }
            if (takenBullet >= bodyhealth || screen.gameCam.position.x > b2body.getPosition().x + Ground.groundSize) {
                if(screen.game.soundOn){

                    screen.game.assets.get("music/explosion.ogg", Sound.class).stop();
                    screen.game.assets.get("music/explosion.ogg", Sound.class).play(0.5f);

                }
                readytoDead();
                screen.score+=100;
            }

            if(takenBullet > bodyhealth)
                takenBullet = bodyhealth;
            ////////////////////////////attacking

            if(stateTimer >1)
                isAttacking = false;
            if (!isAttacking && Math.abs(screen.player.b2body.getPosition().x - b2body.getPosition().x) < screen.gamePort.getWorldWidth()/2
                    && Math.abs(screen.player.b2body.getPosition().y - b2body.getPosition().y) < size
                    && stateTimer > 1 && !screen.player.isDead) {
                isAttacking = true;
                screen.items.bullets.add(new Bullet(screen, new Vector2(b2body.getPosition().x - size,
                        b2body.getPosition().y + size / 2.5f), false, false,false,false));
                setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                        TextureAtlas.class).findRegion("mounted_gunattack")));
                stateTimer = 0;

            }
            if(stateTimer > 0.3f)
                setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                        TextureAtlas.class).findRegion("mounted_gun")));

        }
        else{
            setBounds(b2body.getPosition().x - 2*size
                    ,b2body.getPosition().y - 2*size,4*size,4*size);
            setRegion((TextureRegion) bombAnimation.getKeyFrame(stateTimer));
            if(bombAnimation.isAnimationFinished(stateTimer))
                canRemove = true;
        }

        healthSprite.setBounds(b2body.getPosition().x - size,b2body.getPosition().y + 1.5f * size
                , 2*size*((float)(bodyhealth-takenBullet)/bodyhealth),size/4);
    }

    @Override
    public void createBody(Vector2 pos,float bodysize) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        fdef.density = 10.f;
        fdef.filter.categoryBits = Main.ENEMY_BIT;
        fdef.filter.maskBits = Main.GROUND_BIT | Main.BULLET_BIT | Main.PLAYER_BIT
                | Main.BOMB_BIT | Main.URANIUM_BIT | Main.SANDBAG_BIT | Main.LEG_BIT;
        CircleShape circle = new CircleShape();
        circle.setRadius(bodysize);
        fdef.shape = circle;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void draw(Batch batch) {
        if(screen.gameCam.position.x + Ground.groundSize > b2body.getPosition().x && (!canRemove)){
            super.draw(batch);
            healthSprite.draw(batch);
        }

    }
}
