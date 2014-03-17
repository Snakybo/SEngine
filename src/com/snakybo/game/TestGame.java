package com.snakybo.game;

import com.snakybo.engine.components.Camera;
import com.snakybo.engine.components.DirectionalLight;
import com.snakybo.engine.components.MeshRenderer;
import com.snakybo.engine.components.PointLight;
import com.snakybo.engine.components.SpotLight;
import com.snakybo.engine.core.Game;
import com.snakybo.engine.core.GameObject;
import com.snakybo.engine.core.Quaternion;
import com.snakybo.engine.core.Vector2f;
import com.snakybo.engine.core.Vector3f;
import com.snakybo.engine.renderer.Material;
import com.snakybo.engine.renderer.Mesh;
import com.snakybo.engine.renderer.Texture;
import com.snakybo.engine.renderer.Vertex;
import com.snakybo.engine.renderer.Window;

/** @author Kevin Krol
 * @since Jan 30, 2014 */
public class TestGame extends Game {
	private Mesh mesh;
	private Mesh mesh2;
	private Material material;
	
	public void init() {
			addBase();
		addLights();
		addTestObjects();
	}
	
	private void addBase() {
		Vertex[] vertices =	new Vertex[] {
			new Vertex(new Vector3f(-10.0f, 0.0f, -10.0f), new Vector2f(0, 0)),
			new Vertex(new Vector3f(-10.0f, 0.0f, 10.0f * 3), new Vector2f(0, 1)),
			new Vertex(new Vector3f(10.0f * 3, 0.0f, -10.0f), new Vector2f(1, 0)),
			new Vertex(new Vector3f(10.0f * 3, 0.0f, 10.0f * 3), new Vector2f(1, 1))
		};
		
		int indices[] = {
			0, 1, 2,
			2, 1, 3
		};
		
		Vertex[] vertices2 = new Vertex[] {
			new Vertex(new Vector3f(-2, 0.0f, -2), new Vector2f(0, 0)),
			new Vertex(new Vector3f(-2, 0.0f, 2), new Vector2f(0, 1)),
			new Vertex(new Vector3f(2, 0.0f, -2), new Vector2f(1, 0)),
			new Vertex(new Vector3f(2, 0.0f, 2), new Vector2f(1, 1))
		};
		
		int indices2[] = {
			0, 1, 2,
			2, 1, 3
		};
		
		mesh = new Mesh(vertices, indices, true);
		mesh2 = new Mesh(vertices2, indices2, true);
		material = new Material();
		
		material.addTexture("diffuse", new Texture("test.png"));
		material.addFloat("specularIntensity", 1);
		material.addFloat("specularPower", 8);
	}
	
	private void addLights() {
		GameObject directionalLight = new GameObject().addComponent(new DirectionalLight(new Vector3f(0, 0, 1), 0.4f));
		GameObject pointLight = new GameObject().addComponent(new PointLight(new Vector3f(0, 1, 0), 0.4f, new Vector3f(0, 0, 1)));
		GameObject spotLight = new GameObject().addComponent(new SpotLight(new Vector3f(0, 1, 1), 0.4f, new Vector3f(0, 0, 0.1f), 0.7f));
		
		directionalLight.getTransform().setRotation(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-45)));
		
		spotLight.getTransform().setPosition(new Vector3f(5, 0, 5));
		spotLight.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90.0f)));
		
		addObject(directionalLight);
		addObject(pointLight);
		addObject(spotLight);
	}
	
	private void addTestObjects() {
		GameObject plane = new GameObject().addComponent(new MeshRenderer(mesh, material));

		GameObject testMesh1 = new GameObject().addComponent(new MeshRenderer(mesh2, material));
		GameObject testMesh2 = new GameObject().addComponent(new MeshRenderer(mesh2, material));
		GameObject monkey3 = new GameObject().addComponent(new MeshRenderer(new Mesh("monkey.obj"), material));
		
		plane.getTransform().setPosition(new Vector3f(0, -1, 5));;
		
		testMesh1.getTransform().setPosition(new Vector3f(0, 2, 0));
		testMesh1.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), 0.4f));
		
		testMesh2.getTransform().setPosition(new Vector3f(0, 0, 5));
		
		monkey3.getTransform().getPosition().set(5, 5, 5);
		monkey3.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(-70.0f)));
		
		testMesh1.addChild(testMesh2);
		testMesh2.addChild(new GameObject().addComponent(new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth() / (float)Window.getHeight(), 0.1f, 1000.0f)));
	
		addObject(plane);	
		addObject(testMesh1);
		addObject(monkey3);
	}
}
