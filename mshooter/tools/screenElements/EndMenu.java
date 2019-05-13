package com.itujoker.mshooter.tools.screenElements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.screen.utility.ScreenEnum;
import com.itujoker.mshooter.screen.utility.ScreenManager;

public class EndMenu extends Stage {

    private GameScreen screen;
    private Animation endAnimation;

    private float imageSize;

    private Sprite endBackground;
    private Label score;

    private float stateTimer = 0,continueTimer = 0, quitTimer = 0;
    private int showedScore = 0;

    private boolean isSoundRun,isScoreSoundRun, canContinue, canQuit, isQuit;
    //private boolean isRewardedVideoShowed;

    public EndMenu(GameScreen screen) {
        this.screen = screen;

        setViewport(screen.getViewport());

        imageSize = screen.getHeight() / 8;
        initAnimation();

        endBackground = new Sprite();
        endBackground.setBounds(screen.getWidth() / 2 - 2.5f * imageSize, screen.getHeight() / 2 - 2.5f * imageSize, imageSize * 5, imageSize * 5);
        endBackground.setRegion((TextureRegion) endAnimation.getKeyFrame(0));

        Label.LabelStyle labelStyle = new Label.LabelStyle(screen.game.mySkin.getFont("font"), Color.PURPLE);
        score = new Label(Integer.toString(screen.score), labelStyle);
        score.setSize(screen.getWidth() / 10, screen.getHeight() / 10);
        score.setAlignment(Align.center);
        score.setFontScale(arrangeTextSize());
        score.setPosition(screen.getWidth() / 2 - score.getWidth() / 2, screen.getHeight()/2 - score.getHeight() / 2);
        addActor(score);
        score.setVisible(false);



    }

    public void update(float dt) {

        if (screen.game.soundOn && !isSoundRun) {
            isSoundRun = true;

            screen.game.assets.get("music/walk.ogg", Sound.class).stop();
            screen.game.assets.get("music/player_shoot.ogg", Sound.class).stop();
            screen.game.assets.get("music/helicopter.ogg", Music.class).stop();
            screen.game.assets.get("music/tank_shoot.ogg", Music.class).stop();
            screen.game.assets.get("music/enemy1_shoot.ogg", Music.class).stop();

            screen.game.assets.get("music/explosion.ogg", Sound.class).stop();
            screen.game.assets.get("music/explosion.ogg", Sound.class).play(0.5f);

            screen.game.assets.get("music/beep.ogg", Sound.class).loop();
        }

        if ((stateTimer < 0.5f || canContinue) && !canQuit) {
            stateTimer += dt;
            endBackground.setRegion((TextureRegion) endAnimation.getKeyFrame(stateTimer));
            if(endAnimation.isAnimationFinished(stateTimer)){
                //screen.game.leaderBoardService.submitScore(screen.score);
                //screen.game.leaderBoardService.rewardedVideoNoPass();
                canQuit = true;
            }
        }else if(!canQuit) {
            score.setVisible(true);
            if(!isScoreSoundRun && screen.game.soundOn){
                screen.game.assets.get("music/beep.ogg", Sound.class).play();
                isScoreSoundRun = true;
            }
            if(showedScore < screen.score && screen.score < 2000)
                showedScore += 10;
            else if(showedScore < screen.score && screen.score >= 2000)
                showedScore += 50;
            else showedScore = screen.score;
            score.setText(Integer.toString(showedScore));

            if(showedScore == screen.score){
                continueTimer+= dt;
                if(screen.game.soundOn)
                    screen.game.assets.get("music/beep.ogg", Sound.class).stop();
            }
        }else{/////canQuit

            quitTimer += dt;
            //ScreenManager.getInstance().showScreen(ScreenEnum.GAME, screen.playerType);

            /*if(!isRewardedVideoShowed && screen.game.leaderBoardService.isRewardedVideoLoaded()){
                screen.game.leaderBoardService.showRewarderdVideoAd();
                isRewardedVideoShowed = true;
            }*/

            if(/*screen.game.leaderBoardService.isRewardedVideoCanPass() &&*/ !isQuit)
            {
                //video watched
                //screen.game.isRewardTaken = true;
                //screen.game.leaderBoardService.showToast("You are starting with 1000 points");
                //Gdx.app.log("filter","Rewarded video is watched");
                isQuit = true;
                ScreenManager.getInstance().showScreen(ScreenEnum.GAME, screen.playerType);
            }

            /*if((screen.game.leaderBoardService.isRewardedVideoClosed() && !screen.game.leaderBoardService.isRewardedVideoCanPass()) ||
                    quitTimer >= 10.f){
                //closed without watching
                //screen.game.playServices.showInterstitialAd();
                ScreenManager.getInstance().showScreen(ScreenEnum.MENU);
                Gdx.app.log("filter","Rewarded video is closed without watching");

            }*/
        }

        if(continueTimer >1.f){
            canContinue = true;
            score.setVisible(false);
        }


    }

    private void initAnimation() {

        Array<TextureRegion> frames = new Array();
        for (int i = 0; i <= 11; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("green_bomb/" + (i + 1))));
        endAnimation = new Animation(0.1f, frames);
        frames.clear();

    }

    private float arrangeTextSize() {

        return (0.4f * Gdx.graphics.getWidth()) / 640;

    }

    @Override
    public void draw() {
        getBatch().begin();
        endBackground.draw(getBatch());
        getBatch().end();
        super.draw();
    }
}
