package com.itujoker.mshooter.tools.screenElements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.itujoker.mshooter.screen.GameScreen;

public class Hud {

    private Image statusbar, healthbar, rocketbar, bombbar;
    private Image scoreLamb;
    public Button pauseButton;
    private Label score;


    private float imageSize;
    private GameScreen screen;

    private float statusbarWidth, statusbarHeight;

    public Hud(GameScreen screen) {
        this.screen = screen;
        imageSize = screen.getWidth() / 8;


        createHud();
    }


    private void createHud() {

        statusbarWidth = 3 * imageSize;
        statusbarHeight = imageSize;
        statusbar = new Image(new TextureRegion(screen.game.assets.get("textures/world.pack",
                TextureAtlas.class).findRegion("hud/statusbar")));
        statusbar.setBounds(0, screen.getHeight() - statusbarHeight, statusbarWidth, statusbarHeight);
        screen.addActor(statusbar);

        healthbar = new Image(new TextureRegion(screen.game.assets.get("textures/world.pack",
                TextureAtlas.class).findRegion("hud/healthbar")));
        healthbar.setBounds(statusbarWidth * 0.38f, screen.getHeight() - statusbarHeight * 0.36f, statusbarWidth * 0.53f, statusbarHeight * 0.1f);
        screen.addActor(healthbar);

        bombbar = new Image(new TextureRegion(screen.game.assets.get("textures/world.pack",
                TextureAtlas.class).findRegion("hud/bombbar")));
        bombbar.setBounds(statusbarWidth * 0.47f, screen.getHeight() - statusbarHeight * 0.6f, statusbarWidth * 0.41f, statusbarHeight * 0.07f);
        screen.addActor(bombbar);

        rocketbar = new Image(new TextureRegion(screen.game.assets.get("textures/world.pack",
                TextureAtlas.class).findRegion("hud/rocketbar")));
        rocketbar.setBounds(statusbarWidth * 0.34f, screen.getHeight() - statusbarHeight * 0.82f, statusbarWidth * 0.41f, statusbarHeight * 0.07f);
        screen.addActor(rocketbar);


        Button.ButtonStyle buttonStylePauseButton = new Button.ButtonStyle(
                screen.game.mySkin.getDrawable("hud/pausebut"),
                screen.game.mySkin.getDrawable("hud/pausehitbut"),
                null
        );
        pauseButton = new Button(buttonStylePauseButton);
        pauseButton.setBounds(screen.getWidth() - 0.75f * imageSize, screen.getHeight() - 0.75f * imageSize, imageSize / 2, imageSize / 2);
        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (screen.game.soundOn && !screen.player.isDead) {
                    screen.game.assets.get("music/explosion.ogg", Sound.class).stop();
                    screen.game.assets.get("music/explosion.ogg", Sound.class).play(0.5f);
                }

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!screen.player.isDead) {
                    screen.isPaused = true;
                    screen.pauseMenu.isRun = true;

                    if (screen.game.soundOn) {
                        screen.game.assets.get("music/walk.ogg", Sound.class).stop();
                        screen.game.assets.get("music/player_shoot.ogg", Sound.class).stop();
                        screen.game.assets.get("music/helicopter.ogg", Music.class).stop();
                        screen.game.assets.get("music/tank_shoot.ogg", Music.class).stop();
                        screen.game.assets.get("music/enemy1_shoot.ogg", Music.class).stop();
                    }
                    screen.controller.setVisible(false);
                    screen.hud.setVisible(false);
                    /*if(!screen.isOnPause)
                        screen.game.playServices.showInterstitialAd();*/
                }
            }
        });
        screen.addActor(pauseButton);


        scoreLamb = new Image(new TextureRegion(screen.game.assets.get("textures/world.pack",
                TextureAtlas.class).findRegion("hud/scorelight")));
        scoreLamb.setBounds(screen.getWidth() / 2 - imageSize / 2, screen.getHeight() - imageSize, imageSize, imageSize);
        screen.addActor(scoreLamb);

        Label.LabelStyle labelStyle = new Label.LabelStyle(screen.game.mySkin.getFont("font"), Color.YELLOW);
        score = new Label(Integer.toString(screen.score), labelStyle);
        score.setSize(screen.getWidth() / 10, screen.getHeight() / 10);
        score.setAlignment(Align.center);
        score.setFontScale(arrangeTextSize());
        score.setPosition(screen.getWidth() / 2 - score.getWidth() / 2, screen.getHeight() - 0.85f * imageSize);
        screen.addActor(score);

    }


    public void update() {

        rocketbar.setSize(statusbarWidth * 0.43f * ((float) screen.player.rocketNumber / 9), rocketbar.getHeight());
        bombbar.setSize(statusbarWidth * 0.43f * ((float) screen.player.bombnumber / 9), bombbar.getHeight());
        healthbar.setSize(statusbarWidth * 0.53f
                        * ((float) (screen.player.bodyhealth - screen.player.takenBullet) / screen.player.bodyhealth)
                , healthbar.getHeight());

        score.setText(Integer.toString(screen.score));
    }

    public void setVisible(boolean visible) {

        statusbar.setVisible(visible);
        healthbar.setVisible(visible);
        bombbar.setVisible(visible);
        rocketbar.setVisible(visible);
        pauseButton.setVisible(visible);
        score.setVisible(visible);

    }

    private float arrangeTextSize() {

        return (0.2f * Gdx.graphics.getWidth()) / 640;

    }
}
