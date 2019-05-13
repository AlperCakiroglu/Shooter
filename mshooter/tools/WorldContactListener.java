package com.itujoker.mshooter.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.sprites.MyBody;
import com.itujoker.mshooter.sprites.Player;
import com.itujoker.mshooter.sprites.supplies.Bomb;
import com.itujoker.mshooter.sprites.supplies.Bullet;
import com.itujoker.mshooter.sprites.supplies.Resupply;
import com.itujoker.mshooter.sprites.supplies.Uranium;
import com.itujoker.mshooter.sprites.world.MyPool;
import com.itujoker.mshooter.sprites.world.Rope;
import com.itujoker.mshooter.sprites.world.SandBag;
import com.itujoker.mshooter.sprites.world.Spike;

public class WorldContactListener implements ContactListener {

    private GameScreen screen;

    public WorldContactListener(GameScreen screen) {
        this.screen = screen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case Main.BULLET_BIT | Main.ENEMY_BIT:
            case Main.BULLET_BIT | Main.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == Main.BULLET_BIT) {
                    ((Bullet) fixA.getUserData()).isContact = true;
                    ((Bullet) fixA.getUserData()).contactedBodyType = ((MyBody) fixB.getUserData()).type;
                    if(((Bullet) fixA.getUserData()).isRocket)
                        ((MyBody) fixB.getUserData()).takenBullet = ((MyBody) fixB.getUserData()).bodyhealth;
                    else
                        ((MyBody) fixB.getUserData()).bulletContact = true;
                } else {
                    ((Bullet) fixB.getUserData()).isContact = true;
                    ((Bullet) fixB.getUserData()).contactedBodyType = ((MyBody) fixA.getUserData()).type;
                    if(((Bullet) fixB.getUserData()).isRocket)
                        ((MyBody) fixA.getUserData()).takenBullet = ((MyBody) fixA.getUserData()).bodyhealth;
                    else
                        ((MyBody) fixA.getUserData()).bulletContact = true;
                }
                break;
            case Main.BULLET_BIT | Main.GROUND_BIT:
            case Main.BULLET_BIT | Main.ROPE_BIT:
                if (fixA.getFilterData().categoryBits == Main.BULLET_BIT)
                    ((Bullet) fixA.getUserData()).isContact = true;
                else
                    ((Bullet) fixB.getUserData()).isContact = true;
                break;
            case Main.BULLET_BIT | Main.BULLET_BIT:
                ((Bullet) fixA.getUserData()).isContact = true;
                ((Bullet) fixB.getUserData()).isContact = true;
                break;


            case Main.PLAYER_LADDER_BIT | Main.LADDER_BIT:
                if (fixA.getFilterData().categoryBits == Main.PLAYER_LADDER_BIT) {
                    if (!((Player) fixA.getUserData()).isCrouch) {
                        ((Player) fixA.getUserData()).isOnLadder = true;
                        ((Player) fixA.getUserData()).b2body.setLinearVelocity(0, 0);
                    }
                } else {
                    if (!((Player) fixB.getUserData()).isCrouch) {
                        ((Player) fixB.getUserData()).isOnLadder = true;
                        ((Player) fixB.getUserData()).b2body.setLinearVelocity(0, 0);
                    }
                }
                break;
            case Main.PLAYER_BIT | Main.ROPE_BIT:
                if (fixA.getFilterData().categoryBits == Main.PLAYER_BIT) {
                    if (((Player) fixA.getUserData()).jointTimer > 1 && !((Player) fixA.getUserData()).jointloop) {
                        ((Player) fixA.getUserData()).contactedJointBody = ((Rope) fixB.getUserData()).segments[4];
                        ((Player) fixA.getUserData()).isCreateJoint = true;
                    }
                } else {
                    if (((Player) fixB.getUserData()).jointTimer > 1 && !((Player) fixB.getUserData()).jointloop) {
                        ((Player) fixB.getUserData()).contactedJointBody = ((Rope) fixA.getUserData()).segments[4];
                        ((Player) fixB.getUserData()).isCreateJoint = true;
                    }
                }

                break;

            case Main.PLAYER_BIT | Main.WATER_BIT:
                if (fixA.getFilterData().categoryBits == Main.PLAYER_BIT) {
                    ((Player) fixA.getUserData()).isSwimming = true;
                    ((MyPool) fixB.getUserData()).splashX = ((Player) fixA.getUserData()).b2body.getPosition().x;
                    ((MyPool) fixB.getUserData()).splashTimer = 0;
                    ((MyPool) fixB.getUserData()).showSplash = true;
                    ((MyPool) fixB.getUserData()).splashSound = true;
                }else {
                    ((Player) fixB.getUserData()).isSwimming = true;
                    ((MyPool) fixA.getUserData()).splashX = ((Player) fixB.getUserData()).b2body.getPosition().x;
                    ((MyPool) fixA.getUserData()).splashTimer = 0;
                    ((MyPool) fixA.getUserData()).showSplash = true;
                    ((MyPool) fixA.getUserData()).splashSound = true;
                }
                break;
            case Main.ENEMY_BIT | Main.WATER_BIT:
                if (fixA.getFilterData().categoryBits == Main.ENEMY_BIT)
                    ((MyBody) fixA.getUserData()).takenBullet =
                            ((MyBody) fixB.getUserData()).bodyhealth;
                else
                    ((MyBody) fixB.getUserData()).takenBullet =
                            ((MyBody) fixB.getUserData()).bodyhealth;
                break;

