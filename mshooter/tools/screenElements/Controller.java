package com.itujoker.mshooter.tools.screenElements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.sprites.supplies.Bomb;
import com.itujoker.mshooter.sprites.supplies.Bullet;
import com.itujoker.mshooter.tools.Main;

public class Controller {

    public GameScreen screen;

    private Vector2 picSize;
    private Image down, left, right, up, fire, bomb, rocket;
    private float stateTimer = 0;

    public MyInput myInputDown, myInputRight, myInputLeft, myInputUp, myInputFire;

    public Controller(GameScreen screen) {
        this.screen = screen;

        picSize = new Vector2(screen.getHeight() / 8, screen.getHeight() / 8);

        Color myColor = new Color(Color.PURPLE.r, Color.PURPLE.g, Color.PURPLE.b, 0.5f);
        down = new Image(screen.game.mySkin.getDrawable("controller/down"));
        down.setBounds(0
                , 0
                , picSize.x, picSize.y);
        down.setColor(myColor);
        myInputDown = new MyInput(down, myColor, 1);
        down.addListener(myInputDown);

        up = new Image(screen.game.mySkin.getDrawable("controller/up"));
        up.setBounds(0
                , picSize.y
                , picSize.x, picSize.y);
        up.setColor(myColor);
        myInputUp = new MyInput(up, myColor, 4);
        up.addListener(myInputUp);

        fire = new Image(screen.game.mySkin.getDrawable("controller/fire"));
        fire.setBounds(screen.getWidth() / 2 - 1.5f * picSize.x
                , 0
                , picSize.x, picSize.y);
        fire.setColor(myColor);
        myInputFire = new MyInput(fire, myColor, 5);
        fire.addListener(myInputFire);


        /////////////////////////////

        left = new Image(screen.game.mySkin.getDrawable("controller/left"));
        left.setBounds(screen.getWidth() - 2 * picSize.x
                , picSize.y / 2
                , picSize.x, picSize.y);
        left.setColor(myColor);
        myInputLeft = new MyInput(left, myColor, 3);
        left.addListener(myInputLeft);

        right = new Image(screen.game.mySkin.getDrawable("controller/right"));
        right.setBounds(screen.getWidth() - picSize.x
                , picSize.y / 2
                , picSize.x, picSize.y);
        right.setColor(myColor);
        myInputRight = new MyInput(right, myColor, 2);
        right.addListener(myInputRight);


        bomb = new Image(screen.game.mySkin.getDrawable("controller/bomb"));
        bomb.setBounds(screen.getWidth() / 2 - 0.5f * picSize.x
                , 0
                , picSize.x, picSize.y);
        bomb.setColor(myColor);
        bomb.addListener(new MyInput(bomb, myColor, 6));


        rocket = new Image(screen.game.mySkin.getDrawable("controller/rocket"));
        rocket.setBounds(screen.getWidth() / 2 + 0.5f * picSize.x
                , 0
                , picSize.x, picSize.y);
        rocket.setColor(myColor);
        rocket.addListener(new MyInput(rocket, myColor, 7));


        screen.addActor(rocket);
        screen.addActor(bomb);
        screen.addActor(fire);
        screen.addActor(up);
        screen.addActor(right);
        screen.addActor(left);
        screen.addActor(down);
    }


