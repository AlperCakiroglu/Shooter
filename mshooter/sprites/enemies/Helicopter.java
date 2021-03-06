package com.itujoker.mshooter.sprites.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.sprites.MyBody;
import com.itujoker.mshooter.sprites.supplies.Bullet;
import com.itujoker.mshooter.sprites.world.Ground;
import com.itujoker.mshooter.tools.Main;

public class Helicopter extends MyBody {

    private Animation idleAnimation, attackAnimation, deadAnimation;

    private float attackTimer = 0.6f;

    public boolean isOnScreen;

    public Helicopter(GameScreen screen, Vector2 pos) {
        super(screen);

        type = 3;
        size = Main.tileWidth / 2 / Main.PPM;
        bodyhealth = 4;
        createBody(pos,size);
        initAnimations();

        setRegion((TextureRegion)idleAnimation.getKeyFrame(0));
        setBounds(b2body.getPosition().x - 2*size
                ,b2body.getPosition().y - 1.5f *size
                , 4*size, 3*size);
    }

    @Override
    public State getState() {
        if(isDead)
            return State.DEAD;
        else if(Math.abs(screen.player.b2body.getPosition().x - b2body.getPosition().x) < screen.gamePort.getWorldWidth()/2
                && screen.player.b2body.getPosition().y - b2body.getPosition().y < size && !screen.player.isDead)
            return State.ATTACKING;
        else
            return State.IDLE;
    }

    public TextureRegion getFrame(State state, float dt){

        TextureRegion region;
        switch (state){
            case DEAD:
                region = (TextureRegion) deadAnimation.getKeyFrame(dt);
                if(deadAnimation.isAnimationFinished(dt))
                    canRemove = true;
                break;
            case ATTACKING:
                region = (TextureRegion) attackAnimation.getKeyFrame(dt);
                if(attackAnimation.isAnimationFinished(dt)){
                    stateTimer = 0;
                }
                break;
            case IDLE:
            default:
                region = (TextureRegion) idleAnimation.getKeyFrame(dt, true);
                break;
        }

        if (runningRight && !region.isFlipX()) {
            region.flip(true, false);
        } else if (!runningRight && region.isFlipX()) {
            region.flip(true, false);
        }


        setBounds(b2body.getPosition().x - 2*size
                ,b2body.getPosition().y - 1.5f *size
                , 4*size, 3*size);


        return region;

    }



    @Override
    public void update(float dt) {

        currentState = getState();
        stateTimer = currentState == previousState ? stateTimer + dt : 0;

        if(!isDead) {
            if (screen.player.b2body.getPosition().x > b2body.getPosition().x)
                runningRight = true;
            else
                runningRight = false;

            if (bulletContact) {
                takenBullet++;
                bulletContact = false;
            }
            if (takenBullet >= bodyhealth || screen.gameCam.position.x > b2body.getPosition().x + Ground.groundSize){
                if(screen.game.soundOn){

                    screen.game.assets.get("music/explosion.ogg", Sound.class).stop();
                    screen.game.assets.get("music/explosion.ogg", Sound.class).play(0.3f);

                }
                readytoDead();
                screen.score +=200;

                ///achievements
                if(!screen.unlockOneHeliAc)
                    screen.unlockOneHeliAc = true;
                if(screen.destroyedHelis < 5)
                    screen.destroyedHelis++;
                if(!screen.unlockFiveHeliAc && screen.destroyedHelis == 5)
                    screen.unlockFiveHeliAc = true;
            }

            if(takenBullet > bodyhealth)
                takenBullet = bodyhealth;
            //////////////////////////////////attacking

            if(currentState == State.ATTACKING){
                attackTimer += dt;
                if(attackTimer>0.5f){
                    if (runningRight) {
                        screen.items.bullets.add(new Bullet(screen, new Vector2(b2body.getPosition().x + 2*size,
                                b2body.getPosition().y - size ), true, false,false,true));
                    } else {
                        screen.items.bullets.add(new Bullet(screen, new Vector2(b2body.getPosition().x - 2*size,
                                b2body.getPosition().y - size), false, false,false,true));
                    }
                    attackTimer = 0;
                }
            }

            ////////////////////////////////////walking
            if (Math.abs(screen.player.b2body.getPosition().x - b2body.getPosition().x) < Main.V_WIDTH/Main.PPM &&
                    Math.abs(screen.player.b2body.getPosition().x - b2body.getPosition().x) > Main.V_WIDTH/Main.PPM / 4) {
                if (screen.player.b2body.getPosition().x > b2body.getPosition().x)
                    b2body.setLinearVelocity(2.f, 0);
                else
                    b2body.setLinearVelocity(-2.f, 0);
            }else
                b2body.setLinearVelocity(0,0);


            if(Math.abs(screen.player.b2body.getPosition().x - b2body.getPosition().x) < Main.V_WIDTH/2/Main.PPM)
                isOnScreen = true;
            else
                isOnScreen = false;

        }
        setRegion(getFrame(currentState, stateTimer));
        previousState = currentState;

        healthSprite.setBounds(b2body.getPosition().x - size,b2body.getPosition().y + 1.5f * size
                , 2*size*((float)(bodyhealth-takenBullet)/bodyhealth),size/4);

    }

    @Override
    public void createBody(Vector2 pos, float bodysize) {

        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        fdef.density = 10.f;
        fdef.filter.categoryBits = Main.ENEMY_BIT;
        fdef.filter.maskBits = Main.ENEMY_BIT | Main.GROUND_BIT | Main.BULLET_BIT | Main.PLAYER_BIT
                | Main.BOMB_BIT | Main.URANIUM_BIT ;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(bodysize,bodysize);
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        b2body.setGravityScale(0);
    }


    private void initAnimations(){

        Array<TextureRegion> frames = new Array();
        for (int i = 0; i <= 9; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("animations/enemies.pack",
                    TextureAtlas.class).findRegion("helicopter/attack/"+(i+1))));
        attackAnimation = new Animation(0.08f, frames);
        frames.clear();

        for (int i = 0; i <= 9; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("animations/enemies.pack",
                    TextureAtlas.class).findRegion("helicopter/normal/"+(i+1))));
        idleAnimation = new Animation(0.08f, frames);
        frames.clear();

        for (int i = 0; i <= 10; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("animations/enemies.pack",
                    TextureAtlas.class).findRegion("helicopter/dead/"+(i+1))));
        deadAnimation = new Animation(0.1f, frames);
        frames.clear();
    }

    @Override
    public void draw(Batch batch) {
        if(screen.gameCam.position.x + Ground.groundSize > b2body.getPosition().x && (!canRemove)){
            super.draw(batch);
            healthSprite.draw(batch);
        }
    }
}