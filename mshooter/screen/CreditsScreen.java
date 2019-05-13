package com.itujoker.mshooter.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.itujoker.mshooter.screen.utility.ScreenEnum;
import com.itujoker.mshooter.screen.utility.ScreenManager;

public class CreditsScreen extends AbstractScreen {

    private ScrollPane scroller;
    private float counter = 0;

    private boolean isQuit;

    @Override
    public void buildStage() {

        Image background = new Image(new TextureRegion(

                game.assets.get("backgrounds/backs.pack",
                        TextureAtlas.class).findRegion("credits")
        ));

        background.setSize(getWidth(),getHeight());
        addActor(background);

        Label.LabelStyle labelstyle = new Label.LabelStyle(game.mySkin.getFont("font"), Color.WHITE);
        Label credit1 = new Label("Music: Epic - Bensound.com",labelstyle);credit1.setFontScale(arrangeTextSize());
        Label credit2 = new Label("Sounds : Some Effects - MobileFish.com",labelstyle);credit2.setFontScale(arrangeTextSize());
        Label credit3 = new Label("Sounds : Some Effects - SoundBible.com",labelstyle);credit3.setFontScale(arrangeTextSize());
        Label credit4 = new Label("Characters : Characters - gameArtPartners.com",labelstyle);credit4.setFontScale(arrangeTextSize());
        Label credit5 = new Label("Animations : Characters - gameArtPartners.com",labelstyle);credit5.setFontScale(arrangeTextSize());
        Label credit6 = new Label("Textures : Worlds - gameArtPartners.com",labelstyle);credit6.setFontScale(arrangeTextSize());
        Label credit7 = new Label("Textures : Worlds - LudicArts.com",labelstyle);credit7.setFontScale(arrangeTextSize());
        Label credit8 = new Label("Incscape is used for textures drawing",labelstyle);credit8.setFontScale(arrangeTextSize());
        Label credit9 = new Label("Box2d is used as game engine",labelstyle);credit9.setFontScale(arrangeTextSize());
        Label credit10 = new Label("Powered by libgdx",labelstyle);credit10.setFontScale(arrangeTextSize());
        Label credit11 = new Label("Created by itujoker",labelstyle);credit11.setFontScale(arrangeTextSize());

        Label empty = new Label("",labelstyle);empty.setFontScale(arrangeTextSize()*3);

        Table creditTable = new Table();
        creditTable.add(empty);
        creditTable.row();
        creditTable.add(empty);
        creditTable.row();
        creditTable.add(credit1);
        creditTable.row();
        creditTable.add(credit2);
        creditTable.row();
        creditTable.add(credit3);
        creditTable.row();
        creditTable.add(empty);
        creditTable.row();
        creditTable.add(credit4);
        creditTable.row();
        creditTable.add(credit5);
        creditTable.row();
        creditTable.add(credit6);
        creditTable.row();
        creditTable.add(credit7);
        creditTable.row();
        creditTable.add(empty);
        creditTable.row();
        creditTable.add(credit8);
        creditTable.row();
        creditTable.add(credit9);
        creditTable.row();
        creditTable.add(credit10);
        creditTable.row();
        creditTable.add(empty);
        creditTable.row();
        creditTable.add(empty);
        creditTable.row();
        creditTable.add(credit11);
        creditTable.row();
        creditTable.add(empty);
        creditTable.row();
        creditTable.add(empty);

        scroller = new ScrollPane(creditTable);
        Label header = new Label("Credits",labelstyle);header.setFontScale(arrangeTextSize()*2);

        Table scrollTable = new Table();
        scrollTable.setBounds(0,getHeight()*0.2f,getWidth(),getHeight()*0.60f);
        scrollTable.add(header).height(scrollTable.getHeight()/5).padBottom(scrollTable.getHeight()/10);
        scrollTable.row();
        scrollTable.add(scroller).height(scrollTable.getHeight()*0.7f);
        addActor(scrollTable);

        scroller.setTouchable(Touchable.disabled);


    }


    @Override
    public void render(float delta) {
        super.render(delta);

        counter += 0.001;
        scroller.setScrollPercentY(counter);

        if((Gdx.input.isKeyPressed(Input.Keys.BACK) || Gdx.input.justTouched()) && !isQuit){
            isQuit = true;
            Gdx.app.log("filter","credits quit");
            ScreenManager.getInstance().showScreen(ScreenEnum.MENU);
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        Gdx.app.log("filter","credits disposed");
        System.out.println("credits disposed");
    }

    private float arrangeTextSize() {

        return (0.15f * Gdx.graphics.getWidth()) / 640;

    }
}