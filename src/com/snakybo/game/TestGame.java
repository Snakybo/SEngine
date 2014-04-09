package com.snakybo.game;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.components.DirectionalLight;
import com.snakybo.sengine.components.FreeLook;
import com.snakybo.sengine.components.FreeMove;
import com.snakybo.sengine.components.MeshRenderer;
import com.snakybo.sengine.components.PointLight;
import com.snakybo.sengine.components.SpotLight;
import com.snakybo.sengine.core.Game;
import com.snakybo.sengine.core.GameObject;
import com.snakybo.sengine.core.utils.Quaternion;
import com.snakybo.sengine.core.utils.Vector2f;
import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.Attenuation;
import com.snakybo.sengine.rendering.Material;
import com.snakybo.sengine.rendering.Mesh;
import com.snakybo.sengine.rendering.Texture;
import com.snakybo.sengine.rendering.Vertex;
import com.snakybo.sengine.rendering.Window;

public class TestGame extends Game {
	@Override
	public void init() {
		super.init();
		
		Vertex[] vertices = new Vertex[] {
			new Vertex(new Vector3f(-10.0f, 0.0f, -10.0f), new Vector2f(0.0f, 0.0f)),
			new Vertex(new Vector3f(-10.0f, 0.0f, 10.0f * 3), new Vector2f(0.0f, 1.0f)),
			new Vertex(new Vector3f(10.0f * 3, 0.0f, -10.0f), new Vector2f(1.0f, 0.0f)),
			new Vertex(new Vector3f(10.0f * 3, 0.0f, 10.0f * 3), new Vector2f(1.0f, 1.0f))
		};
		
		Vertex[] vertices2 = new Vertex[] {
			new Vertex(new Vector3f(-10.0f / 10, 0.0f, -10.0f / 10), new Vector2f(0.0f, 0.0f)),
			new Vertex(new Vector3f(-10.0f / 10, 0.0f, 10.0f / 10 * 3), new Vector2f(0.0f, 1.0f)),
			new Vertex(new Vector3f(10.0f / 10 * 3, 0.0f, -10.0f / 10), new Vector2f(1.0f, 0.0f)),
			new Vertex(new Vector3f(10.0f / 10 * 3, 0.0f, 10.0f / 10 * 3), new Vector2f(1.0f, 1.0f))
		};
		
		int indices[] = {0, 1, 2, 2, 1, 3};
		int indices2[] = {0, 1, 2, 2, 1, 3};
		
		
		Mesh plane1 = new Mesh(vertices, indices, true);
		Mesh plane2 = new Mesh(vertices2, indices2, true);
		Mesh monkey = new Mesh("monkey3.obj");
		
		Material brickMaterial = new Material();
		Material testMaterial = new Material();
		
		brickMaterial.addTexture("diffuse", new Texture("bricks.jpg"));
		brickMaterial.addFloat("specularIntensity", 1);
		brickMaterial.addFloat("specularPower", 8);
		
		testMaterial.addTexture("diffuse", new Texture("test.png"));
		testMaterial.addFloat("specularIntensity", 1);
		testMaterial.addFloat("specularPower", 8);
		
		GameObject planeGo1 = new GameObject(new MeshRenderer(plane1, brickMaterial));
		GameObject planeGo2 = new GameObject(new MeshRenderer(plane2, brickMaterial));
		GameObject planeGo3 = new GameObject(new MeshRenderer(plane2, brickMaterial));
		GameObject monkeyGo1 = new GameObject(new LookAtComponent(), new MeshRenderer(monkey, brickMaterial));
		GameObject monkeyGo2 = new GameObject(new MeshRenderer(new Mesh("monkey3.obj"), testMaterial));
		
		GameObject directionalLight = new GameObject(new DirectionalLight(new Vector3f(0.93f, 0.93f, 0.93f), 0.2f));
		GameObject pointLight = new GameObject(new PointLight(new Vector3f(0, 1, 0.5f), 0.4f, new Attenuation(0, 0, 1)));
		GameObject spotLight = new GameObject(new SpotLight(new Vector3f(0.5f, 1, 1), 0.4f, new Attenuation(0, 0, 0.1f), 0.7f));
		
		planeGo1.getTransform().getLocalPosition().set(0, -1, 5);
		
		planeGo2.getTransform().getLocalPosition().set(0, 2, 0);
		planeGo2.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), 0.4f));
		
		planeGo3.getTransform().getLocalPosition().set(0, 0, 5);
		
		monkeyGo1.getTransform().getLocalPosition().set(5, 5, 5);
		monkeyGo1.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(-70.0f)));
		
		monkeyGo2.getTransform().getLocalPosition().set(-3, 7, 4);
		
		directionalLight.getTransform().setRotation(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-45)));
		
		spotLight.getTransform().getLocalPosition().set(5, 0, 5);
		spotLight.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90.0f)));
		
		planeGo2.addChild(planeGo3);
		planeGo3.addChild(new GameObject(new FreeLook(0.5f), new FreeMove(10.0f), new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth() / (float)Window.getHeight(),0.01f, 1000.0f)));
		
		addObject(planeGo1);
		addObject(planeGo2);
		addObject(monkeyGo1);
		addObject(monkeyGo2);
		
		addObject(directionalLight);
		addObject(pointLight);
		addObject(spotLight);
	}
}
