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
import com.snakybo.sengine.core.Input;
import com.snakybo.sengine.core.Input.KeyCode;
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
	private Material tiles;
	private Material fireTiles;
	
	private PointLight pointLight;
	private SpotLight spotLight;
	
	@Override
	public void init() {
		super.init();
		
		// Material:
		// 	- [diffuse]
		//	- [normalMap]
		//	- [specularIntensity]
		//	- [specularPower]
		
		tiles = new Material();
		fireTiles = new Material();
		
		tiles.addTexture("diffuse", new Texture("tegels.png"));
		tiles.addTexture("normalMap", new Texture("tegels_normal.jpg"));
		tiles.addFloat("specularIntensity", 1);
		tiles.addFloat("specularPower", 8);
		
		fireTiles.addTexture("diffuse", new Texture("fire_dungeon.jpg"));
		fireTiles.addTexture("normalMap", new Texture("fire_dungeon_normal.jpg"));
		fireTiles.addFloat("specularIntensity", 1);
		fireTiles.addFloat("specularPower", 8);
		
		addPlanes();
		addMonkeys();
		addLights();
		
		// GameObject:
		//	- component1
		//	- component2
		//	- ...
		
		// Camera:
		//	- Field of view
		//	- Aspect ratio
		//	- Near clipping plane
		//	- Far clipping plane
		
		Camera camera = new Camera((float)Math.toRadians(70.0f), (float)Window.getWidth() / (float)Window.getHeight(), 0.01f, 1000.0f);
		
		addChild(new GameObject(new FreeLook(0.5f), new FreeMove(10.0f), camera));
	}
	
	@Override
	public void input(float delta) {
		super.input(delta);
		
		Vector3f newAttenuation = new Vector3f(0, 0, 0);
		
		if(Input.getKey(KeyCode.U)) {
			newAttenuation = newAttenuation.add(new Vector3f(0.01f, 0, 0));
		} else if(Input.getKey(KeyCode.J)) {
			newAttenuation = newAttenuation.sub(new Vector3f(0.01f, 0, 0));
		}
		
		if(Input.getKey(KeyCode.I)) {
			newAttenuation = newAttenuation.add(new Vector3f(0, 0.01f, 0));
		} else if(Input.getKey(KeyCode.K)) {
			newAttenuation = newAttenuation.sub(new Vector3f(0, 0.01f, 0));
		}
		
		if(Input.getKey(KeyCode.O)) {
			newAttenuation = newAttenuation.add(new Vector3f(0, 0, 0.01f));
		} else if(Input.getKey(KeyCode.L)) {
			newAttenuation = newAttenuation.sub(new Vector3f(0, 0, 0.01f));
		}
		
		if(!newAttenuation.equals(new Vector3f(0, 0, 0))) {
			pointLight.getAttenuation().set(pointLight.getAttenuation().add(newAttenuation));
			
			System.out.println("Attenuation: " + pointLight.getAttenuation());
		}
	}
	
	private void addPlanes() {
		Vertex[] vertices = new Vertex[] {
			new Vertex(new Vector3f(-1, 0, -1), new Vector2f(0.0f, 0.0f)),
			new Vertex(new Vector3f(-1, 0, 1), new Vector2f(0.0f, 1.0f)),
			new Vertex(new Vector3f(1, 0, -1), new Vector2f(1.0f, 0.0f)),
			new Vertex(new Vector3f(1, 0, 1), new Vector2f(1.0f, 1.0f))
		};
		
		int indices[] = {0, 1, 2, 2, 1, 3};
		
		for(int y = 0; y < 10; y++) {
			for(int x = 0; x < 10; x++) {
				GameObject plane = new GameObject(new MeshRenderer(new Mesh(vertices, indices, true), fireTiles));
				
				plane.getTransform().getLocalPosition().set(x * 2, -1, y * 2);
				
				addChild(plane);
			}
		}
		
		for(int i = 0; i < 2; i++) {
			GameObject plane;
			
			for(int x = -1; x < 11; x++) {				
				plane = new GameObject(new MeshRenderer(new Mesh(vertices, indices, true), tiles));
				
				plane.getTransform().getLocalPosition().set(x * 2, -1, (i * 22) - 2);
				
				addChild(plane);
			}
			
			for(int y = 0; y < 10; y++) {
				plane = new GameObject(new MeshRenderer(new Mesh(vertices, indices, true), tiles));
				
				plane.getTransform().getLocalPosition().set((i * 22) - 2, -1, y * 2);
				
				addChild(plane);
			}
		}
	}
	
	private void addMonkeys() {
		GameObject monkey1 = new GameObject(new LookAtComponent(), new MeshRenderer(new Mesh("monkey.obj"), tiles));
		GameObject monkey2 = new GameObject(new MeshRenderer(new Mesh("monkey.obj"), new Material()));
		
		monkey1.getTransform().getLocalPosition().set(5, 5, 5);
		monkey1.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(-70.0f)));
		
		monkey2.getTransform().getLocalPosition().set(-3, 7, 4);
		
		addChild(monkey1);
		addChild(monkey2);
	}
	
	private void addLights() {
		// Directional Light:
		//	- color
		//	- intensity
		
		// Point Light:
		//	- color
		//	- intensity
		//	- attenuation
		//		- constant
		//		- linear
		//		- exponent
		
		// Spot Light:
		//	- color
		//	- intensity
		//	- attenuation
		//		- constant
		//		- linear
		//		- exponent
		//	- cuttoff
		
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(0.93f, 0.93f, 0.93f), 0.2f);
		
		pointLight = new PointLight(new Vector3f(0, 1, 0.5f), 0.4f, new Attenuation(0, 0, 1));
		spotLight = new SpotLight(new Vector3f(1, 1, 0), 1, new Attenuation(0, 0, 0.05f), 0.7f);
		
		GameObject directionalLightGo = new GameObject(directionalLight);
		GameObject pointLightGo = new GameObject(pointLight);
		GameObject spotLightGo = new GameObject(spotLight);
		
		directionalLightGo.getTransform().setRotation(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-45)));
		
		spotLightGo.getTransform().getLocalPosition().set(5, 0, 5);
		spotLightGo.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90.0f)));
		
		addChild(directionalLightGo);
		addChild(pointLightGo);
		addChild(spotLightGo);
	}
}