            /////on ground
            case Main.LEG_BIT | Main.GROUND_BIT:
            case Main.LEG_BIT | Main.SANDBAG_BIT:
            case Main.LEG_BIT | Main.SPIKE_BIT:
            case Main.LEG_BIT | Main.ENEMY_BIT:
            case Main.LEG_BIT | Main.URANIUM_BIT:
                if (fixA.getFilterData().categoryBits == Main.LEG_BIT)
                    ((Player) fixA.getUserData()).groundContact = true;
                else
                    ((Player) fixB.getUserData()).groundContact = true;
                break;
            /*case Main.LEG_BIT | Main.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == Main.LEG_BIT){
                    if(((MyBody) fixB.getUserData()).type == 2 )
                        ((Player) fixA.getUserData()).groundContact = true;
                }else{
                    if(((MyBody) fixA.getUserData()).type == 2 )
                        ((Player) fixB.getUserData()).groundContact = true;
                }
                break;*/

            case Main.ENEMY_BIT | Main.GROUND_BIT:
                if (fixA.getFilterData().categoryBits == Main.GROUND_BIT) {
                    ((MyBody) fixB.getUserData()).groundContact = true;
                    if(((MyBody) fixB.getUserData()).type == 3)
                        ((MyBody) fixB.getUserData()).readytoDead();
                } else {
                    ((MyBody) fixA.getUserData()).groundContact = true;
                    if(((MyBody) fixA.getUserData()).type == 3)
                        ((MyBody) fixA.getUserData()).readytoDead();
                }
                break;


