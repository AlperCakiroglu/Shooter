package com.itujoker.mshooter.tools;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.itujoker.mshooter.screen.utility.ScreenEnum;
import com.itujoker.mshooter.screen.utility.ScreenManager;

public class Main extends Game {


	public static final int tileWidth = 128;
	public static final int tileHeight = 128;

	public static final int V_WIDTH = tileWidth * 10;
	public static final int V_HEIGHT = tileHeight * 6;//10

	public static final float PPM = 100;


	public static final short PLAYER_BIT = 1;
	public static final short ENEMY_BIT = 2;
	public static final short BULLET_BIT = 4;
	public static final short DESTROYED_BIT = 8;
	public static final short GROUND_BIT = 16;
	public static final short LADDER_BIT = 32;
	public static final short PLAYER_LADDER_BIT = 64;
	public static final short SUPPLY_BIT = 128;
	public static final short BOMB_BIT = 256;
	public static final short URANIUM_BIT = 512;
	public static final short SANDBAG_BIT = 1024;
	public static final short SPIKE_BIT = 2048;
	public static final short ROPE_BIT = 4096;
	public static final short LEG_BIT = 8192;
	public static final short WATER_BIT = 8192*2;


	public AssetManager assets;
	public Skin mySkin;

	public boolean musicOn, soundOn;

	private boolean isInternetQuit,isInternetQuitPageSet;

	//public boolean isRewardTaken;

	//public LeaderBoardService leaderBoardService;

	public enum DeviceType {
		ANDROID_PHONE,
		ANDROID_TV,
		DESKTOP
	}

	public DeviceType deviceType;

	public Main(DeviceType deviceType){
		this.deviceType = deviceType;
	}


	@Override
	public void create () {

		System.out.println(deviceType);
		mySkin = new Skin();
		assets = new AssetManager();

		ScreenManager.getInstance().initialize(this);
		ScreenManager.getInstance().showScreen(ScreenEnum.LOADING);
	}

	@Override
	public void render() {

		super.render();

		/*if(!leaderBoardService.isNetworkConnected() && !isInternetQuit){
			isInternetQuit = true;
		}else if(isInternetQuit && !isInternetQuitPageSet && assets.isLoaded("buttons/texts.pack", TextureAtlas.class)){
			isInternetQuitPageSet = true;
			ScreenManager.getInstance().showScreen(ScreenEnum.NOINTERNET);
		}*/
	}

	@Override
	public void dispose () {
		super.dispose();
		getScreen().dispose();
		assets.dispose();
		mySkin.dispose();
		System.out.println("main disposed");
		Gdx.app.log("filter","dispose");
	}
}