    public void update(float dt) {

        /////basılı tutmayı burada ayarla //// playerda crouch bitimi olarak butondan eli kaldırmayı ayarla // saldırı ce bomba tusu saga al

        stateTimer += dt;

        if(!screen.player.isDead) {
            if (!screen.player.isOnLadder && !screen.player.isOnRope && !screen.player.isSwimming) {

                if (screen.player.isAttacking && stateTimer > 0.2f) {

                    if (screen.player.runningRight)
                        screen.items.bullets.add(new Bullet(screen, new Vector2(screen.player.b2body.getPosition().x + Main.tileWidth / 2 / Main.PPM + Main.tileWidth / 30 / Main.PPM,
                                screen.player.b2body.getPosition().y), true, true, false, false));
                    else

                        screen.items.bullets.add(new Bullet(screen, new Vector2(screen.player.b2body.getPosition().x - Main.tileWidth / 2 / Main.PPM - Main.tileWidth / 30 / Main.PPM,
                                screen.player.b2body.getPosition().y), false, true, false, false));

                    stateTimer = 0;
                }

                if (myInputRight.activeTouch) {

                    if (screen.player.b2body.getLinearVelocity().x < 2.f)
                        screen.player.b2body.applyLinearImpulse(new Vector2(0.3f, 0), screen.player.b2body.getWorldCenter(), true);

                } else if (myInputLeft.activeTouch) {

                    if (screen.player.b2body.getLinearVelocity().x > -2.f)
                        screen.player.b2body.applyLinearImpulse(new Vector2(-0.3f, 0), screen.player.b2body.getWorldCenter(), true);
                }


            } else if (screen.player.isOnLadder) {//player onLadder

                if (Gdx.input.isKeyPressed(Input.Keys.W) || myInputUp.activeTouch)
                    screen.player.b2body.setLinearVelocity(0, 2);
                else if (Gdx.input.isKeyPressed(Input.Keys.S) || myInputDown.activeTouch)
                    screen.player.b2body.setLinearVelocity(0, -2);
                else if (screen.player.groundContact) {

                    if (myInputRight.activeTouch) {

                        if (screen.player.b2body.getLinearVelocity().x < 2.f)
                            screen.player.b2body.applyLinearImpulse(new Vector2(0.3f, 0), screen.player.b2body.getWorldCenter(), true);

                    } else if (myInputLeft.activeTouch) {

                        if (screen.player.b2body.getLinearVelocity().x > -2.f)
                            screen.player.b2body.applyLinearImpulse(new Vector2(-0.3f, 0), screen.player.b2body.getWorldCenter(), true);
                    }


                } else
                    screen.player.b2body.setLinearVelocity(0, 0);


            } else if (screen.player.isOnRope) {////player on Rope

                if (Gdx.input.isKeyPressed(Input.Keys.W))
                    screen.player.isDestroyJoint = true;

                if (myInputRight.activeTouch) {

                    if (screen.player.b2body.getLinearVelocity().x < 1.5f)
                        screen.player.b2body.applyLinearImpulse(new Vector2(0.3f, 0), screen.player.b2body.getWorldCenter(), true);

                } else if (myInputLeft.activeTouch) {

                    if (screen.player.b2body.getLinearVelocity().x > -1.5f)
                        screen.player.b2body.applyLinearImpulse(new Vector2(-0.3f, 0), screen.player.b2body.getWorldCenter(), true);
                }

            } else {////isSwimming

                if (myInputRight.activeTouch) {

                    if (screen.player.b2body.getLinearVelocity().x < 1f)
                        screen.player.b2body.applyLinearImpulse(new Vector2(0.2f, 0), screen.player.b2body.getWorldCenter(), true);

                } else if (myInputLeft.activeTouch) {

                    if (screen.player.b2body.getLinearVelocity().x > -1f)
                        screen.player.b2body.applyLinearImpulse(new Vector2(-0.2f, 0), screen.player.b2body.getWorldCenter(), true);
                }

            }

        }
    }



    private class MyInput extends InputListener {
        public boolean activeTouch;
        private Image image;
        private Color lastcolor;
        private int type;

