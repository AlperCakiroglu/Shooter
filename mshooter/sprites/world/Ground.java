package com.itujoker.mshooter.sprites.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.sprites.enemies.Enemy1;
import com.itujoker.mshooter.sprites.enemies.Helicopter;
import com.itujoker.mshooter.sprites.enemies.MountedGun;
import com.itujoker.mshooter.sprites.enemies.Tank;
import com.itujoker.mshooter.sprites.supplies.Resupply;
import com.itujoker.mshooter.sprites.supplies.Uranium;
import com.itujoker.mshooter.tools.Main;

public class Ground extends Sprite {

    private GameScreen screen;
    public Body b2body;
    public static final float groundSize = 1.2f * Main.V_WIDTH / Main.PPM;
    public int enemyNumber = 3;

    public Ground previousGround;
    private int type;

    private Array<Integer> randomGroundHeight;
    private Array<Integer> randomEnemyPlaces;
    private Array<Integer> randomEnemies;



    public Ground(GameScreen screen, Ground previousGround, Vector2 pos, int type) {
        this.screen = screen;
        this.type = type;
        this.previousGround = previousGround;

        randomGroundHeight = new Array<Integer>();
        randomEnemyPlaces = new Array<Integer>();
        randomEnemies = new Array<Integer>();

        for (int i = 1; i <= 10; i++) { //// bir yerde 8 den fazla olamaz dusman
            if (i != 1 && i != 2)
                randomEnemyPlaces.add(i);
            if (i != 10 && i != 9 && i != 8 && i != 1)
                randomGroundHeight.add(i);
            if (i <= enemyNumber)
                randomEnemies.add(i);
        }

        randomGroundHeight.shuffle();

        generateGround(new Vector2(pos.x, (randomGroundHeight.get(0) * Main.V_HEIGHT / 10 - Main.V_HEIGHT / 20) / Main.PPM));

        update();
        //brige and ladder
        if (previousGround != null && b2body.getPosition().y - previousGround.b2body.getPosition().y > 100 / Main.PPM)
            screen.items.ladders.add(new Ladder(screen, previousGround, this));
        if(previousGround != null && b2body.getPosition().x > previousGround.b2body.getPosition().x + groundSize)
            if(screen.items.connectionType == 1){

                screen.items.bridges.add(new Bridge(screen,new Vector2(
                        previousGround.b2body.getPosition().x + groundSize/2
                        ,previousGround.b2body.getPosition().y)));
                screen.items.connectionType = 2 ;
            }
            else if(screen.items.connectionType == 2){

                screen.items.ropes.add(new Rope(screen,new Vector2(
                        previousGround.b2body.getPosition().x + groundSize/2 + Bridge.bridgeLength/2,
                        previousGround.b2body.getPosition().y + Rope.ropeLength
                )));
                screen.items.connectionType = 3;
            }
            else if(screen.items.connectionType == 3){

                screen.items.myPools.add(new MyPool(screen,new Vector2(
                        previousGround.b2body.getPosition().x + groundSize/2 ,
                        previousGround.b2body.getPosition().y - Bridge.bridgeLength/3
                )));
                screen.items.connectionType = 1;
            }

        //////////////////

        if (type  == 3)
            screen.items.resupplies.add(new Resupply(screen,
                    new Vector2(
                            b2body.getPosition().x - groundSize / 2 +
                                    groundSize / 10 * 2 - groundSize / 20
                            , b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM)
                    , 1));
        else if(type == 4)
            screen.items.resupplies.add(new Resupply(screen,
                    new Vector2(
                            b2body.getPosition().x - groundSize / 2 +
                                    groundSize / 10 * 2 - groundSize / 20
                            , b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM)
                    , 2));


        ///////////////////
        screen.items.uraniums.add(new Uranium(screen,new Vector2(0,0),true));

        if(type!=1)
            generateEnemies();
        generateEffectMisc();
        generateMiscs();

        setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                TextureAtlas.class).findRegion("ground")));
        setBounds(b2body.getPosition().x - groundSize / 2, b2body.getPosition().y - 30 / Main.PPM, groundSize, 40 / Main.PPM);
    }

    private void generateGround(Vector2 pos) {

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(pos);
        b2body = screen.getWorld().createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(groundSize / 2, 5 / Main.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = Main.GROUND_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void update() {

        if (screen.gameCam.position.x > b2body.getPosition().x + groundSize) {

            ///move ground forward
            randomGroundHeight.shuffle();
            float newX;
            if(type == 1)
                newX = screen.items.ground4.b2body.getPosition().x + groundSize + Bridge.bridgeLength;
            else if(type == 2)
                newX = screen.items.ground1.b2body.getPosition().x + groundSize;
            else if(type == 3)
                newX = screen.items.ground2.b2body.getPosition().x + groundSize + Bridge.bridgeLength;
            else
                newX = screen.items.ground3.b2body.getPosition().x + groundSize + Bridge.bridgeLength;

            b2body.setTransform(newX
                    , (randomGroundHeight.get(0) * Main.V_HEIGHT / 10 - Main.V_HEIGHT / 20) / Main.PPM
                    , b2body.getAngle());

            setPosition(b2body.getPosition().x - groundSize / 2, b2body.getPosition().y - 30 / Main.PPM);


            /////bridge and ladder

            if( b2body.getPosition().x > previousGround.b2body.getPosition().x + groundSize) {
                if(screen.items.connectionType == 3 &&
                        previousGround.b2body.getPosition().y >= b2body.getPosition().y){//////////pool

                    screen.items.myPools.add(new MyPool(screen,new Vector2(
                            previousGround.b2body.getPosition().x + groundSize/2 ,
                            previousGround.b2body.getPosition().y - Bridge.bridgeLength/3
                    )));
                    screen.items.connectionType = 1;
                }
                else if (screen.items.connectionType == 1 ||
                        (screen.items.connectionType == 3 &&
                                previousGround.b2body.getPosition().y < b2body.getPosition().y)) {///////////bridge

                    screen.items.bridges.add(new Bridge(screen, new Vector2(
                            previousGround.b2body.getPosition().x + groundSize / 2
                            , previousGround.b2body.getPosition().y)));
                    screen.items.connectionType = 2;
                } else if(screen.items.connectionType == 2) {///////////rope

                    screen.items.ropes.add(new Rope(screen, new Vector2(
                            previousGround.b2body.getPosition().x + groundSize / 2 + Bridge.bridgeLength / 2,
                            previousGround.b2body.getPosition().y + Rope.ropeLength
                    )));
                    screen.items.connectionType = 3;
                }

            }

            if (b2body.getPosition().y - previousGround.b2body.getPosition().y > 100 / Main.PPM)
                screen.items.ladders.add(new Ladder(screen, previousGround, this));


            ////////////////////////////////////
            if (previousGround == screen.items.ground4 && enemyNumber < 5) { ///////dusman sayısını arttırma
                enemyNumber++;
                randomEnemies.clear();
                for (int j = 1; j <= enemyNumber; j++)
                    randomEnemies.add(j);

                screen.items.ground4.enemyNumber = enemyNumber;
                screen.items.ground3.enemyNumber = enemyNumber;
                screen.items.ground2.enemyNumber = enemyNumber;
                screen.items.ground4.randomEnemies = randomEnemies;
                screen.items.ground3.randomEnemies = randomEnemies;
                screen.items.ground2.randomEnemies = randomEnemies;


            } else if (previousGround == screen.items.ground3)
                screen.items.resupplies.add(new Resupply(screen,
                        new Vector2(
                                b2body.getPosition().x - groundSize / 2 +
                                        groundSize / 10 * 2 - groundSize / 20
                                , b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM)
                        , 1));
            else if(previousGround == screen.items.ground4)
                screen.items.resupplies.add(new Resupply(screen,
                        new Vector2(
                                b2body.getPosition().x - groundSize / 2 +
                                        groundSize / 10 * 2 - groundSize / 20
                                , b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM)
                        , 2));

            generateEnemies();
            generateEffectMisc();
            generateMiscs();
        }

    }

    private void generateEnemies() {

        randomEnemyPlaces.shuffle();
        randomEnemies.shuffle();
        for (int i = 0; i < randomEnemies.size; i++) {
            if(randomEnemies.get(i) % 5 == 0 && b2body.getPosition().y + 2* Main.tileHeight/Main.PPM < Main.V_HEIGHT/Main.PPM){
                //Helicopter
                screen.items.helicopters.add(new Helicopter(screen,new Vector2(b2body.getPosition().x - groundSize / 2 +
                        groundSize / 10 * randomEnemyPlaces.get(i) - groundSize / 20,
                        b2body.getPosition().y + 1.5f * Main.tileHeight/Main.PPM)));
            }
            else if (randomEnemies.get(i) % 4 == 0) {////////////////////////tank
                screen.items.tanks.add(new Tank(screen, new Vector2(
                        b2body.getPosition().x - groundSize / 2 +
                                groundSize / 10 * randomEnemyPlaces.get(i) - groundSize / 20
                        , b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM)));
            } else if (randomEnemies.get(i) % 3 == 0) {//////////////mounted gun
                screen.items.mountedGuns.add(new MountedGun(screen, new Vector2(
                        b2body.getPosition().x - groundSize / 2 +
                                groundSize / 10 * randomEnemyPlaces.get(i) - groundSize / 20
                        , b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM)));
            } else {//////////////enemy1
                screen.items.enemy1s.add(new Enemy1(screen, new Vector2(
                        b2body.getPosition().x - groundSize / 2 +
                                groundSize / 10 * randomEnemyPlaces.get(i) - groundSize / 20
                        , b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM)));
            }

        }

    }

    public void generateEffectMisc() {//uranium,sandbag,spike ///contact

        screen.items.sandBags.add(new SandBag(screen, new Vector2(b2body.getPosition().x - groundSize / 2 +
                groundSize / 10 * randomEnemyPlaces.get(randomEnemies.size) - groundSize / 20
                , b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM)));
        screen.items.uraniums.add(new Uranium(screen, new Vector2(b2body.getPosition().x - groundSize / 2 +
                groundSize / 10 * randomEnemyPlaces.get(randomEnemies.size + 1) - groundSize / 20
                , b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM), false));
        if ((type == 2 || type == 4) && randomEnemies.size < 6 )
            screen.items.spikes.add(new Spike(screen, new Vector2(b2body.getPosition().x - groundSize / 2 +
                    groundSize / 10 * randomEnemyPlaces.get(randomEnemies.size + 2) - groundSize / 20
                    , b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM)));
    }

    public void generateMiscs() { //lamb,container,fence ///not contact

        /*screen.items.miscs.add(new Misc(screen,
                new Vector2(b2body.getPosition().x - groundSize / 2, b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM), 3));
        */

        for (int i = 1; i <= 4; i++)
            screen.items.miscs.add(new Misc(screen,
                    new Vector2(b2body.getPosition().x + groundSize / 2 - i * groundSize / 4
                            , b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM), 2));


        if(screen.playerType == 1) {
            screen.items.miscs.add(new Misc(screen,
                    new Vector2(b2body.getPosition().x +
                            groundSize / 10   - groundSize / 20,
                            b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM), 4));
            screen.items.miscs.add(new Misc(screen,
                    new Vector2(b2body.getPosition().x -
                            groundSize / 10  - groundSize / 20,
                            b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM), 10));
            screen.items.miscs.add(new Misc(screen,
                    new Vector2(b2body.getPosition().x
                            - groundSize / 20,
                            b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM), 1));
            screen.items.miscs.add(new Misc(screen,
                    new Vector2(b2body.getPosition().x +
                            groundSize / 10 * 2 - groundSize / 20,
                            b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM), 5));
        }

        else {
            screen.items.miscs.add(new Misc(screen,
                    new Vector2(b2body.getPosition().x +
                            groundSize / 10  - groundSize / 20,
                            b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM), 7));
            screen.items.miscs.add(new Misc(screen,
                    new Vector2(b2body.getPosition().x +
                            groundSize / 10  - groundSize / 20,
                            b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM), 8));
            screen.items.miscs.add(new Misc(screen,
                    new Vector2(b2body.getPosition().x
                            - groundSize / 20,
                            b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM), 7));
            screen.items.miscs.add(new Misc(screen,
                    new Vector2(b2body.getPosition().x -
                            groundSize / 10  - groundSize / 20,
                            b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM), 6));
            screen.items.miscs.add(new Misc(screen,
                    new Vector2(b2body.getPosition().x
                            - groundSize / 20,
                            b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM), 9));

            screen.items.miscs.add(new Misc(screen,
                    new Vector2(b2body.getPosition().x +
                            groundSize / 10 * 2 - groundSize / 20,
                            b2body.getPosition().y + Main.tileWidth / 3 / Main.PPM), 6));
        }

    }


    public void setPreviousGround(Ground previousGround) {
        this.previousGround = previousGround;
    }
}
