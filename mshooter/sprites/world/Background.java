package com.itujoker.mshooter.sprites.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.tools.Main;

public class Background extends Sprite {

    GameScreen screen;

    public Background(GameScreen screen, int name) {

        this.screen = screen;

        if(screen.playerType == 2)
            setRegion(new TextureRegion(screen.getGame().assets.get("backgrounds/backs.pack",
                    TextureAtlas.class).findRegion(Integer.toString(name))));
        else
            setRegion(new TextureRegion(screen.getGame().assets.get("backgrounds/backs.pack",
                    TextureAtlas.class).findRegion(Integer.toString(name+4))));
        setBounds((name-1)* Main.V_WIDTH / Main.PPM /*- (name-1)* Main.V_WIDTH/30/Main.PPM*/,0
                ,Main.V_WIDTH / Main.PPM, Main.V_HEIGHT / Main.PPM);

    }

    public void update(){

        if(screen.gameCam.position.x > getX() + 3*Main.V_WIDTH / Main.PPM)
            setPosition(getX()+ 4* Main.V_WIDTH / Main.PPM /*-  4 * Main.V_WIDTH/30/Main.PPM*/,getY());
    }
}
