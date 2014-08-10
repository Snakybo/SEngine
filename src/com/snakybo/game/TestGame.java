package com.snakybo.game;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.components.DirectionalLight;
import com.snakybo.sengine.components.FreeLook;
import com.snakybo.sengine.components.FreeMove;
import com.snakybo.sengine.components.MeshRenderer;
import com.snakybo.sengine.components.PointLight;
import com.snakybo.sengine.components.SpotLight;
import com.snakybo.sengine.core.Game;
import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.rendering.Attenuation;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Window;
import com.snakybo.sengine.resource.Material;
import com.snakybo.sengine.resource.Mesh;
import com.snakybo.sengine.resource.Texture;
import com.snakybo.sengine.utils.Color;
import com.snakybo.sengine.utils.math.Quaternion;
import com.snakybo.sengine.utils.math.Vector3f;

public class TestGame extends Game {
	public void init(Window window) {
		Camera.initPerspectiveCamera(
			(float)Math.toRadians(70.0f),
			(float)Window.getWidth() / (float)Window.getHeight(),
			0.01f, 1000.0f
		);
		
		addChild(
			new GameObject()
				.addComponent(new FreeLook(Window.getCenter()))
				.addComponent(new FreeMove())
				.addComponent(RenderingEngine.getMainCamera())
		);
		
		new Material(
			"bricks",
			new Texture("bricks.png"), 0.5f, 4.0f,
			new Texture("bricks_normal.png"),
			new Texture("bricks_disp.png"), 0.03f, -0.5f
		);
		
		new Material(
			"bricks2",
			new Texture("bricks2.png"), 0.5f, 4.0f,
			new Texture("bricks2_normal.png"),
			new Texture("bricks2_disp.png"), 0.04f, -1.0f
		);
		
		new Material(
			"uv",
			new Texture("monkey_uv.png"), 0.5f, 4.0f
		);
		
		addChild(new GameObject(new Vector3f(2.0f, 4.0f, 2.0f))
					.addComponent(new MeshRenderer(new Mesh("monkey.obj"), new Material("uv"))));
		
		addChild(new GameObject(new Vector3f(0.0f, -1.0f, 0.0f))
					.addComponent(new MeshRenderer(new Mesh("plane.obj"), new Material("bricks2"))));
		
		addLights();
	}
	
	private void addLights() {
		GameObject directionalLight = new GameObject().addComponent(
			new DirectionalLight(
				new Color(1.0f, 1.0f, 1.0f),
				0.1f
			)
		);
		
		GameObject pointLight = new GameObject().addComponent(
			new PointLight(
				new Color(0.0f, 1.0f, 0.5f),
				0.9f,
				new Attenuation(0.0f, 0.0f, 1.0f)
			)
		);
		
		GameObject spotLight = new GameObject().addComponent(
			new SpotLight(
				new Color(1.0f, 1.0f, 0.0f),
				1.0f,
				new Attenuation(0.0f, 0.0f, 0.05f),
				0.7f
			)
		);
		
		directionalLight.getTransform().setRotation(new Quaternion(new Vector3f(1.0f, 0.0f, 0.0f), (float)Math.toRadians(-45.0f)));
		
		spotLight.getTransform().getPosition().set(3.0f, 0.0f, 3.0f);
		spotLight.getTransform().setRotation(new Quaternion(new Vector3f(0.0f, 1.0f, 0.0f), (float)Math.toRadians(90.0f)));
		
		addChild(directionalLight);
		addChild(pointLight);
		addChild(spotLight);
	}
}
