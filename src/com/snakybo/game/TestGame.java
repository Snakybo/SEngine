package com.snakybo.game;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.components.FreeLook;
import com.snakybo.sengine.components.FreeMove;
import com.snakybo.sengine.core.Game;
import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.rendering.Window;
import com.snakybo.sengine.resource.Prefab;

public class TestGame extends Game {
	public void init() {
		Camera camera =
				Camera.initPerspectiveCamera((float)Math.toRadians(70.0f),
						(float)Window.getWidth() / (float)Window.getHeight(), 0.01f, 1000.0f);
		
		addChild(new GameObject(new FreeLook(0.5f), new FreeMove(10.0f), camera));
		
		addChild(Prefab.load("plane"));
	}
}
