package com.snakybo.game;

import com.snakybo.sengine.core.CoreEngine;

public class Main {
	public static void main(String[] args) {
		CoreEngine engine = new CoreEngine(new TestGame());
		engine.createWindow(1280, 720, "SEngine Test Game", 60);
		engine.start();
	}
}
