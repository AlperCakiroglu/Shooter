package com.itujoker.mshooter.screen.utility;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.itujoker.mshooter.screen.AbstractScreen;

public class ScreenManager {

    private static ScreenManager instance;
    private Game game;

    private ScreenManager() {
        super();
    }

    public static ScreenManager getInstance() {
        if (instance == null)
            instance = new ScreenManager();
        return instance;
    }

    public void initialize(Game game) {
        this.game = game;
    }

    public void showScreen(ScreenEnum screenEnum, Object... params) {

        Screen currentScreen = game.getScreen();

        if (currentScreen != null){
            currentScreen.dispose();
        }

        AbstractScreen newScreen = screenEnum.getScreen(params);
        newScreen.setGame(game);
        Gdx.input.setCatchBackKey(true);
        newScreen.initialize();
        newScreen.buildStage();
        game.setScreen(newScreen);

    }

}