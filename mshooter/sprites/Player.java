package com.itujoker.mshooter.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.tools.Main;

public class Player extends MyBody {


    private int playerType;
    public boolean isOnLadder, isCrouch, isToss, isRocket, isSwimming;

    public boolean isCreateJoint, isDestroyJoint;
    public RevoluteJoint ropeJoint;
    public boolean isOnRope;
    private boolean jointlock;
    public float jointTimer = 0;
    public Body contactedJointBody;
    public boolean jointloop;

    public boolean walkingSoundRunning,deadSoundRun,deadEffectRun;

    public boolean canQuit;

    public int bombnumber = 9, rocketNumber = 9;

    private Animation idleAnimation, attackAnimation, jumpAnimation,
            climbAnimation, runAnimation, jumpAttackAnimation, runAttackAnimation,
            tossAnimation, swimAnimation, rocketAnimation, deadAnimation;

    public Player(GameScreen screen, int playerType, Vector2 pos) {
        super(screen);

        this.playerType = playerType;

        canQuit = false;
        takenBullet = 0;
        bodyhealth = 10;

        type = 1;
        size = Main.tileWidth / 3 / Main.PPM;
        runningRight = true;

        createBody(pos, size);
        initAnimations();
    }

    public State getState() {
        if (isDead)
            return State.DEAD;

        else if (isSwimming)
            return State.SWIMMING;

        else if ((isOnLadder) && b2body.getLinearVelocity().y != 0)
            return State.CLIMBING;
        else if (isOnLadder || isOnRope)
            return State.STANDINGCLIMB;

        else if (isToss)
            return State.TOSS;
        else if (isRocket)
            return State.ROCKET;


        else if (isCrouch && isAttacking && Math.abs(b2body.getLinearVelocity().x) > 0.2f)
            return State.CROUCHRUNATTACKING;
        else if (isCrouch && isAttacking && Math.abs(b2body.getLinearVelocity().x) < 0.2f)
            return State.CROUCHATTACKING;
        else if (isCrouch && Math.abs(b2body.getLinearVelocity().x) > 0.2f)
            return State.CROUCHRUNNING;
        else if (isCrouch)
            return State.CROUCH;

        else if (isAttacking && Math.abs(b2body.getLinearVelocity().x) < 0.2f && Math.abs(b2body.getLinearVelocity().y) < 0.2f)
            return State.ATTACKING;
        else if (isAttacking && Math.abs(b2body.getLinearVelocity().x) > 0.2f && Math.abs(b2body.getLinearVelocity().y) < 0.2f)
            return State.RUNATTACKING;
        else if (isAttacking && !groundContact)
            return State.JUMPATTACKING;


        else if (Math.abs(b2body.getLinearVelocity().x) > 0.2f && groundContact)
            return State.RUNNING;
        else if (!groundContact)
            return State.JUMPING;
        else
            return State.IDLE;
    }

