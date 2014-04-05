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
import com.snakybo.sengine.core.Quaternion;
import com.snakybo.sengine.core.Vector2f;
import com.snakybo.sengine.core.Vector3f;
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
		
		float fieldDepth = 10.0f;
		float fieldWidth = 10.0f;
		
		Vertex[] vertices =
				new Vertex[] {new Vertex(new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 0.0f)),
						new Vertex(new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 1.0f)),
						new Vertex(new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 0.0f)),
						new Vertex(new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 1.0f))};
		
		int indices[] = {0, 1, 2, 2, 1, 3};
		
		Vertex[] vertices2 =
				new Vertex[] {
						new Vertex(new Vector3f(-fieldWidth / 10, 0.0f, -fieldDepth / 10), new Vector2f(0.0f, 0.0f)),
						new Vertex(new Vector3f(-fieldWidth / 10, 0.0f, fieldDepth / 10 * 3), new Vector2f(0.0f, 1.0f)),
						new Vertex(new Vector3f(fieldWidth / 10 * 3, 0.0f, -fieldDepth / 10), new Vector2f(1.0f, 0.0f)),
						new Vertex(new Vector3f(fieldWidth / 10 * 3, 0.0f, fieldDepth / 10 * 3), new Vector2f(1.0f,
								1.0f))};
		
		int indices2[] = {0, 1, 2, 2, 1, 3};
		
		Mesh mesh2 = new Mesh(vertices2, indices2, true);
		
		Mesh mesh = new Mesh(vertices, indices, true);
		Material material = new Material();
		material.addTexture("diffuse", new Texture("bricks.jpg"));
		material.addFloat("specularIntensity", 1);
		material.addFloat("specularPower", 8);
		
		Material material2 = new Material();
		material2.addTexture("diffuse", new Texture("test.png"));
		material2.addFloat("specularIntensity", 1);
		material2.addFloat("specularPower", 8);
		
		Mesh tempMesh = new Mesh("monkey3.obj");
		
		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);
		
		GameObject planeObject = new GameObject();
		planeObject.addComponent(meshRenderer);
		planeObject.getTransform().getLocalPosition().set(0, -1, 5);
		
		GameObject directionalLightObject = new GameObject();
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(0, 0, 1), 0.4f);
		
		directionalLightObject.addComponent(directionalLight);
		
		GameObject pointLightObject = new GameObject();
		pointLightObject.addComponent(new PointLight(new Vector3f(0, 1, 0), 0.4f, new Attenuation(0, 0, 1)));
		
		SpotLight spotLight = new SpotLight(new Vector3f(0, 1, 1), 0.4f, new Attenuation(0, 0, 0.1f), 0.7f);
		
		GameObject spotLightObject = new GameObject();
		spotLightObject.addComponent(spotLight);
		
		spotLightObject.getTransform().getLocalPosition().set(5, 0, 5);
		spotLightObject.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90.0f)));
		
		addObject(planeObject);
		addObject(directionalLightObject);
		addObject(pointLightObject);
		addObject(spotLightObject);
		
		GameObject testMesh1 = new GameObject().addComponent(new MeshRenderer(mesh2, material));
		GameObject testMesh2 = new GameObject().addComponent(new MeshRenderer(mesh2, material));
		GameObject testMesh3 =
				new GameObject().addComponent(new LookAtComponent()).addComponent(new MeshRenderer(tempMesh, material));
		
		testMesh1.getTransform().getLocalPosition().set(0, 2, 0);
		testMesh1.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), 0.4f));
		
		testMesh2.getTransform().getLocalPosition().set(0, 0, 5);
		
		testMesh1.addChild(testMesh2);
		testMesh2.addChild(new GameObject()
				.addComponent(new FreeLook(0.5f))
				.addComponent(new FreeMove(10.0f))
				.addComponent(
						new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth() / (float)Window.getHeight(),
								0.01f, 1000.0f)));
		
		addObject(testMesh1);
		addObject(testMesh3);
		
		testMesh3.getTransform().getLocalPosition().set(5, 5, 5);
		testMesh3.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(-70.0f)));
		
		addObject(new GameObject().addComponent(new MeshRenderer(new Mesh("monkey3.obj"), material2)));
		
		directionalLight.getTransform().setRotation(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-45)));
	}
}
