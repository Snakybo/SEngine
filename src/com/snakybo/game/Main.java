package com.snakybo.game;

import com.snakybo.engine.core.SEngine;

/** @author Kevin Krol
 * @since Jan 30, 2014 */
public class Main {
	public static void main(String[] args) {
		SEngine sEngine = new SEngine(new TestGame());
		
		sEngine.createWindow(1280, 720, "3D Game Engine", 60);
		sEngine.start();
	}
}
