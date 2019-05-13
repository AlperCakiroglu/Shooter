package com.itujoker.mshooter.screen.utility;

import com.itujoker.mshooter.screen.AbstractScreen;
import com.itujoker.mshooter.screen.CreditsScreen;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.screen.LoadingScreen;
import com.itujoker.mshooter.screen.MenuScreen;
import com.itujoker.mshooter.screen.NoInternetScreen;

public enum ScreenEnum {

    GAME {
        public AbstractScreen getScreen(Object... params){
            return new GameScreen((Integer) params[0]);
        }
    },
    LOADING {
        public AbstractScreen getScreen(Object... params){
            return new LoadingScreen();
        }
    },
    MENU {
        public AbstractScreen getScreen(Object... params){
            return new MenuScreen();
        }
    },
    CREDITS {
        public AbstractScreen getScreen(Object... params){
            return new CreditsScreen();
        }
    },
    NOINTERNET {
        public AbstractScreen getScreen(Object... params){
            return new NoInternetScreen();
        }
    }


    ;

    public abstract AbstractScreen getScreen(Object... params);

}
