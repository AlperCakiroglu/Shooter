package com.itujoker.mshooter.tools.screenElements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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

public class DesktopController {

    public GameScreen screen;
    public MyDesktopInput myDesktopInput;

    private float stateTimer = 0;

    private boolean myInputRight, myInputLeft, myInputUp, myInputDown;
    public DesktopController(GameScreen screen) {
        this.screen = screen;
        myDesktopInput = new MyDesktopInput();
        //Gdx.input.setInputProcessor(new MyDesktopInput());

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

                if (myInputRight) {

                    if (screen.player.b2body.getLinearVelocity().x < 2.f)
                        screen.player.b2body.applyLinearImpulse(new Vector2(0.3f, 0), screen.player.b2body.getWorldCenter(), true);

                } else if (myInputLeft) {

                    if (screen.player.b2body.getLinearVelocity().x > -2.f)
                        screen.player.b2body.applyLinearImpulse(new Vector2(-0.3f, 0), screen.player.b2body.getWorldCenter(), true);
                }


            } else if (screen.player.isOnLadder) {//player onLadder

                if (myInputUp)
                    screen.player.b2body.setLinearVelocity(0, 2);
                else if (myInputDown)
                    screen.player.b2body.setLinearVelocity(0, -2);
                else if (screen.player.groundContact) {

                    if (myInputRight) {

                        if (screen.player.b2body.getLinearVelocity().x < 2.f)
                            screen.player.b2body.applyLinearImpulse(new Vector2(0.3f, 0), screen.player.b2body.getWorldCenter(), true);

                    } else if (myInputLeft) {

                        if (screen.player.b2body.getLinearVelocity().x > -2.f)
                            screen.player.b2body.applyLinearImpulse(new Vector2(-0.3f, 0), screen.player.b2body.getWorldCenter(), true);
                    }


                } else
                    screen.player.b2body.setLinearVelocity(0, 0);


            } else if (screen.player.isOnRope) {////player on Rope

                /*if (Gdx.input.isKeyPressed(Input.Keys.W))
                    screen.player.isDestroyJoint = true;*/

                if (myInputRight) {

                    if (screen.player.b2body.getLinearVelocity().x < 1.5f)
                        screen.player.b2body.applyLinearImpulse(new Vector2(0.3f, 0), screen.player.b2body.getWorldCenter(), true);

                } else if (myInputLeft) {

                    if (screen.player.b2body.getLinearVelocity().x > -1.5f)
                        screen.player.b2body.applyLinearImpulse(new Vector2(-0.3f, 0), screen.player.b2body.getWorldCenter(), true);
                }

            } else {////isSwimming

                if (myInputRight) {

                    if (screen.player.b2body.getLinearVelocity().x < 1f)
                        screen.player.b2body.applyLinearImpulse(new Vector2(0.2f, 0), screen.player.b2body.getWorldCenter(), true);

                } else if (myInputLeft) {

                    if (screen.player.b2body.getLinearVelocity().x > -1f)
                        screen.player.b2body.applyLinearImpulse(new Vector2(-0.2f, 0), screen.player.b2body.getWorldCenter(), true);
                }

            }

        }
    }



    private class MyDesktopInput implements InputProcessor {


        @Override
        public boolean keyDown(int keykode) {

            ///basılı tutmalar update tek basimlar burada
            //image.setColor(Color.ORANGE.r, Color.ORANGE.g, Color.ORANGE.b, 0.5f);
            if(!screen.player.isDead) {
                //////////////
                if(keykode == Input.Keys.W)
                    myInputUp = true;
                else if(keykode == Input.Keys.A)
                    myInputLeft = true;
                else if(keykode == Input.Keys.D)
                    myInputRight = true;
                else if(keykode == Input.Keys.S)
                    myInputDown = true;
                /////////////

                if (!screen.player.isOnLadder && !screen.player.isOnRope && !screen.player.isSwimming) {


                    if (/*type == 4*/keykode == Input.Keys.SPACE && !screen.player.isCrouch && screen.player.groundContact) {//////////////zıplamak
                        screen.player.b2body.applyLinearImpulse(new Vector2(0, 4.f), screen.player.b2body.getWorldCenter(), true);
                        if (screen.game.soundOn) {
                            screen.game.assets.get("music/jump.ogg", Sound.class).stop();
                            screen.game.assets.get("music/jump.ogg", Sound.class).play(0.3f);
                        }
                    } else if (/*type == 1*/keykode == Input.Keys.S) {
                        screen.player.isCrouch = true;
                        screen.player.crouchBody(1);
                    } else if (/*type == 5*/keykode == Input.Keys.F) {//fire
                        stateTimer = 0.3f;
                        screen.player.isAttacking = true;

                        if (screen.game.soundOn) {
                            screen.game.assets.get("music/player_shoot.ogg", Sound.class).stop();
                            screen.game.assets.get("music/player_shoot.ogg", Sound.class).play(0.2f);
                            screen.game.assets.get("music/player_shoot.ogg", Sound.class).loop();
                        }

                    } else if (/*type == 6*/keykode == Input.Keys.G) {//bomb
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
                    } else if (/*type == 7*/keykode == Input.Keys.Q) {//rocket
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
                    if (/*type == 4*/keykode == Input.Keys.SPACE) {//////////////zıplamak
                        screen.player.b2body.applyLinearImpulse(new Vector2(0, 3.f), screen.player.b2body.getWorldCenter(), true);
                        screen.player.isDestroyJoint = true;
                    }
                } else if (screen.player.isSwimming) {

                    if (/*type == 4*/keykode == Input.Keys.SPACE) {//////////////zıplamak
                        screen.player.b2body.applyLinearImpulse(new Vector2(0, 0.5f), screen.player.b2body.getWorldCenter(), true);

                    }

                }
            }
            return true;

        }

        @Override
        public boolean keyUp(int keykode) {

            //image.setColor(lastcolor);
            if(!screen.player.isDead) {

                //////////////
                if(keykode == Input.Keys.W)
                    myInputUp = false;
                else if(keykode == Input.Keys.A)
                    myInputLeft = false;
                else if(keykode == Input.Keys.D)
                    myInputRight = false;
                else if(keykode == Input.Keys.S)
                    myInputDown = false;
                /////////////


                if (!screen.player.isOnLadder && !screen.player.isOnRope && !screen.player.isSwimming) {
                    if (/*type == 1*/keykode == Input.Keys.S) {
                        screen.player.isAttacking = false;
                        screen.player.isCrouch = false;
                        screen.player.crouchBody(2);
                    } else if (/*type == 5*/keykode == Input.Keys.F) {
                        screen.player.isAttacking = false;
                        if (screen.game.soundOn)
                            screen.game.assets.get("music/player_shoot.ogg", Sound.class).stop();
                    }
                }
            }

            return true;
        }

        @Override
        public boolean keyTyped(char character) {
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            return false;
        }

    }

    }







