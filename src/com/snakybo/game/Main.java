package com.snakybo.game;

import com.snakybo.sengine.core.SEngine;

public class Main {
	public static void main(String[] args) {
		SEngine engine = new SEngine(1280, 720, 60, new TestGame());
		
		engine.createWindow("SEngine Test Game");
		engine.start();
	}
}
