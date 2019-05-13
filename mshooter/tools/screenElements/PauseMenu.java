package com.itujoker.mshooter.tools.screenElements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.screen.utility.ScreenEnum;
import com.itujoker.mshooter.screen.utility.ScreenManager;

public class PauseMenu extends Stage {

    private GameScreen screen;

    private Button resumeButton, restartButton, quitButton, soundButton, musicButton;

    private Sprite pauseBackground;

    private float  imageSize ;

    private float stateTimer = 0;
    private boolean canQuit = false, isButtonShowed;
    public boolean isRun;
    private Animation pauseAnimation;

    public PauseMenu(GameScreen screen) {
        this.screen = screen;
        setViewport(screen.getViewport());

        imageSize = screen.getHeight() / 8;
        initAnimation();

        pauseBackground = new Sprite();
        pauseBackground.setBounds(screen.getWidth()/2 - 2.5f*imageSize,screen.getHeight()/2 - 2.5f*imageSize,imageSize*5,imageSize*5);
        pauseBackground.setRegion((TextureRegion) pauseAnimation.getKeyFrame(0));

        createMenu();

    }

    public void update(float dt) {

        if ((stateTimer < 0.5f || canQuit) && isRun) {
            stateTimer += dt;
            pauseBackground.setRegion((TextureRegion) pauseAnimation.getKeyFrame(stateTimer));

            if(pauseAnimation.isAnimationFinished(stateTimer)){
                screen.isPaused = false;
                stateTimer = 0;
                isRun = false;
                canQuit = false;
                isButtonShowed = false;

            }
        }
        else if(stateTimer>= 0.5f && !isButtonShowed && isRun){
            screen.pauseMenu.setVisible(true);
            Gdx.input.setInputProcessor(screen.pauseMenu);
            isButtonShowed = true;
        }

        if(screen.game.musicOn)
            musicButton.setChecked(true);
        else
            musicButton.setChecked(false);

        if(screen.game.soundOn)
            soundButton.setChecked(true);
        else
            soundButton.setChecked(false);
    }

    private void createMenu(){

        Button.ButtonStyle buttonStyleresumeButton = new Button.ButtonStyle(
                screen.game.mySkin.getDrawable("hud/playbut"),
                screen.game.mySkin.getDrawable("hud/playhitbut"),
                null
        );
        resumeButton = new Button(buttonStyleresumeButton);
        resumeButton.setBounds( getWidth()/2 - 4*imageSize/2,getHeight()/2 - imageSize/2 ,imageSize,imageSize);
        resumeButton.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                /*screen.isPaused = false;
                screen.controller.setVisible(true);
                screen.hud.setVisible(true);
                screen.pauseMenu.setVisible(false);
                Gdx.input.setInputProcessor(screen);
                stateTimer = 0;*/
                canQuit = true;

                screen.controller.setVisible(true);
                screen.hud.setVisible(true);
                screen.pauseMenu.setVisible(false);
                Gdx.input.setInputProcessor(screen);
            }
        });

        addActor(resumeButton);

        /////////////////////////////

        Button.ButtonStyle buttonStylerestartButton = new Button.ButtonStyle(
                screen.game.mySkin.getDrawable("hud/restartbut"),
                screen.game.mySkin.getDrawable("hud/restarthitbut"),
                null
        );
        restartButton = new Button(buttonStylerestartButton);
        restartButton.setBounds( getWidth()/2 - imageSize/2,getHeight()/2 - imageSize/2 ,imageSize,imageSize);
        restartButton.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                ScreenManager.getInstance().showScreen(ScreenEnum.GAME, screen.playerType);

            }
        });

        addActor(restartButton);


        /////////////////////////////////

        Button.ButtonStyle buttonStylequitButton = new Button.ButtonStyle(
                screen.game.mySkin.getDrawable("hud/quitbut"),
                screen.game.mySkin.getDrawable("hud/quithitbut"),
                null
        );
        quitButton = new Button(buttonStylequitButton);
        quitButton.setBounds( getWidth()/2 + imageSize,getHeight()/2 - imageSize/2 ,imageSize,imageSize);
        quitButton.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                if(screen.game.soundOn)
                    screen.game.assets.get("music/background.ogg", Music.class).stop();
                ScreenManager.getInstance().showScreen(ScreenEnum.MENU);

            }
        });

        addActor(quitButton);


        ////////////////////////

        Button.ButtonStyle buttonStylesoundButton = new Button.ButtonStyle(
                screen.game.mySkin.getDrawable("hud/soundOffbut"),
                screen.game.mySkin.getDrawable("hud/soundOnbut"),
                screen.game.mySkin.getDrawable("hud/soundOnbut")
        );
        soundButton = new Button(buttonStylesoundButton);
        soundButton.setBounds( getWidth()/2 - imageSize/2,getHeight()/2 - 2*imageSize ,imageSize,imageSize);
        soundButton.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                if(screen.game.soundOn) {
                    screen.game.soundOn = false;
                    screen.game.assets.get("music/background.ogg", Music.class).stop();
                }else{
                    screen.game.soundOn = true;
                    screen.game.assets.get("music/background.ogg", Music.class).play();

                }

            }
        });

        addActor(soundButton);


        /////////////////////

        Button.ButtonStyle buttonStylemusicButton = new Button.ButtonStyle(
                screen.game.mySkin.getDrawable("hud/musicOffBut"),
                screen.game.mySkin.getDrawable("hud/musicOnbut"),
                screen.game.mySkin.getDrawable("hud/musicOnbut")
        );
        musicButton = new Button(buttonStylemusicButton);
        musicButton.setBounds( getWidth()/2 - imageSize/2,getHeight()/2 +imageSize ,imageSize,imageSize);
        musicButton.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                if(screen.game.musicOn) {
                    screen.game.musicOn = false;
                    screen.game.assets.get("music/bensound-epic.ogg", Music.class).pause();
                }else{
                    screen.game.musicOn = true;
                    screen.game.assets.get("music/bensound-epic.ogg", Music.class).setLooping(true);
                    screen.game.assets.get("music/bensound-epic.ogg", Music.class).play();

                }


            }
        });

        addActor(musicButton);


        setVisible(false);

    }

    public void setVisible(boolean visible){

        resumeButton.setVisible(visible);
        restartButton.setVisible(visible);
        quitButton.setVisible(visible);
        soundButton.setVisible(visible);
        musicButton.setVisible(visible);
    }

    private void initAnimation(){

        Array<TextureRegion> frames = new Array();
        for (int i = 0; i <= 11; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("green_bomb/"+(i+1))));
        pauseAnimation = new Animation(0.1f, frames);
        frames.clear();

    }

    @Override
    public void draw() {
        if(screen.isPaused){
            getBatch().begin();
            pauseBackground.draw(getBatch());
            getBatch().end();
        }
        super.draw();
    }
}
