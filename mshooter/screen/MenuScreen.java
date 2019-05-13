package com.itujoker.mshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.itujoker.mshooter.screen.utility.ScreenEnum;
import com.itujoker.mshooter.screen.utility.ScreenManager;

public class MenuScreen extends AbstractScreen {

    private Button playBroButton, playSlyButton, creditsButton, scoresButton, quitButton, trophyButton, soundButton, musicButton;
    private boolean isQuit;
    private float canQuit = 0;
    private boolean isAdShown;

    @Override
    public void initialize() {

        if (game.soundOn || game.assets.get("music/background.ogg", Music.class).isPlaying()) {
            game.assets.get("music/walk.ogg", Sound.class).stop();
            game.assets.get("music/player_shoot.ogg", Sound.class).stop();
            game.assets.get("music/helicopter.ogg", Music.class).stop();
            game.assets.get("music/tank_shoot.ogg", Music.class).stop();
            game.assets.get("music/enemy1_shoot.ogg", Music.class).stop();
            game.assets.get("music/background.ogg", Music.class).stop();
        }
    }

    @Override
    public void buildStage() {


        Image background = new Image(new TextureRegion(

                game.assets.get("backgrounds/backs.pack",
                        TextureAtlas.class).findRegion("menuBack")
        ));

        background.setSize(getWidth(), getHeight());
        addActor(background);

        ////////////////Buttons
        Button.ButtonStyle buttonStylePlayBro = new Button.ButtonStyle(
                game.mySkin.getDrawable("bro"),
                game.mySkin.getDrawable("bro_click"),
                null
        );
        playBroButton = new Button(buttonStylePlayBro);
        playBroButton.setBounds(getWidth() / 2 - getHeight()*0.16f, getHeight() * 0.27f, getHeight() *0.15f , getHeight() * 0.15f);

        playBroButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (game.soundOn) {
                    game.assets.get("music/button.ogg", Sound.class).play();
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                /*if (!isAdShown) {
                    game.leaderBoardService.showInterstitialAd();
                    isAdShown = true;
                }else*/
                    ScreenManager.getInstance().showScreen(ScreenEnum.GAME, 1);
            }
        });
        addActor(playBroButton);

        /////////////////////////////////
        Button.ButtonStyle buttonStylePlaySly = new Button.ButtonStyle(
                game.mySkin.getDrawable("sly"),
                game.mySkin.getDrawable("sly_click"),
                null
        );
        playSlyButton = new Button(buttonStylePlaySly);
        playSlyButton.setBounds(getWidth() / 2 + getHeight()*0.01f, getHeight() * 0.27f, getHeight() *0.15f , getHeight() * 0.15f);

        playSlyButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (game.soundOn) {
                    game.assets.get("music/button.ogg", Sound.class).play();
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                /*if (!isAdShown) {
                    game.leaderBoardService.showInterstitialAd();
                    isAdShown = true;
                }else*/
                    ScreenManager.getInstance().showScreen(ScreenEnum.GAME, 2);
            }
        });
        addActor(playSlyButton);


        /////////////////////////////////

        /*Button.ButtonStyle buttonStyleScore = new Button.ButtonStyle(
                game.mySkin.getDrawable("scores"),
                game.mySkin.getDrawable("scores_click"),
                null
        );
        scoresButton = new Button(buttonStyleScore);
        scoresButton.setBounds(getWidth() - getHeight()*0.11f, getHeight() * 0.29f, getHeight() *0.1f , getHeight() * 0.1f);

        scoresButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (game.soundOn) {
                    game.assets.get("music/button.ogg", Sound.class).play();
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //game.leaderBoardService.showScore();
            }
        });
        addActor(scoresButton);*/

        /////////////////////////////////////////
        /*Button.ButtonStyle buttonStyleTrophy = new Button.ButtonStyle(
                game.mySkin.getDrawable("trophy"),
                game.mySkin.getDrawable("trophy_click"),
                null
        );
        trophyButton = new Button(buttonStyleTrophy);
        trophyButton.setBounds(getWidth() - getHeight()*0.11f,  getHeight() * 0.18f, getHeight() *0.1f , getHeight() * 0.1f);

        trophyButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (game.soundOn) {
                    game.assets.get("music/button.ogg", Sound.class).play();
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                //game.leaderBoardService.showAchievement();
            }
        });
        addActor(trophyButton);*/

        //////////////////////////////////////////

        Button.ButtonStyle buttonStyleRate = new Button.ButtonStyle(
                game.mySkin.getDrawable("rate"),
                game.mySkin.getDrawable("rate_click"),
                null
        );
        quitButton = new Button(buttonStyleRate);
        quitButton.setBounds(getWidth() - getHeight()*0.11f, getHeight() * 0.24f, getHeight() *0.1f , getHeight() * 0.1f);

        quitButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (game.soundOn) {
                    game.assets.get("music/button.ogg", Sound.class).play();
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isQuit = true;
                Gdx.app.log("filter", "exit");
                Gdx.app.exit();
            }
        });
        addActor(quitButton);

        //////////////////////////////////////////


        Button.ButtonStyle buttonStyleCredits = new Button.ButtonStyle(
                game.mySkin.getDrawable("credits"),
                game.mySkin.getDrawable("credits_click"),
                null
        );
        creditsButton = new Button(buttonStyleCredits);
        creditsButton.setBounds(getWidth() - getHeight()*0.11f, getHeight() * 0.35f, getHeight() *0.1f , getHeight() * 0.1f/*getHeight()*0.01f, getHeight() * 0.4f, getHeight() *0.1f , getHeight() * 0.1f*/);
        creditsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (game.soundOn) {
                    game.assets.get("music/button.ogg", Sound.class).play();
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //if (canContinue)
                ScreenManager.getInstance().showScreen(ScreenEnum.CREDITS);
                /*else
                    game.playServices.showInterstitialAd();*/


            }
        });

        addActor(creditsButton);

        ///////////////////////////////////

        Button.ButtonStyle buttonStyleSound = new Button.ButtonStyle(
                game.mySkin.getDrawable("sound"),
                game.mySkin.getDrawable("sound_click"),
                game.mySkin.getDrawable("sound_click")
        );
        soundButton = new Button(buttonStyleSound);
        soundButton.setBounds(getHeight()*0.01f, getHeight() * 0.35f, getHeight() *0.1f , getHeight() * 0.1f);
        soundButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (game.soundOn) {
                    game.assets.get("music/button.ogg", Sound.class).play();
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                if (game.soundOn){
                    game.soundOn = false;
                    if (game.assets.get("music/background.ogg", Music.class).isPlaying()) {
                        game.assets.get("music/walk.ogg", Sound.class).stop();
                        game.assets.get("music/player_shoot.ogg", Sound.class).stop();
                        game.assets.get("music/helicopter.ogg", Music.class).stop();
                        game.assets.get("music/tank_shoot.ogg", Music.class).stop();
                        game.assets.get("music/enemy1_shoot.ogg", Music.class).stop();
                        game.assets.get("music/background.ogg", Music.class).stop();
                    }
                }
                else
                    game.soundOn = true;


            }
        });

        addActor(soundButton);

        ////////////////////////////
        Button.ButtonStyle buttonStyleMusic = new Button.ButtonStyle(
                game.mySkin.getDrawable("music"),
                game.mySkin.getDrawable("music_click"),
                game.mySkin.getDrawable("music_click")
        );
        musicButton = new Button(buttonStyleMusic);
        musicButton.setBounds(getHeight()*0.01f, getHeight() * 0.24f, getHeight() *0.1f , getHeight() * 0.1f);
        musicButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (game.soundOn) {
                    game.assets.get("music/button.ogg", Sound.class).play();
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                if (game.musicOn) {
                    game.musicOn = false;
                    game.assets.get("music/bensound-epic.ogg", Music.class).pause();
                } else {
                    game.musicOn = true;
                    game.assets.get("music/bensound-epic.ogg", Music.class).setLooping(true);
                    game.assets.get("music/bensound-epic.ogg", Music.class).play();
                }

            }
        });

        addActor(musicButton);

    }


    @Override
    public void render(float delta) {
        super.render(delta);

        /*progress = MathUtils.lerp(progress, game.assets.getProgress(), .1f);
        if (game.assets.update() && progress >= game.assets.getProgress() - 0.001f && !canContinue) {
            //enable buttons
            canContinue = true;
        }*/

        if (canQuit < 1)////creditsden backle bu da exit oluyordu
            canQuit += delta;

        if (game.soundOn)
            soundButton.setChecked(true);
        else
            soundButton.setChecked(false);

        if (game.musicOn)
            musicButton.setChecked(true);
        else
            musicButton.setChecked(false);

        if (Gdx.input.isKeyPressed(Input.Keys.BACK) && !isQuit && canQuit > 0.5f) {
            isQuit = true;
            Gdx.app.log("filter", "exit");
            Gdx.app.exit();
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        System.out.println("menu disposed");
        Gdx.app.log("filter", "menu disposed");
    }
}

