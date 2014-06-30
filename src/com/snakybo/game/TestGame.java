package com.snakybo.game;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.components.FreeLook;
import com.snakybo.sengine.components.FreeMove;
import com.snakybo.sengine.components.MeshRenderer;
import com.snakybo.sengine.components.lighting.DirectionalLight;
import com.snakybo.sengine.components.lighting.PointLight;
import com.snakybo.sengine.components.lighting.SpotLight;
import com.snakybo.sengine.core.Game;
import com.snakybo.sengine.core.GameObject;
import com.snakybo.sengine.core.utils.Quaternion;
import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.Attenuation;
import com.snakybo.sengine.rendering.Material;
import com.snakybo.sengine.rendering.Mesh;
import com.snakybo.sengine.rendering.Texture;
import com.snakybo.sengine.rendering.Window;

public class TestGame extends Game {
	public void init() {
		Mesh planeMesh = new Mesh("plane.obj");
		Mesh monkeyMesh = new Mesh("monkey.obj");
		
		Texture bricks = new Texture("bricks.jpg");
		
		Material bricksMaterial = new Material();
		bricksMaterial.addDiffuseTexture(bricks);
		bricksMaterial.addNormalMap(new Texture("bricks_normal.jpg"));
		bricksMaterial.addDispMap(new Texture("bricks_disp.png"), 0.04f, -0.5f);
		bricksMaterial.addSpecular(0.5f, 4);
		
		Material bricks2Material = new Material();
		bricks2Material.addDiffuseTexture(new Texture("bricks2.jpg"));
		bricks2Material.addNormalMap(new Texture("bricks2_normal.jpg"));
		bricks2Material.addDispMap(new Texture("bricks2_disp.jpg"), 0.04f, -1.0f);
		
		Material bricks3Material = new Material();
		bricks3Material.addDiffuseTexture(new Texture("bricks.jpg"));
		//bricks3Material.addNormalMap(new Texture("bricks_normal.jpg"));
		bricks3Material.addSpecular(0.5f, 4);
		
		MeshRenderer bricksRenderer = new MeshRenderer(planeMesh, bricksMaterial);
		MeshRenderer bricks2Renderer = new MeshRenderer(monkeyMesh, bricks2Material);
		MeshRenderer bricks3Renderer = new MeshRenderer(planeMesh, bricks3Material);
		
		GameObject planeObject = new GameObject();
		planeObject.addComponent(bricksRenderer);
		planeObject.getTransform().getPosition().set(0, -1, 5);
		
		GameObject plane2Object = new GameObject();
		plane2Object.addComponent(bricks3Renderer);
		plane2Object.getTransform().getPosition().set(17, -1, 5);
		
		GameObject directionalLight = 
			new GameObject().addComponent(
				new DirectionalLight(
					new Vector3f(1, 1, 1),
					0.1f
				)
			);
		
		GameObject pointLight =
			new GameObject().addComponent(
				new PointLight(
					new Vector3f(0, 1, 0),
					0.4f,
					new Attenuation(0, 0, 1)
				)
			);
		
		GameObject spotLight =
			new GameObject().addComponent(
				new SpotLight(
					new Vector3f(0, 1, 1), 
					0.4f,
					new Attenuation(0, 0, 0.1f),
					0.7f
				)
			);
		
		directionalLight.getTransform().setRotation(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-45)));
		
		spotLight.getTransform().getPosition().set(5, 0, 5);
		spotLight.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90.0f)));
		
		GameObject monkeyObject =
			new GameObject()
				.addComponent(new LookAtComponent())
				.addComponent(bricks2Renderer);
		
		GameObject cameraObject =
			new GameObject()
				.addComponent(new FreeLook(0.5f))
				.addComponent(new FreeMove(10.0f))
				.addComponent(new Camera(
					(float)Math.toRadians(70.0f),
					(float)Window.getWidth() / (float)Window.getHeight(),
					0.01f,
					1000.0f
				));
		
		GameObject monkey2Object =
			new GameObject().addComponent(
				new MeshRenderer(new Mesh("monkey.obj"), bricks2Material)
			);
		
		monkeyObject.getTransform().getPosition().set(5, 5, 5);
		monkeyObject.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(-70.0f)));
		
		monkey2Object.getTransform().getPosition().set(2, 0, 2);
		
		addObject(cameraObject);
		addObject(planeObject);
		addObject(plane2Object);
		addObject(monkeyObject);
		addObject(monkey2Object);
		addObject(directionalLight);
		addObject(pointLight);
		addObject(spotLight);
	}
}
