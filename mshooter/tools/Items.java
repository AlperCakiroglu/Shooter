package com.itujoker.mshooter.tools;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.sprites.MyBody;
import com.itujoker.mshooter.sprites.enemies.Enemy1;
import com.itujoker.mshooter.sprites.enemies.Helicopter;
import com.itujoker.mshooter.sprites.enemies.MountedGun;
import com.itujoker.mshooter.sprites.enemies.Tank;
import com.itujoker.mshooter.sprites.supplies.Bomb;
import com.itujoker.mshooter.sprites.supplies.Bullet;
import com.itujoker.mshooter.sprites.supplies.Resupply;
import com.itujoker.mshooter.sprites.supplies.Uranium;
import com.itujoker.mshooter.sprites.world.Background;
import com.itujoker.mshooter.sprites.world.Bridge;
import com.itujoker.mshooter.sprites.world.Ground;
import com.itujoker.mshooter.sprites.world.Ladder;
import com.itujoker.mshooter.sprites.world.Lava;
import com.itujoker.mshooter.sprites.world.Misc;
import com.itujoker.mshooter.sprites.world.MyPool;
import com.itujoker.mshooter.sprites.world.Rope;
import com.itujoker.mshooter.sprites.world.SandBag;
import com.itujoker.mshooter.sprites.world.Spike;

public class Items {

    public GameScreen screen;

    public Array<Body> removeBodyArray ;

    public Array<Bullet> bullets;
    public Array<Enemy1> enemy1s;
    public Array<MountedGun> mountedGuns;
    public Array<Helicopter> helicopters;
    public Array<Tank> tanks;
    public Array<Ladder> ladders;
    public Array<Resupply> resupplies;
    public Array<Bomb> bombs;
    public Array<Uranium> uraniums;
    public Array<SandBag> sandBags;
    public Array<Spike> spikes;
    public Array<Misc> miscs;
    public Array<Bridge> bridges;
    public Array<Rope> ropes;
    public Array<MyPool> myPools;

    public Ground ground1, ground2, ground3, ground4;
    private Lava lava;
    private Background background1, background2, background3, background4;

    public int connectionType = 1;
    //public int miscType = 1;

    private boolean runVehiclecAttackSound,runHelicopterSound, runEnemyAttackSound;

    public Items(GameScreen screen) {

        this.screen = screen;


        removeBodyArray = new Array<Body>();

        helicopters = new Array<Helicopter>();
        bullets = new Array<Bullet>();
        enemy1s = new Array<Enemy1>();
        mountedGuns = new Array<MountedGun>();
        tanks = new Array<Tank>();
        ladders = new Array<Ladder>();
        resupplies = new Array<Resupply>();
        bombs = new Array<Bomb>();
        uraniums = new Array<Uranium>();
        sandBags = new Array<SandBag>();
        spikes = new Array<Spike>();
        miscs = new Array<Misc>();
        bridges = new Array<Bridge>();
        ropes = new Array<Rope>();
        myPools = new Array<MyPool>();
    }

