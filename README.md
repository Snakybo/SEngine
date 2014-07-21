# SEngine

A work in progress 3D game engine made in Java using OpenGL.

## Core features:
- [Component system](#component-system)
- [Forward rendering](#forward-rendering)
- [Lighting](#lighting)
- [Normal maps](http://en.wikipedia.org/wiki/Normal_mapping)
- [Displacement maps](http://en.wikipedia.org/wiki/Displacement_mapping)
- [Model loader](#model-loader)
- [Automatic resource management](#automatic-resource-management)
- [Automatic shader uniform updating](#automatic-shader-uniform-updating)
- [Anti-Aliasing](#anti-aliasing)

## Planned features
- FXAA anti-aliasing
- Support for different types of models
- Dynamic shadows
- Profiling system
- Deferred rendering
- Physics

## Component system
SEngine uses a system similar to [Unity 3D](http://unity3d.com/), It offers a scene which you can fill with game objects. You can attach multiple components to each game object.

## Forward rendering
The engine uses a forward rendering system, meaning it's fast and relatively easy on the GPU. Deferred rendering support is on the [Planned features](#planned-features) list.

## Lighting
The engine supports different kinds of lighting, including:
- Ambient light
- Directional light
- Point light
- Spot light

It's fairly easy to create your own light.
Every light extends the base class ```Light```, which holds information about the ```color```, ```shader```, and ```intensity``` of the light. Then you simply have to call ```setShader(new Shader("name-of-shader"));``` to set the shader of the light.

## Model loader
The engine has the ability to load 3D models. Currently only .obj is supported. Other filetypes are on the [Planned features](#planned-features) list.

## Automatic resource management
The engine automatically manages resources that have been loaded. Whenever you create a texture, mesh or shader it automatically checks if it has been loaded before. This saves a lot of time and memory when you try to load the same file multiple times. It also automatically frees any memory allocated to the file by OpenGL when the last reference to the file has been destroyed.

## Automatic shader uniform updating
The engine has the ability to automatically update most shader uniforms, it supports the GLSL datatypes ```float```, ```vec3``` , ```mat4``` and ```sampler2D```. These variables should be prefixed with ```T_```, ```M_``` or ```R_``` if it's for the ```Transform```, ```Material``` or ```Rendererer```.

## Anti-Aliasing
Currently the engine only supports [MSAA](http://en.wikipedia.org/wiki/Multisample_anti-aliasing), Other anti-aliasing techniques are on the [Planned features](#planned-features) list.

------------------------

## Basic game example

Main class
```
package com.snakybo.game;

import com.snakybo.sengine.core.SEngine;
import com.snakybo.sengine.rendering.Window;
import com.snakybo.sengine.utils.math.Vector3f;

public class Main {
	public static void main(String[] args) {
		SEngine engine = new SEngine(new TestGame()); // Create the engine, passing in your main game class
		Window window = new Window(1280, 720, "SEngine Test Game"); // Create a new window
		
		window.setAmbientColor(new Vector3f(1.0f, 1.0f, 0.0f)); // Set the ambient color to yellow
		window.setClearColor(new Vector3f(1.0f, 0.0f, 1.0f)); // Set the clear color to purple
		window.setAA(Window.MSAA, 16); // Turn on 16x MSAA
		
		window.create();
		engine.start(window, 60.0); // Start the engine, second parameter is the desired framerate of the game
	}
}

```

Game class
```
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
```

Basic look-at component class
```
package com.snakybo.game;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.utils.math.Quaternion;
import com.snakybo.sengine.utils.math.Vector3f;

public class LookAtComponent extends Component {
	@Override
	public void update(float delta) {
		Quaternion newRotation = getTransform().getLookAtRotation(RenderingEngine.getMainCamera().getTransform().getTransformedPosition(), new Vector3f(0, 1, 0));
		
		getTransform().setRotation(getTransform().getRotation().nlerp(newRotation, delta * 5.0f, true)); // Rotate the game object towards the main camera
	}
}

```
