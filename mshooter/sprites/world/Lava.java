package com.itujoker.mshooter.sprites.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.tools.Main;

public class Lava {


    private GameScreen screen;
    private Animation lavaAnimation;
    private float stateTimer = 0;

    private Array<Sprite> sprites;

    public Lava(GameScreen screen) {
        this.screen = screen;
        initlavaAnimation();

        sprites = new Array<Sprite>();
        for(int i = 0 ; i < 16 ; i++){
            Sprite temp = new Sprite();
            temp.setSize(screen.gamePort.getWorldWidth()/4, Main.tileHeight/2/Main.PPM);
            temp.setPosition((i*screen.gamePort.getWorldWidth()/4) - i*10/Main.PPM ,-10/Main.PPM);
            sprites.add(temp);
        }

    }

    public void update(float dt){
        stateTimer+= dt;

        for (int i = 0 ; i < 16 ; i++){
            sprites.get(i).setRegion((TextureRegion)lavaAnimation.getKeyFrame(stateTimer,true));
            if(sprites.get(i).getX() < screen.gameCam.position.x - 2.5f * screen.gamePort.getWorldWidth())
                sprites.get(i).setPosition(sprites.get(i).getX() +
                        ((16*screen.gamePort.getWorldWidth()/4) - 16*10/Main.PPM) ,-10/Main.PPM);
        }
    }


    public void draw(Batch batch) {
        for(Sprite sprite: sprites)
            sprite.draw(batch);

    }

    private void initlavaAnimation(){


        Array<TextureRegion> frames = new Array();
        for (int i = 0; i <= 15; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("lava/"+(i+1))));
        lavaAnimation = new Animation(0.1f, frames);
        frames.clear();

    }
}

