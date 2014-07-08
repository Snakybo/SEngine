package com.snakybo.game;

import com.snakybo.sengine.core.SEngine;
import com.snakybo.sengine.rendering.Window;
import com.snakybo.sengine.utils.math.Vector3f;

public class Main {
	public static void main(String[] args) {
		SEngine engine = new SEngine(new TestGame());
		Window window = new Window(1280, 720, "SEngine Test Game");
		
		window.setAmbientColor(new Vector3f(1.0f, 1.0f, 1.0f));
		// window.setClearColor(new Vector3f(0.3f, 0.0f, 0.5f));
		window.setAA(Window.MSAA, 16);
		
		window.create();
		engine.start(window, 60.0);
	}
}
