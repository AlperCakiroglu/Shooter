package com.itujoker.mshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.itujoker.mshooter.screen.utility.ScreenEnum;
import com.itujoker.mshooter.screen.utility.ScreenManager;

public class LoadingScreen extends AbstractScreen {

    private float progress;
    private boolean canQuit,isBackgroundSet, isTextSet;
    private ShapeRenderer shapeRenderer;

    private Image tapText;
    private float elapsedtime = 0;
    private static final float FADE_TIME = 1f;

    private boolean isAdShown;
    @Override
    public void initialize() {

        super.initialize();

        shapeRenderer = new ShapeRenderer();


        game.assets.load("buttons/texts.pack", TextureAtlas.class);
        game.assets.load("backgrounds/backs.pack", TextureAtlas.class);

        game.assets.load("music/button.ogg", Sound.class);
        game.assets.load("music/walk.ogg", Sound.class);
        game.assets.load("music/jump.ogg", Sound.class);
        game.assets.load("music/background.ogg", Music.class);
        game.assets.load("music/bullet_hit.ogg", Sound.class);
        game.assets.load("music/bomb_release.ogg", Sound.class);
        game.assets.load("buttons/buttons.pack", TextureAtlas.class);

        game.assets.load("animations/arnie_jungle.pack", TextureAtlas.class);
        game.assets.load("animations/sly.pack", TextureAtlas.class);
        game.assets.load("animations/enemies.pack", TextureAtlas.class);
        game.assets.load("textures/world.pack", TextureAtlas.class);
        game.assets.load("font/myfont.fnt", BitmapFont.class);
        game.assets.load("music/bensound-epic.ogg", Music.class);

        game.assets.load("music/enemy1_shoot.ogg", Music.class);
        game.assets.load("music/player_shoot.ogg", Sound.class);
        game.assets.load("music/explosion.ogg", Sound.class);
        game.assets.load("music/missile.ogg", Sound.class);
        game.assets.load("music/tank_shoot.ogg", Music.class);
        game.assets.load("music/helicopter.ogg", Music.class);
        game.assets.load("music/bubbles.ogg", Sound.class);
        game.assets.load("music/vehicle_hit.ogg", Sound.class);
        game.assets.load("music/enemy_hit.ogg", Sound.class);
        game.assets.load("music/dead.ogg", Sound.class);
        game.assets.load("music/beep.ogg", Sound.class);


    }

    @Override
    public void buildStage() {


    }

    @Override
    public void render(float delta) {
        super.render(delta);

        progress = MathUtils.lerp(progress, game.assets.getProgress(), .1f);
        if (game.assets.update() && progress >= game.assets.getProgress() - 0.001f && !canQuit) {
            //com.itujoker.shooter.screen.utility.ScreenManager.getInstance().showScreen(com.itujoker.shooter.screen.utility.ScreenEnum.GAME,1);
            canQuit = true;

            game.mySkin.addRegions(game.assets.get("buttons/buttons.pack",
                    TextureAtlas.class));
            game.mySkin.addRegions(game.assets.get("textures/world.pack", TextureAtlas.class));
            game.mySkin.add("font", game.assets.get("font/myfont.fnt", BitmapFont.class),BitmapFont.class);
        }

        if(game.assets.isLoaded("backgrounds/backs.pack", TextureAtlas.class)
                && !isBackgroundSet){

            isBackgroundSet = true;
            Image background = new Image(new TextureRegion(

                    game.assets.get("backgrounds/backs.pack",
                            TextureAtlas.class).findRegion("loadingBack")
            ));

            background.setSize(getWidth(),getHeight());
            addActor(background);
        }

        if(game.assets.isLoaded("buttons/texts.pack", TextureAtlas.class)
                && !isTextSet && isBackgroundSet){
            isTextSet = true;
            tapText = new Image(new TextureRegion(

                    game.assets.get("buttons/texts.pack",
                            TextureAtlas.class).findRegion("tap")
            ));

            tapText.setBounds(getWidth()*0.15f,getHeight()/12,getWidth()*0.7f,getHeight()/12);
            tapText.setVisible(false);
            addActor(tapText);

        }


        if(isBackgroundSet && !canQuit){

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.rect(getWidth() / 6, getHeight() / 12
                    , getWidth() * .66f, getHeight() / 15);

            shapeRenderer.setColor((float)166/256,(float)117/256,(float)68/256,0);
            shapeRenderer.rect(getWidth() / 6, getHeight() / 12
                    , progress * getWidth() * .66f, getHeight() / 15);
            shapeRenderer.end();

        }else if(canQuit && isTextSet){

            tapText.setVisible(true);
            elapsedtime += delta;
            tapText.setColor(tapText.getColor().r,tapText.getColor().g,tapText.getColor().b,
                    (elapsedtime / FADE_TIME) % 1f);

            if(Gdx.input.justTouched()){
                /*if(!isAdShown){
                    game.leaderBoardService.showInterstitialAd();
                    isAdShown = true;
                }
                else*/
                    ScreenManager.getInstance().showScreen(ScreenEnum.MENU);
            }
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        shapeRenderer.dispose();
        System.out.println("loading disposed");
        Gdx.app.log("filter","loading disposed");
    }
}
