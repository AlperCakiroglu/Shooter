package com.itujoker.mshooter;

import android.app.UiModeManager;
import android.content.res.Configuration;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.itujoker.mshooter.tools.Main;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
		if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
			//tvden baglandi
			initialize(new Main(Main.DeviceType.ANDROID_TV), config);
		}else{
			initialize(new Main(Main.DeviceType.ANDROID_PHONE), config);
		}
	}
}