            case Main.SUPPLY_BIT | Main.PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == Main.SUPPLY_BIT) {
                    ((Resupply) fixA.getUserData()).playerContact = true;
                } else {
                    ((Resupply) fixB.getUserData()).playerContact = true;
                }
                break;
            case Main.BOMB_BIT | Main.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == Main.BOMB_BIT) {
                    if (((Bomb) fixA.getUserData()).isExploded)
                        ((MyBody) fixB.getUserData()).takenBullet =
                                ((MyBody) fixB.getUserData()).bodyhealth;
                } else {
                    if (((Bomb) fixB.getUserData()).isExploded)
                        ((MyBody) fixA.getUserData()).takenBullet =
                                ((MyBody) fixA.getUserData()).bodyhealth;
                }
                break;


            case Main.URANIUM_BIT | Main.BULLET_BIT:
                if (fixA.getFilterData().categoryBits == Main.URANIUM_BIT){
                    ((Uranium) fixA.getUserData()).isExploded = true;
                    ((Bullet) fixB.getUserData()).isContact = true;
                }
                else {
                    ((Uranium) fixB.getUserData()).isExploded = true;
                    ((Bullet) fixA.getUserData()).isContact = true;
                }break;

            case Main.URANIUM_BIT | Main.BOMB_BIT:
                if (fixA.getFilterData().categoryBits == Main.BOMB_BIT) {
                    if (((Bomb) fixA.getUserData()).isExploded)
                        ((Uranium) fixB.getUserData()).isExploded = true;
                } else {
                    if (((Bomb) fixB.getUserData()).isExploded)
                        ((Uranium) fixA.getUserData()).isExploded = true;
                }
                break;

            case Main.URANIUM_BIT | Main.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == Main.URANIUM_BIT) {
                    if (((Uranium) fixA.getUserData()).isExploded ||
                            ((Uranium) fixA.getUserData()).isBigExplosion)
                        ((MyBody) fixB.getUserData()).takenBullet =
                                ((MyBody) fixB.getUserData()).bodyhealth;
                } else {
                    if (((Uranium) fixB.getUserData()).isExploded ||
                            ((Uranium) fixB.getUserData()).isBigExplosion)
                        ((MyBody) fixA.getUserData()).takenBullet =
                                ((MyBody) fixA.getUserData()).bodyhealth;
                }
                break;


            case Main.PLAYER_BIT | Main.SPIKE_BIT:
                if (fixA.getFilterData().categoryBits == Main.PLAYER_BIT)
                    ((Player) fixA.getUserData()).takenBullet =
                            ((Player) fixA.getUserData()).bodyhealth;
                else
                    ((Player) fixB.getUserData()).takenBullet =
                            ((Player) fixB.getUserData()).bodyhealth;
                break;


            case Main.ENEMY_BIT | Main.SPIKE_BIT:
                if (fixA.getFilterData().categoryBits == Main.ENEMY_BIT)
                    ((MyBody) fixA.getUserData()).takenBullet = ((MyBody) fixA.getUserData()).bodyhealth ;
                else
                    ((MyBody) fixB.getUserData()).takenBullet = ((MyBody) fixB.getUserData()).bodyhealth ;
                break;
            case Main.ENEMY_BIT | Main.ENEMY_BIT:
            case Main.ENEMY_BIT | Main.SANDBAG_BIT:
                if (fixA.getFilterData().categoryBits == Main.SANDBAG_BIT)
                    ((MyBody) fixB.getUserData()).enemyContact = true;
                else if (fixB.getFilterData().categoryBits == Main.SANDBAG_BIT)
                    ((MyBody) fixA.getUserData()).enemyContact = true;
                else {
                    ((MyBody) fixB.getUserData()).enemyContact = true;
                    ((MyBody) fixA.getUserData()).enemyContact = true;

                    if(((MyBody) fixA.getUserData()).type == 3 )
                        ((MyBody) fixA.getUserData()).takenBullet = ((MyBody) fixA.getUserData()).bodyhealth;
                    if(((MyBody) fixB.getUserData()).type == 3 )
                        ((MyBody) fixB.getUserData()).takenBullet = ((MyBody) fixB.getUserData()).bodyhealth;
                }
                break;


            case Main.BOMB_BIT | Main.SANDBAG_BIT:
                if (fixA.getFilterData().categoryBits == Main.SANDBAG_BIT){
                    if( ((Bomb) fixB.getUserData()).isExploded)
                        ((SandBag) fixA.getUserData()).isKaput = true;
                }else{
                    if( ((Bomb) fixA.getUserData()).isExploded)
                        ((SandBag) fixB.getUserData()).isKaput = true;
                }

                break;
            case Main.URANIUM_BIT | Main.SANDBAG_BIT:
                if (fixA.getFilterData().categoryBits == Main.SANDBAG_BIT){
                    if( ((Uranium) fixB.getUserData()).isExploded)
                        ((SandBag) fixA.getUserData()).isKaput = true;
                }else{
                    if( ((Uranium) fixA.getUserData()).isExploded)
                        ((SandBag) fixB.getUserData()).isKaput = true;
                }

                break;
            case Main.BULLET_BIT | Main.SANDBAG_BIT:
                if (fixA.getFilterData().categoryBits == Main.BULLET_BIT){
                    ((Bullet) fixA.getUserData()).isContact = true;
                    if(((Bullet) fixA.getUserData()).isRocket && ((Bullet) fixA.getUserData()).rocketExplosion)
                        ((SandBag) fixB.getUserData()).isKaput = true;
                }
                else {
                    ((Bullet) fixB.getUserData()).isContact = true;
                    if(((Bullet) fixB.getUserData()).isRocket && ((Bullet) fixB.getUserData()).rocketExplosion)
                        ((SandBag) fixA.getUserData()).isKaput = true;
                }
                break;

            case Main.BOMB_BIT | Main.SPIKE_BIT:
                if (fixA.getFilterData().categoryBits == Main.SPIKE_BIT){
                    if( ((Bomb) fixB.getUserData()).isExploded)
                        ((Spike) fixA.getUserData()).isKaput = true;
                }else{
                    if( ((Bomb) fixA.getUserData()).isExploded)
                        ((Spike) fixB.getUserData()).isKaput = true;
                }

                break;
            case Main.URANIUM_BIT | Main.SPIKE_BIT:
                if (fixA.getFilterData().categoryBits == Main.SPIKE_BIT){
                    if( ((Uranium) fixB.getUserData()).isExploded)
                        ((Spike) fixA.getUserData()).isKaput = true;
                }else{
                    if( ((Uranium) fixA.getUserData()).isExploded)
                        ((Spike) fixB.getUserData()).isKaput = true;
                }

                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case Main.PLAYER_LADDER_BIT | Main.LADDER_BIT:
                if (fixA.getFilterData().categoryBits == Main.PLAYER_LADDER_BIT) {
                    ((Player) fixA.getUserData()).isOnLadder = false;
                } else {
                    ((Player) fixB.getUserData()).isOnLadder = false;
                }
                break;


            case Main.ENEMY_BIT | Main.GROUND_BIT:
                if (fixA.getFilterData().categoryBits == Main.GROUND_BIT) {
                    ((MyBody) fixB.getUserData()).groundContact = false;
                } else {
                    ((MyBody) fixA.getUserData()).groundContact = false;
                }
                break;

            case Main.ENEMY_BIT | Main.ENEMY_BIT:
            case Main.ENEMY_BIT | Main.SANDBAG_BIT:
                if (fixA.getFilterData().categoryBits == Main.SANDBAG_BIT)
                    ((MyBody) fixB.getUserData()).enemyContact = false;
                else if (fixB.getFilterData().categoryBits == Main.SANDBAG_BIT)
                    ((MyBody) fixA.getUserData()).enemyContact = false;
                else {
                    ((MyBody) fixB.getUserData()).enemyContact = false;
                    ((MyBody) fixA.getUserData()).enemyContact = false;
                }
                break;
            case Main.PLAYER_BIT | Main.WATER_BIT:
                if (fixA.getFilterData().categoryBits == Main.PLAYER_BIT)
                    ((Player) fixA.getUserData()).isSwimming = false;
                else
                    ((Player) fixB.getUserData()).isSwimming = false;
                break;

        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}