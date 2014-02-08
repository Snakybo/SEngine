package com.snakybo.game;

import com.snakybo.engine.core.Engine;

/** @author Kevin Krol
 *  @since Jan 30, 2014 */
public class Main {
	public static void main(String[] args) {
		Engine engine = new Engine(new TestGame());
		
		engine.createWindow(1280, 720, "3D Game Engine", 60);
		engine.start();
	}
}
