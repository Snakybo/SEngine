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
import com.snakybo.sengine.rendering.Window;
import com.snakybo.sengine.resource.Material;
import com.snakybo.sengine.resource.Mesh;
import com.snakybo.sengine.resource.Texture;
import com.snakybo.sengine.resource.loading.IndexedModel;
import com.snakybo.sengine.utils.Color;
import com.snakybo.sengine.utils.math.Quaternion;
import com.snakybo.sengine.utils.math.Vector2f;
import com.snakybo.sengine.utils.math.Vector3f;

public class TestGame extends Game {
	public void init() {
		Camera camera =
				Camera.initPerspectiveCamera((float)Math.toRadians(70.0f),
						(float)Window.getWidth() / (float)Window.getHeight(), 0.01f, 1000.0f);
		
		addChild(new GameObject()
					.addComponent(new FreeLook(0.5f))
					.addComponent(new FreeMove(10.0f))
					.addComponent(camera)
				);
		
		new Material("bricks", new Texture("bricks.png"), 0.0f, 0.0f, new Texture("bricks_normal.png"), new Texture("bricks_disp.png"), 0.03f, -0.5f);
		new Material("bricks2", new Texture("bricks2.png"), 0.0f, 0.0f, new Texture("bricks2_normal.png"), new Texture("bricks2_disp.png"), 0.04f, -1.0f);
		
		//addCustomMesh();
		addMesh();
		addLights();
	}
	
	private void addCustomMesh() {		
		IndexedModel plane = new IndexedModel(); {
			plane.addVertex(new Vector3f(1.0f, -1.0f, 0.0f));  plane.addTexCoord(new Vector2f(1.0f, 1.0f));
			plane.addVertex(new Vector3f(1.0f, 1.0f, 0.0f));   plane.addTexCoord(new Vector2f(1.0f, 0.0f));
			plane.addVertex(new Vector3f(-1.0f, -1.0f, 0.0f)); plane.addTexCoord(new Vector2f(0.0f, 1.0f));
			plane.addVertex(new Vector3f(-1.0f, 1.0f, 0.0f));  plane.addTexCoord(new Vector2f(0.0f, 0.0f));
			
			plane.addFace(0, 1, 2);
			plane.addFace(2, 1, 3);
		}
		
		GameObject go = new GameObject()
					.addComponent(new MeshRenderer(new Mesh("plane", plane.finish()), new Material("bricks")));
		
		go.getTransform().getPosition().set(0.0f, -1.0f, 5.0f);
		
		addChild(go);
	}
	
	private void addMesh() {
		GameObject go = new GameObject()
					.addComponent(new MeshRenderer(new Mesh("plane.obj"), new Material("bricks2")));
		
		go.getTransform().getPosition().set(0.0f, -1.0f, 5.0f);
		
		addChild(go);
	}
	
	private void addLights() {
		GameObject directionalLight = new GameObject()
										.addComponent(new DirectionalLight(new Color(0.93f, 0.93f, 0.93f), 0.2f));
		GameObject pointLight = new GameObject()
									.addComponent(new PointLight(new Color(0.0f, 1.0f, 0.5f), 0.9f, new Attenuation(0.0f, 0.0f, 1.0f)));
		GameObject spotLight = new GameObject()
									.addComponent(new SpotLight(new Color(1.0f, 1.0f, 0.0f), 1.0f, new Attenuation(0.0f, 0.0f, 0.05f), 0.7f));
		
		directionalLight.getTransform().setRotation(new Quaternion(new Vector3f(1.0f, 0.0f, 0.0f), (float)Math.toRadians(-45.0f)));
		
		spotLight.getTransform().getPosition().set(3.0f, 0.0f, 3.0f);
		spotLight.getTransform().setRotation(new Quaternion(new Vector3f(0.0f, 1.0f, 0.0f), (float)Math.toRadians(90.0f)));
		
		addChild(directionalLight);
		addChild(pointLight);
		addChild(spotLight);
	}
}
