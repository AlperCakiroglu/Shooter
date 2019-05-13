package com.itujoker.mshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.itujoker.mshooter.sprites.Player;
import com.itujoker.mshooter.tools.Items;
import com.itujoker.mshooter.tools.Main;
import com.itujoker.mshooter.tools.WorldContactListener;
import com.itujoker.mshooter.tools.screenElements.Controller;
import com.itujoker.mshooter.tools.screenElements.EndMenu;
import com.itujoker.mshooter.tools.screenElements.Hud;
import com.itujoker.mshooter.tools.screenElements.PauseMenu;

public class GameScreen extends AbstractScreen {


    public int playerType;

    ////utilities
    public Batch batch;
    public Viewport gamePort;
    public OrthographicCamera gameCam;
    private Stage gameStage;
    public Items items;

    //box2d
    private World world;
    private WorldContactListener worldContactListener;
    //private Box2DDebugRenderer b2dr;

    public Player player;

    //sreen elements
    public Controller controller;
    public Hud hud;
    public PauseMenu pauseMenu;
    public EndMenu endMenu;


    public boolean isPaused;
    public int score;

    //achievements
    public int destroyedHelis = 0, destroyedTanks = 0;
    public boolean unlockOneHeliAc, isOneHeliAcUnlocked;
    public boolean unlockFiveHeliAc, isFiveHeliAcUnlocked;
    public boolean unlockOneTankAc, isOneTankAcUnlocked;
    public boolean unlockFiveTankAc, isFiveTankAcUnlocked;
    public boolean unlockScoreFiveAc, isScoreFiveAcUnlocked;
    public boolean unlockScoreTenAc,isScoreTenAcUnlocked;


    ////dont show add onPause
    public boolean isOnPause;
    public GameScreen(int playerType) {

        this.playerType = playerType;

        isPaused = false;

        batch = new SpriteBatch();
        gameCam = new OrthographicCamera();
        gamePort = new StretchViewport(Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM, gameCam);
        gameStage = new Stage(gamePort);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

    }

    @Override
    public void initialize() {

        super.initialize();

        //if(game.isRewardTaken)
        //    score = 1000;
       // else
            score = 0;
        //game.isRewardTaken = false;

        world = new World(new Vector2(0, -10), true);
        worldContactListener = new WorldContactListener(this);
        world.setContactListener(worldContactListener);
        //b2dr = new Box2DDebugRenderer();

        player = new Player(this, playerType, new Vector2(128 / Main.PPM, Main.V_HEIGHT*0.8f / Main.PPM));
        items = new Items(this);
        items.initialize();
        controller = new Controller(this);


        game.assets.get("music/tank_shoot.ogg", Music.class).setLooping(true);
        game.assets.get("music/helicopter.ogg", Music.class).setLooping(true);
        game.assets.get("music/enemy1_shoot.ogg", Music.class).setLooping(true);
        game.assets.get("music/background.ogg", Music.class).setLooping(true);
        if(game.soundOn)
            game.assets.get("music/background.ogg", Music.class).play();

    }


    @Override
    public void buildStage() {

        hud = new Hud(this);
        pauseMenu = new PauseMenu(this);
        endMenu = new EndMenu(this);


    }



    private void update(float dt) {

        if (player.b2body.getPosition().x > gamePort.getWorldWidth() / 2 &&
                gameCam.position.x < player.b2body.getPosition().x && !player.isDead)
            gameCam.position.x = player.b2body.getPosition().x;

        gameCam.update();

        world.step(1 / 60.f, 6, 2);

        player.jointOperations();

        items.update(dt);

        player.update(dt);

        controller.update(dt);

        hud.update();

        //achievementsControl();
    }

    @Override
    public void render(float delta) {
        //super.render(delta);
        Gdx.gl.glClearColor((float) 44 / 255, (float) 111 / 255, (float) 73 / 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        gameStage.act(delta);
        gameStage.draw();

        if (!isPaused)
            update(delta);

        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();

        items.draw();
        player.draw(batch);

        batch.end();

        //b2dr.render(world, gameCam.combined);

        act();
        draw();

        if (isPaused) {
            pauseMenu.act();
            pauseMenu.draw();
            pauseMenu.update(delta);
        }

        if (player.canQuit){
            endMenu.act();
            endMenu.draw();
            endMenu.update(delta);

        }

        if(Gdx.input.isKeyPressed(Input.Keys.BACK) && !isPaused){
            if (game.soundOn && !player.isDead) {
                game.assets.get("music/explosion.ogg", Sound.class).stop();
                game.assets.get("music/explosion.ogg", Sound.class).play(0.5f);
            }
            pressPauseButton();
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        System.out.println("game disposed");
        batch.dispose();
        //b2dr.dispose();
        gameStage.dispose();
        pauseMenu.dispose();
    }


    public World getWorld() {
        return world;
    }

    private void pressPauseButton(){

        InputEvent event2 = new InputEvent();
        event2.setType(InputEvent.Type.touchUp);
        hud.pauseButton.fire(event2);

    }

    /*public void achievementsControl(){

        if(score>= 5000 && !unlockScoreFiveAc)
            unlockScoreFiveAc = true;
        if(score >= 10000 && !unlockScoreTenAc)
            unlockScoreTenAc = true;

        //////////////
        if(unlockOneHeliAc &&!isOneHeliAcUnlocked){
            isOneHeliAcUnlocked = true;
            Gdx.app.log("filter","One Heli Ac");
            game.leaderBoardService.unlockAchievement(1);
        }
        if(unlockFiveHeliAc &&!isFiveHeliAcUnlocked){
            isFiveHeliAcUnlocked = true;
            Gdx.app.log("filter","Five Heli Ac");
            game.leaderBoardService.unlockAchievement(2);
        }
        if(unlockOneTankAc &&!isOneTankAcUnlocked){
            isOneTankAcUnlocked = true;
            Gdx.app.log("filter","One Tank Ac");
            game.leaderBoardService.unlockAchievement(3);
        }
        if(unlockFiveTankAc &&!isFiveTankAcUnlocked){
            isFiveTankAcUnlocked = true;
            Gdx.app.log("filter","Five Tank Ac");
            game.leaderBoardService.unlockAchievement(4);
        }
        if(unlockScoreFiveAc &&!isScoreFiveAcUnlocked){
            isScoreFiveAcUnlocked = true;
            Gdx.app.log("filter","Score Five Ac");
            game.leaderBoardService.unlockAchievement(5);
        }
        if(unlockScoreTenAc &&!isScoreTenAcUnlocked){
            isScoreTenAcUnlocked = true;
            Gdx.app.log("filter","Score Ten Ac");
            game.leaderBoardService.unlockAchievement(6);
        }


    }*/

    @Override
    public void pause() {
        super.pause();
        isOnPause = true;
        pressPauseButton();
    }

    @Override
    public void resume() {
        super.resume();
        isOnPause = false;
    }
}