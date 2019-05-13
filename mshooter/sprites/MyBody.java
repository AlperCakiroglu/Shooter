package com.itujoker.mshooter.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.tools.Main;

public abstract class MyBody extends Sprite {

    public enum State {JUMPING, IDLE, RUNNING, DEAD, CROUCH, CROUCHRUNNING, CLIMBING, STANDINGCLIMB,
        ATTACKING, JUMPATTACKING, CROUCHATTACKING, CROUCHRUNATTACKING, RUNATTACKING, TOSS, ROCKET, SWIMMING }

    public GameScreen screen;
    public World world;
    public Body b2body;
    public float size;
    public int type;/////helicopter de 3

    public State currentState, previousState;
    public Animation bombAnimation;

    public boolean isDead, isAttacking;
    public boolean runningRight;
    public boolean bulletContact;
    public boolean groundContact;
    public boolean canRemove;
    public boolean enemyContact;///enemy enemy ve sandbag

    public int takenBullet = 0;
    public int bodyhealth;

    public float stateTimer = 0;

    protected Sprite healthSprite;

    public MyBody(GameScreen screen){

        this.screen = screen;
        this.world = screen.getWorld();

        healthSprite = new Sprite();
        healthSprite.setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                TextureAtlas.class).findRegion("health")));

        bombAnimationReady();
        currentState = State.IDLE;
        previousState = State.IDLE;

    }

    public abstract State getState();
    public abstract void update(float dt);
    public abstract void createBody(Vector2 pos, float bodysize);
    public void readytoDead(){
        isDead = true;

        Filter filter = new Filter();
        filter.categoryBits = Main.DESTROYED_BIT;

        for (Fixture fixture : b2body.getFixtureList()) {
            fixture.setFilterData(filter);
        }

        stateTimer = 0;
    }

    private void bombAnimationReady(){
        Array<TextureRegion> frames = new Array();
        for (int i = 0; i <= 11; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("bomb/"+(i+1))));
        bombAnimation = new Animation(0.1f, frames);
        frames.clear();
    }
}
