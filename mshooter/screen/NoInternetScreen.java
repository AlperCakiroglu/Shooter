package com.itujoker.mshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class NoInternetScreen extends AbstractScreen{

    private boolean isTextSet;
    private Image exitText;

    private float elapsedtime = 0;
    private static final float FADE_TIME = 1f;

    @Override
    public void buildStage() {

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if(!isTextSet){
            isTextSet = true;
            exitText = new Image(new TextureRegion(

                    game.assets.get("buttons/texts.pack",
                            TextureAtlas.class).findRegion("internet")
            ));

            exitText.setBounds(getWidth()*0.15f,getHeight()/2 - getHeight()/24,getWidth()*0.7f,getHeight()/12);
            exitText.setVisible(false);
            addActor(exitText);

        }else {

            exitText.setVisible(true);
            elapsedtime += delta;
            exitText.setColor(exitText.getColor().r,exitText.getColor().g,exitText.getColor().b,
                    (elapsedtime / FADE_TIME) % 1f);

            if(Gdx.input.isTouched() || Gdx.input.isKeyPressed(Input.Keys.BACK)){
                Gdx.app.exit();
            }

        }



    }

    @Override
    public void dispose() {
        super.dispose();
        Gdx.app.log("filter"," no internet dispose");
    }
}