        public MyInput(Image image, Color lastcolor, int type) {

            this.image = image;
            this.lastcolor = lastcolor;
            this.type = type;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

            ///basılı tutmalar update tek basimlar burada
            image.setColor(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, 0.5f);
            if(!screen.player.isDead) {
                activeTouch = true;


                if (!screen.player.isOnLadder && !screen.player.isOnRope && !screen.player.isSwimming) {


                    if (type == 4 && !screen.player.isCrouch && screen.player.groundContact) {//////////////zıplamak
                        screen.player.b2body.applyLinearImpulse(new Vector2(0, 4.f), screen.player.b2body.getWorldCenter(), true);
                        if (screen.player.isOnRope)
                            screen.player.isDestroyJoint = true;
                        if (screen.game.soundOn) {
                            screen.game.assets.get("music/jump.ogg", Sound.class).stop();
                            screen.game.assets.get("music/jump.ogg", Sound.class).play(0.3f);
                        }
                    } else if (type == 1) {
                        screen.player.isCrouch = true;
                        screen.player.crouchBody(1);
                    } else if (type == 5) {//fire
                        stateTimer = 0.3f;
                        screen.player.isAttacking = true;

                        if (screen.game.soundOn) {
                            screen.game.assets.get("music/player_shoot.ogg", Sound.class).stop();
                            screen.game.assets.get("music/player_shoot.ogg", Sound.class).play(0.2f);
                            screen.game.assets.get("music/player_shoot.ogg", Sound.class).loop();
                        }

                    } else if (type == 6) {//bomb
                        if (screen.player.bombnumber != 0) {
                            screen.player.isToss = true;
                            screen.items.bombs.add(new Bomb(screen, new Vector2(screen.player.b2body.getPosition().x - Main.tileWidth / 2 / Main.PPM - Main.tileWidth / 30 / Main.PPM,
                                    screen.player.b2body.getPosition().y)));


                            if (screen.game.soundOn) {
                                screen.game.assets.get("music/bomb_release.ogg", Sound.class).stop();
                                screen.game.assets.get("music/bomb_release.ogg", Sound.class).play(0.3f);
                            }
                            /////bombayı 1 azalt burada //////////////////////////////////////////////////
                            screen.player.bombnumber--;
                        }
                    } else if (type == 7) {//rocket
                        if (screen.player.rocketNumber != 0) {
                            screen.player.isRocket = true;
                            if (screen.game.soundOn) {

                                screen.game.assets.get("music/missile.ogg", Sound.class).stop();
                                screen.game.assets.get("music/missile.ogg", Sound.class).play(0.3f);

                            }

                            if (screen.player.runningRight)
                                screen.items.bullets.add(new Bullet(screen, new Vector2(screen.player.b2body.getPosition().x + Main.tileWidth / 2 / Main.PPM + Main.tileWidth / 30 / Main.PPM,
                                        screen.player.b2body.getPosition().y), true, false, true, false));
                            else
                                screen.items.bullets.add(new Bullet(screen, new Vector2(screen.player.b2body.getPosition().x - Main.tileWidth / 2 / Main.PPM + Main.tileWidth / 30 / Main.PPM,
                                        screen.player.b2body.getPosition().y), false, false, true, false));
                            /////rocketi 1 azalt burada //////////////////////////////////////////////////
                            screen.player.rocketNumber--;
                        }
                    }
                } else if (screen.player.isOnRope) {
                    if (type == 4) {//////////////zıplamak
                        screen.player.b2body.applyLinearImpulse(new Vector2(0, 3.f), screen.player.b2body.getWorldCenter(), true);
                        screen.player.isDestroyJoint = true;
                    }
                } else if (screen.player.isSwimming) {

                    if (type == 4) {//////////////zıplamak
                        screen.player.b2body.applyLinearImpulse(new Vector2(0, 0.5f), screen.player.b2body.getWorldCenter(), true);

                    }

                }
            }
            return true;

        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            image.setColor(lastcolor);
            if(!screen.player.isDead) {
                activeTouch = false;
                if (!screen.player.isOnLadder && !screen.player.isOnRope && !screen.player.isSwimming) {
                    if (type == 1) {
                        screen.player.isAttacking = false;
                        screen.player.isCrouch = false;
                        screen.player.crouchBody(2);
                    } else if (type == 5) {
                        screen.player.isAttacking = false;
                        if (screen.game.soundOn)
                            screen.game.assets.get("music/player_shoot.ogg", Sound.class).stop();
                    }
                }
            }
        }

    }

    public void setVisible(boolean visible){
        down.setVisible(visible); left.setVisible(visible);
        right.setVisible(visible); up.setVisible(visible); fire.setVisible(visible);
        bomb.setVisible(visible); rocket.setVisible(visible);
    }


}

