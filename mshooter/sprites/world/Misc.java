package com.itujoker.mshooter.sprites.world;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.itujoker.mshooter.screen.GameScreen;
import com.itujoker.mshooter.tools.Main;

public class Misc extends Sprite {

    private GameScreen screen;
    private int type;
    public boolean canRemove;

    private Animation beltAnimation, doorAnimation;
    private float stateTimer = 0;

    public Misc(GameScreen screen, Vector2 pos, int type) {
        this.screen = screen;
        this.type = type;

        initAnimations();

        if(type == 1){/////container
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("misc/container")));
            setBounds(pos.x, pos.y - Main.tileWidth / 3.9f / Main.PPM, Main.tileWidth  / Main.PPM,2*Main.tileWidth  / Main.PPM);
        }
        else if(type == 2){////light
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("misc/light")));
            setBounds(pos.x, pos.y,Main.tileWidth  / Main.PPM,Main.tileWidth  / Main.PPM);
        }
        else if(type == 3){///fence
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("misc/fence")));
            setBounds(pos.x, pos.y - Ground.groundSize/30,Ground.groundSize ,Ground.groundSize/15);
        }

        ////together
        else if(type == 4){////slate
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("misc/slate")));
            setBounds(pos.x, pos.y - Ground.groundSize/30,Ground.groundSize/10 ,Ground.groundSize/10);
        }
        else if(type == 5){////rock
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("misc/rock")));
            setBounds(pos.x, pos.y - Ground.groundSize/30,Ground.groundSize/10 ,Ground.groundSize/10);
        }

        /////together
        else if(type == 6){////windowwall
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("misc/windowwall")));
            setBounds(pos.x, pos.y - Ground.groundSize/30,Ground.groundSize/10 ,Ground.groundSize/8);
        }
        else if(type == 7){////wall
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("misc/wall")));
            setBounds(pos.x, pos.y - Ground.groundSize/30,Ground.groundSize/10 ,Ground.groundSize/8);
        }
        else if(type == 8){/////window
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("misc/window")));
            setBounds(pos.x + Ground.groundSize/45, pos.y ,Ground.groundSize/15 ,Ground.groundSize/15);
        }
        else if(type == 9){///door
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("door/1")));
            setBounds(pos.x, pos.y - Ground.groundSize/30,Ground.groundSize/10 ,Ground.groundSize/8);
        }
        else if(type == 10){////belt
            setRegion(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("belt/1")));
            setBounds(pos.x - Ground.groundSize/10, pos.y - Ground.groundSize/40,2*Ground.groundSize/10 ,Ground.groundSize/12);
        }
    }

    public void update(float dt){

        if(screen.gameCam.position.x > getX() + 1.5f*Ground.groundSize)
            canRemove = true;

        stateTimer +=dt;
        if(type == 9)
            setRegion((TextureRegion)doorAnimation.getKeyFrame(stateTimer,true));

        else if(type == 10)
            setRegion((TextureRegion)beltAnimation.getKeyFrame(stateTimer,true));

    }

    private void initAnimations(){

        Array<TextureRegion> frames = new Array();
        for (int i = 0; i <= 5; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("belt/"+(i+1))));
        beltAnimation = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 0; i <= 7; i++)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("door/"+(i+1))));
        for (int i = 6; i >= 0; i--)
            frames.add(new TextureRegion(screen.getGame().assets.get("textures/world.pack",
                    TextureAtlas.class).findRegion("door/"+(i+1))));

        doorAnimation = new Animation(0.1f, frames);
        frames.clear();

    }
}