    public void initialize(){

        lava = new Lava(screen);

        background1 = new Background(screen,1);
        background2 = new Background(screen,2);
        background3 = new Background(screen,3);
        background4 = new Background(screen,4);

        ground1 = new Ground(screen ,null ,new Vector2(Ground.groundSize / 2, 2),1);
        ground2 = new Ground(screen ,ground1 ,new Vector2(ground1.b2body.getPosition().x + Ground.groundSize + Bridge.bridgeLength, 4),2);
        ground3 = new Ground(screen ,ground2 ,new Vector2(ground2.b2body.getPosition().x + Ground.groundSize + Bridge.bridgeLength, 2),3);
        ground4 = new Ground(screen ,ground3 ,new Vector2(ground3.b2body.getPosition().x + Ground.groundSize, 4),4);
        ground1.setPreviousGround(ground4);



    }
    public void update(float dt){


        for(Body body:removeBodyArray)///remove Bodies
            screen.getWorld().destroyBody(body);
        removeBodyArray.clear();

        for(Bullet bullet:bullets){///remove Bullets
            if( bullet.canRemove) {
                bullets.removeValue(bullet, true);
                removeBodyArray.add(bullet.b2body);
                bullet = null;
            }
            else
                bullet.update(dt);

        }

        runEnemyAttackSound = false;
        for(Enemy1 enemy1: enemy1s){

            if(enemy1.currentState == MyBody.State.ATTACKING)
                runEnemyAttackSound = true;

            if(enemy1.canRemove){
                enemy1s.removeValue(enemy1, true);
                removeBodyArray.add(enemy1.b2body);
                enemy1 = null;
            }
            else
                enemy1.update(dt);
        }

        for(MountedGun mountedGun : mountedGuns){

            if(mountedGun.isAttacking)
                runEnemyAttackSound = true;

            if(mountedGun.canRemove){
                mountedGuns.removeValue(mountedGun, true);
                removeBodyArray.add(mountedGun.b2body);
                mountedGun = null;
            }
            else
                mountedGun.update(dt);
        }


        runVehiclecAttackSound = false;runHelicopterSound = false;
        for(Helicopter helicopter: helicopters){

            if(helicopter.currentState == MyBody.State.ATTACKING)
                runVehiclecAttackSound = true;

            if(helicopter.isOnScreen)
                runHelicopterSound = true;

            if(helicopter.canRemove){
                helicopters.removeValue(helicopter, true);
                removeBodyArray.add(helicopter.b2body);
                helicopter = null;
            }
            else
                helicopter.update(dt);
        }
        for(Tank tank : tanks){

            if(tank.currentState == MyBody.State.ATTACKING)
                runVehiclecAttackSound = true;

            if(tank.canRemove){
                tanks.removeValue(tank, true);
                removeBodyArray.add(tank.b2body);
                tank = null;
            }
            else
                tank.update(dt);
        }

        for(Ladder ladder : ladders){

            if(screen.gameCam.position.x > ladder.b2body.getPosition().x + Ground.groundSize) {
                ladders.removeValue(ladder, true);
                removeBodyArray.add(ladder.b2body);
                ladder = null;
            }

        }
        for(Resupply resupply: resupplies){
            if(resupply.canRemove){
                resupplies.removeValue(resupply, true);
                removeBodyArray.add(resupply.b2body);
                resupply = null;
            }
            else
                resupply.update();
        }
        for(Bomb bomb: bombs){
            if(bomb.canRemove){
                bombs.removeValue(bomb, true);
                removeBodyArray.add(bomb.b2body);
                bomb = null;
            }
            else
                bomb.update(dt);
        }

        for(Uranium uranium: uraniums){
            if(uranium.canRemove){
                uraniums.removeValue(uranium, true);
                removeBodyArray.add(uranium.b2body);
                uranium = null;
            }
            else
                uranium.update(dt);
        }

        for(SandBag sandBag : sandBags){
            if(sandBag.canRemove){
                sandBags.removeValue(sandBag, true);
                removeBodyArray.add(sandBag.b2body);
                sandBag = null;
            }
            else
                sandBag.update(dt);
        }

        for(Spike spike : spikes){
            if(spike.canRemove){
                spikes.removeValue(spike, true);
                removeBodyArray.add(spike.b2body);
                spike = null;
            }
            else
                spike.update();
        }

        for(Misc misc : miscs){
            if(misc.canRemove){
                miscs.removeValue(misc, true);
                misc = null;
            }
            else
                misc.update(dt);
        }


        for(Bridge bridge : bridges){

            if(screen.gameCam.position.x > bridge.segments[5].getPosition().x + Ground.groundSize) {
                bridges.removeValue(bridge, true);
                for(int i=0;i<bridge.segments.length;i++)
                    removeBodyArray.add(bridge.segments[i]);
                bridge = null;
            }
            else
                bridge.update();

        }

        for(Rope rope : ropes){

            if(screen.gameCam.position.x > rope.segments[0].getPosition().x + Ground.groundSize) {
                ropes.removeValue(rope, true);
                for(int i=0;i<rope.segments.length;i++)
                    removeBodyArray.add(rope.segments[i]);
                rope = null;
            }
            else
                rope.update();

        }


        for(MyPool myPool : myPools){
            if(screen.gameCam.position.x > myPool.outBody.getPosition().x + Ground.groundSize){
                myPools.removeValue(myPool, true);
                removeBodyArray.add(myPool.outBody);
                myPool = null;
            }
            else
                myPool.update(dt);
        }


        lava.update(dt);

        background1.update();
        background2.update();
        background3.update();
        background4.update();

        ground1.update();
        ground2.update();
        ground3.update();
        ground4.update();


        if(screen.game.soundOn && runVehiclecAttackSound &&
                !screen.game.assets.get("music/tank_shoot.ogg", Music.class).isPlaying()){
            screen.game.assets.get("music/tank_shoot.ogg", Music.class).play();
            screen.game.assets.get("music/tank_shoot.ogg", Music.class).setVolume(0.1f);
        }

        if(screen.game.soundOn && !runVehiclecAttackSound &&
                screen.game.assets.get("music/tank_shoot.ogg", Music.class).isPlaying())
            screen.game.assets.get("music/tank_shoot.ogg", Music.class).stop();

        if(screen.game.soundOn && runHelicopterSound &&
                !screen.game.assets.get("music/helicopter.ogg", Music.class).isPlaying()){
            screen.game.assets.get("music/helicopter.ogg", Music.class).play();
            screen.game.assets.get("music/helicopter.ogg", Music.class).setVolume(0.2f);
        }
        if(screen.game.soundOn && !runHelicopterSound && screen.game.assets.get("music/helicopter.ogg", Music.class).isPlaying())
            screen.game.assets.get("music/helicopter.ogg", Music.class).stop();

        if(screen.game.soundOn && runEnemyAttackSound && !screen.game.assets.get("music/enemy1_shoot.ogg", Music.class).isPlaying()){
            screen.game.assets.get("music/enemy1_shoot.ogg", Music.class).play();
            screen.game.assets.get("music/enemy1_shoot.ogg", Music.class).setVolume(0.3f);
        }
        if(screen.game.soundOn && !runEnemyAttackSound && screen.game.assets.get("music/enemy1_shoot.ogg", Music.class).isPlaying())
            screen.game.assets.get("music/enemy1_shoot.ogg", Music.class).stop();
    }

