package com.snakybo.game;

import com.snakybo.sengine.core.SEngine;
import com.snakybo.sengine.rendering.Window;

public class Main {
	public static void main(String[] args) {
		SEngine engine = new SEngine(new TestGame());
		Window window = new Window(1280, 720, "SEngine Test Game");
		
		//window.setAA(Window.MSAA, 16);
		
		window.create(null);
		engine.start(window, 60.0);
	}
}
