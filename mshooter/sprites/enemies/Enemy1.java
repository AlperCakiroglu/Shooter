package com.itujoker.mshooter.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.sprites.MyBody;
import com.itujoker.mshooter.sprites.supplies.Bullet;
import com.itujoker.mshooter.sprites.world.Ground;
import com.itujoker.mshooter.tools.Main;

public class Enemy1 extends MyBody {

    private Animation idleAnimation, attackAnimation, runAnimation, deadAnimation;

    public Enemy1(GameScreen screen, Vector2 pos) {
        super(screen);

        type = 5;
        size = Main.tileWidth / 3 / Main.PPM;
        bodyhealth = 3;
        createBody(new Vector2(pos),size);
        initAnimations();
    }

    @Override
    public State getState() {

        if(isDead)
            return State.DEAD;
        else if(isAttacking)
            return State.ATTACKING;
        else if(b2body.getLinearVelocity().x != 0)
            return  State.RUNNING;
        else
            return State.IDLE;

    }

    public TextureRegion getFrame(State state, float dt){

        TextureRegion region;
        switch (state){

            case DEAD:
                region = (TextureRegion) deadAnimation.getKeyFrame(dt);
                break;
            case ATTACKING:
                region = (TextureRegion) attackAnimation.getKeyFrame(dt);
                if(attackAnimation.isAnimationFinished(dt))
                    isAttacking = false;

                break;
            case RUNNING:
                region = (TextureRegion) runAnimation.getKeyFrame(dt, true);
                break;
            case IDLE:
            default:
                region = (TextureRegion) idleAnimation.getKeyFrame(dt, true);
                break;
        }

        if(!isDead) {
            if ((screen.player.b2body.getPosition().x > b2body.getPosition().x) && region.isFlipX()) {
                region.flip(true, false);
                runningRight = true;


            } else if ((screen.player.b2body.getPosition().x < b2body.getPosition().x) && !region.isFlipX()) {
                region.flip(true, false);
                runningRight = false;

            }
        }
        else{
            if (runningRight && region.isFlipX()) {
                region.flip(true, false);
                runningRight = true;
            } else if (!runningRight && !region.isFlipX()) {
                region.flip(true, false);
                runningRight = false;

            }
        }
        setBounds(b2body.getPosition().x - 1.5f*size
                ,b2body.getPosition().y - size
                , 3*size, 2.5f*size);

        return region;

    }

    @Override
    public void update(float dt) {

        currentState = getState();
        stateTimer = currentState == previousState ? stateTimer + dt : 0;

        if(!isDead) {
            if (b2body.getLinearVelocity().x > 1.f)
                runningRight = true;
            else if (b2body.getLinearVelocity().x < -1.f)
                runningRight = false;

            if (bulletContact) {
                takenBullet++;
                bulletContact = false;

            }
            if (takenBullet >= bodyhealth || b2body.getPosition().y < 0 || screen.gameCam.position.x > b2body.getPosition().x + Ground.groundSize) {
                readytoDead();
                screen.score += 100;
            }

            if(takenBullet > bodyhealth)
                takenBullet = bodyhealth;

            ////////////////////////////////////walking
            if (Math.abs(screen.player.b2body.getPosition().x - b2body.getPosition().x) < screen.gamePort.getWorldWidth() &&
                    Math.abs(screen.player.b2body.getPosition().x - b2body.getPosition().x) > screen.gamePort.getWorldWidth() / 3 &&
                    Math.abs(screen.player.b2body.getPosition().y - b2body.getPosition().y) < size && !enemyContact) {
                if (screen.player.b2body.getPosition().x > b2body.getPosition().x)
                    b2body.setLinearVelocity(2.f, b2body.getLinearVelocity().y);
                else
                    b2body.setLinearVelocity(-2.f, b2body.getLinearVelocity().y);
            }

            //////////////////////////////////attacking
            if (Math.abs(b2body.getLinearVelocity().x) < 0.5f
                    && Math.abs(screen.player.b2body.getPosition().x - b2body.getPosition().x) < screen.gamePort.getWorldWidth()/2
                    && Math.abs(screen.player.b2body.getPosition().y - b2body.getPosition().y) < size
                    && !isAttacking && !screen.player.isDead) {
                isAttacking = true;
                if (runningRight) {
                    screen.items.bullets.add(new Bullet(screen, new Vector2(b2body.getPosition().x + 1.1f*size,
                            b2body.getPosition().y + size / 2.5f), true, false, false, false));
                } else {
                    screen.items.bullets.add(new Bullet(screen, new Vector2(b2body.getPosition().x - 1.1f*size,
                            b2body.getPosition().y + size / 2.5f), false, false, false, false));
                }


            }

        }
        setRegion(getFrame(currentState, stateTimer));
        previousState = currentState;


        healthSprite.setBounds(b2body.getPosition().x - size,b2body.getPosition().y + 1.5f * size
                , 2*size*((float)(bodyhealth-takenBullet)/bodyhealth),size/4);

    }

    @Override
    public void createBody(Vector2 pos,float bodysize) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        fdef.density = 10.f;
        fdef.filter.categoryBits = Main.ENEMY_BIT;
        fdef.filter.maskBits = Main.GROUND_BIT | Main.BULLET_BIT | Main.PLAYER_BIT | Main.ENEMY_BIT
                | Main.BOMB_BIT | Main.URANIUM_BIT | Main.SANDBAG_BIT | Main.SPIKE_BIT | Main.WATER_BIT | Main.LEG_BIT;
        CircleShape circle = new CircleShape();
        circle.setRadius(bodysize);
        fdef.shape = circle;
        b2body.createFixture(fdef).setUserData(this);
    }

    private void initAnimations(){

        Array<TextureRegion> frames = new Array();
        for (int i = 0; i <= 5; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("animations/enemies.pack",
                    TextureAtlas.class).findRegion("enemy1/attack/"+(i+1))));
        attackAnimation = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 0; i <= 5; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("animations/enemies.pack",
                    TextureAtlas.class).findRegion("enemy1/dead/"+(i+1))));
        deadAnimation = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 0; i <= 7; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("animations/enemies.pack",
                    TextureAtlas.class).findRegion("enemy1/idle/"+(i+1))));
        idleAnimation = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 0; i <= 8; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("animations/enemies.pack",
                    TextureAtlas.class).findRegion("enemy1/running/"+(i+1))));
        runAnimation = new Animation(0.1f, frames);
        frames.clear();
    }


    @Override
    public void draw(Batch batch) {
        if(screen.gameCam.position.x + Ground.groundSize > b2body.getPosition().x && (!isDead || stateTimer<2)){
            super.draw(batch);
            healthSprite.draw(batch);
        }
        else if(stateTimer>2 && isDead)
            canRemove = true;
    }
}

