package com.csanon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.csanon.App;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1366;
        config.height = 768;
        //config.useGL30 = true;
        config.useHDPI = true;
        config.vSyncEnabled = false;
        new LwjglApplication(new App(), config);
	}
}