    public TextureRegion getFrame(State state, float dt) {

        TextureRegion region;
        switch (state) {

            case DEAD:
                region = (TextureRegion) deadAnimation.getKeyFrame(dt);
                if(dt>0.6f && screen.game.soundOn && !deadSoundRun && b2body.getPosition().y > 0 && playerType == 1){
                    deadSoundRun = true;
                    screen.game.assets.get("music/explosion.ogg", Sound.class).stop();
                    screen.game.assets.get("music/explosion.ogg", Sound.class).play(0.5f);
                }
                else if(screen.game.soundOn && b2body.getPosition().y < 0 && !deadSoundRun){
                    deadSoundRun = true;
                    screen.game.assets.get("music/bubbles.ogg", Sound.class).stop();
                    screen.game.assets.get("music/bubbles.ogg", Sound.class).play();
                }
                if(deadAnimation.isAnimationFinished(dt))
                    canQuit = true;
                break;
            case ATTACKING:
                region = (TextureRegion) attackAnimation.getKeyFrame(dt, true);
                break;
            case JUMPATTACKING:
                region = (TextureRegion) jumpAttackAnimation.getKeyFrame(dt, true);
                break;
            case RUNATTACKING:
                region = (TextureRegion) runAttackAnimation.getKeyFrame(dt, true);
                break;

            case SWIMMING:
                region = (TextureRegion) swimAnimation.getKeyFrame(dt, true);
                break;
            case TOSS:
                region = (TextureRegion) tossAnimation.getKeyFrame(dt);
                if (tossAnimation.isAnimationFinished(dt))
                    isToss = false;
                break;
            case ROCKET:
                region = (TextureRegion) rocketAnimation.getKeyFrame(dt);
                if (rocketAnimation.isAnimationFinished(dt))
                    isRocket = false;
                break;

            case JUMPING:
                region = (TextureRegion) jumpAnimation.getKeyFrame(dt);
                break;
            case CLIMBING:
                region = (TextureRegion) climbAnimation.getKeyFrame(dt, true);
                break;
            case STANDINGCLIMB:
                region = (TextureRegion) climbAnimation.getKeyFrame(0);
                break;
            case RUNNING:
                region = (TextureRegion) runAnimation.getKeyFrame(dt, true);
                break;


            case CROUCHRUNATTACKING:
                region = (TextureRegion) runAttackAnimation.getKeyFrame(dt, true);
                break;
            case CROUCHATTACKING:///////standing
                region = (TextureRegion) attackAnimation.getKeyFrame(dt, true);
                break;
            case CROUCHRUNNING:
                region = (TextureRegion) runAnimation.getKeyFrame(dt, true);
                break;
            case CROUCH:
                region = (TextureRegion) idleAnimation.getKeyFrame(dt, true);
                break;


            case IDLE:
            default:
                region = (TextureRegion) idleAnimation.getKeyFrame(dt, true);
                break;
        }

        if ((b2body.getLinearVelocity().x >= 1.f || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;


        } else if ((b2body.getLinearVelocity().x <= -1.f || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;

        }


        if (!isCrouch)
            setBounds(b2body.getPosition().x - 1.5f * size
                    , b2body.getPosition().y - size
                    , 3 * size, 3 * size);
        else
            setBounds(b2body.getPosition().x - 1.5f * size
                    , b2body.getPosition().y - 0.75f * size
                    , 3 * size, 2.5f * size);
        return region;

    }

    public void update(float dt) {

        currentState = getState();
        stateTimer = currentState == previousState ? stateTimer + dt : 0;

        jointTimer += dt;

        if (!isDead) {

            if (bulletContact) {
                takenBullet++;
                bulletContact = false;
            }
            if (takenBullet > bodyhealth)
                takenBullet = bodyhealth;


            if (b2body.getPosition().y < -1 || takenBullet == bodyhealth)
                isDead = true;

            if (b2body.getLinearVelocity().x > 1.f)
                runningRight = true;
            else if (b2body.getLinearVelocity().x < -1.f)
                runningRight = false;

            if (Math.abs(b2body.getLinearVelocity().y) > 2)
                groundContact = false;

            if (isOnLadder || isSwimming)
                b2body.setGravityScale(0);
            else
                b2body.setGravityScale(1);


            if (currentState == State.RUNNING && screen.game.soundOn && !walkingSoundRunning) {
                screen.game.assets.get("music/walk.ogg", Sound.class).stop();
                screen.game.assets.get("music/walk.ogg", Sound.class).play(0.1f);
                screen.game.assets.get("music/walk.ogg", Sound.class).loop();
                walkingSoundRunning = true;
            } else if (currentState != State.RUNNING && screen.game.soundOn && walkingSoundRunning) {
                walkingSoundRunning = false;
                screen.game.assets.get("music/walk.ogg", Sound.class).stop();
            }

        }else{
            if(screen.game.soundOn && !deadEffectRun){
                deadEffectRun = true;
                screen.game.assets.get("music/dead.ogg", Sound.class).play(0.5f);
            }
        }
        setRegion(getFrame(currentState, stateTimer));
        previousState = currentState;
    }


    public void createBody(Vector2 pos, float bodysize) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(pos);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);
        b2body.setFixedRotation(true);

        FixtureDef fdef = new FixtureDef();
        fdef.density = 1.f;
        fdef.filter.categoryBits = Main.PLAYER_BIT;
        fdef.filter.maskBits = Main.GROUND_BIT | Main.BULLET_BIT | Main.ENEMY_BIT | Main.SUPPLY_BIT
                | Main.SANDBAG_BIT | Main.URANIUM_BIT | Main.SPIKE_BIT | Main.ROPE_BIT | Main.WATER_BIT;
        CircleShape circle = new CircleShape();
        circle.setRadius(bodysize);
        fdef.shape = circle;
        b2body.createFixture(fdef).setUserData(this);

        /////ladder
        circle.setRadius(size / 3);
        fdef.shape = circle;
        fdef.filter.categoryBits = Main.PLAYER_LADDER_BIT;
        fdef.filter.maskBits = Main.LADDER_BIT;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

        /////leg
        PolygonShape leg = new PolygonShape();
        leg.setAsBox(bodysize / 2, bodysize / 2, new Vector2(0, -bodysize/2), 0);
        fdef.filter.categoryBits = Main.LEG_BIT;
        fdef.filter.maskBits = Main.GROUND_BIT | Main.SANDBAG_BIT | Main.SPIKE_BIT | Main.ENEMY_BIT | Main.URANIUM_BIT;
        fdef.shape = leg;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);

    }

    private void initAnimations() {

        Array<TextureRegion> frames = new Array();

        if(playerType == 1) {
            for (int i = 0; i <= 4; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/arnie_jungle.pack",
                        TextureAtlas.class).findRegion("fire/" + (i + 1))));
            attackAnimation = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 0; i <= 7; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/arnie_jungle.pack",
                        TextureAtlas.class).findRegion("idle/" + (i + 1))));
            idleAnimation = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 0; i <= 4; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/arnie_jungle.pack",
                        TextureAtlas.class).findRegion("jumping/" + (i + 1))));
            jumpAnimation = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 0; i <= 10; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/arnie_jungle.pack",
                        TextureAtlas.class).findRegion("climbing/" + (i + 1))));
            climbAnimation = new Animation(0.05f, frames);
            frames.clear();

            for (int i = 0; i <= 6; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/arnie_jungle.pack",
                        TextureAtlas.class).findRegion("running/" + (i + 1))));
            runAnimation = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 0; i <= 4; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/arnie_jungle.pack",
                        TextureAtlas.class).findRegion("jumpfire/" + (i + 1))));
            jumpAttackAnimation = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 0; i <= 13; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/arnie_jungle.pack",
                        TextureAtlas.class).findRegion("walkfire/" + (i + 1))));
            runAttackAnimation = new Animation(0.05f, frames);
            frames.clear();

            for (int i = 0; i <= 8; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/arnie_jungle.pack",
                        TextureAtlas.class).findRegion("toss/" + (i + 1))));
            tossAnimation = new Animation(0.05f, frames);
            frames.clear();

            for (int i = 0; i <= 4; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/arnie_jungle.pack",
                        TextureAtlas.class).findRegion("rocketa/" + (i + 1))));
            rocketAnimation = new Animation(0.1f, frames);
            frames.clear();

            frames.add(new TextureRegion(screen.getGame().assets.get("animations/arnie_jungle.pack",
                    TextureAtlas.class).findRegion("idle/1")));
            frames.add(new TextureRegion(screen.getGame().assets.get("animations/arnie_jungle.pack",
                    TextureAtlas.class).findRegion("jumping/3")));
            swimAnimation = new Animation(0.4f, frames);
            frames.clear();

            for (int i = 0; i <= 21; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/arnie_jungle.pack",
                        TextureAtlas.class).findRegion("defeated/" + (i + 1))));
            deadAnimation = new Animation(0.1f, frames);
            frames.clear();

        }else{

            for (int i = 0; i <= 4; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/sly.pack",
                        TextureAtlas.class).findRegion("fire/" + (i + 1))));
            attackAnimation = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 0; i <= 7; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/sly.pack",
                        TextureAtlas.class).findRegion("idle/" + (i + 1))));
            idleAnimation = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 0; i <= 4; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/sly.pack",
                        TextureAtlas.class).findRegion("jumping/" + (i + 1))));
            jumpAnimation = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 0; i <= 10; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/sly.pack",
                        TextureAtlas.class).findRegion("climbing/" + (i + 1))));
            climbAnimation = new Animation(0.05f, frames);
            frames.clear();

            for (int i = 0; i <= 6; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/sly.pack",
                        TextureAtlas.class).findRegion("running/" + (i + 1))));
            runAnimation = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 0; i <= 4; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/sly.pack",
                        TextureAtlas.class).findRegion("jumpfire/" + (i + 1))));
            jumpAttackAnimation = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 0; i <= 13; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/sly.pack",
                        TextureAtlas.class).findRegion("walkfire/" + (i + 1))));
            runAttackAnimation = new Animation(0.05f, frames);
            frames.clear();

            for (int i = 0; i <= 8; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/sly.pack",
                        TextureAtlas.class).findRegion("toss/" + (i + 1))));
            tossAnimation = new Animation(0.05f, frames);
            frames.clear();

            for (int i = 0; i <= 4; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/sly.pack",
                        TextureAtlas.class).findRegion("rocketa/" + (i + 1))));
            rocketAnimation = new Animation(0.1f, frames);
            frames.clear();

            frames.add(new TextureRegion(screen.getGame().assets.get("animations/sly.pack",
                    TextureAtlas.class).findRegion("idle/1")));
            frames.add(new TextureRegion(screen.getGame().assets.get("animations/sly.pack",
                    TextureAtlas.class).findRegion("jumping/3")));
            swimAnimation = new Animation(0.4f, frames);
            frames.clear();

            for (int i = 0; i <= 6; i++)
                frames.add(new TextureRegion(screen.getGame().assets.get("animations/sly.pack",
                        TextureAtlas.class).findRegion("defeated/" + (i + 1))));
            deadAnimation = new Animation(0.12f, frames);
            frames.clear();

        }
    }

    public void crouchBody(int type) {

        readytoDead();
        isDead = false;
        screen.items.removeBodyArray.add(b2body);
        if (type == 1)
            createBody(b2body.getPosition(), size * 0.75f);
        else
            createBody(b2body.getPosition(), size);
    }


    public void jointOperations() {
        if (isCreateJoint && !jointlock) {
            jointloop = true;
            isOnRope = true;
            isCreateJoint = false;

            RevoluteJointDef revoluteJointDef = new RevoluteJointDef();
            revoluteJointDef.localAnchorA.set(0, 0);
            revoluteJointDef.localAnchorB.set(0, 0);

            revoluteJointDef.bodyA = contactedJointBody;
            revoluteJointDef.bodyB = b2body;
            ropeJoint = (RevoluteJoint) screen.getWorld().createJoint(revoluteJointDef);

            jointlock = true;

        }

        if (isDestroyJoint && jointlock) {

            isOnRope = false;
            isDestroyJoint = false;
            screen.getWorld().destroyJoint(ropeJoint);
            jointTimer = 0;

            jointlock = false;
            jointloop = false;
        }
    }
}