    public void draw(){

        background1.draw(screen.batch);
        background2.draw(screen.batch);
        background3.draw(screen.batch);
        background4.draw(screen.batch);

        lava.draw(screen.batch);

        for(Misc misc : miscs)
            misc.draw(screen.batch);

        ground1.draw(screen.batch);
        ground2.draw(screen.batch);
        ground3.draw(screen.batch);
        ground4.draw(screen.batch);


        for(MyPool myPool : myPools)
            myPool.draw(screen.batch);

        for(Bridge bridge :bridges)
            bridge.draw(screen.batch);

        for(Rope rope: ropes)
            rope.draw(screen.batch);

        for(Ladder ladder : ladders)
            ladder.draw(screen.batch);

        for(Tank tank: tanks)
            tank.draw(screen.batch);

        for(Helicopter helicopter : helicopters)
            helicopter.draw(screen.batch);

        for(MountedGun mountedGun : mountedGuns)
            mountedGun.draw(screen.batch);

        for(Enemy1 enemy1: enemy1s)
            enemy1.draw(screen.batch);

        for(Resupply resupply: resupplies)
            resupply.draw(screen.batch);

        for (SandBag sandBag: sandBags)
            sandBag.draw(screen.batch);

        for(Spike spike : spikes)
            spike.draw(screen.batch);

        for(Bullet bullet:bullets)
            bullet.draw(screen.batch);

        for(Bomb bomb: bombs)
            bomb.draw(screen.batch);

        for(Uranium uranium: uraniums)
            uranium.draw(screen.batch);


    }
}
