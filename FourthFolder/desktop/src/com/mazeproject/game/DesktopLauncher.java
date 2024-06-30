package com.mazeproject.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setWindowedMode(com.mazeproject.game.MazeProject.WIDTH, com.mazeproject.game.MazeProject.HEIGHT);
		config.setResizable(false);
		config.setTitle("mazeproject");
		new Lwjgl3Application(new com.mazeproject.game.MazeProject(), config);
	}
}
