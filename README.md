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

To learn how to create your own lights, check the wiki

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
import com.snakybo.sengine.components.MeshRenderer;
import com.snakybo.sengine.components.lighting.DirectionalLight;
import com.snakybo.sengine.components.lighting.PointLight;
import com.snakybo.sengine.components.lighting.SpotLight;
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
	public void init() {
		// Create a camera
		Camera.initPerspectiveCamera(
			(float)Math.toRadians(70.0f),
			(float)Window.getWidth() / (float)Window.getHeight(),
			0.01f, 1000.0f
		); 
		
		// Add the camera to the scene, along with a component to look around and to move around
		addChild(
			new GameObject()
				.addComponent(new FreeLook(Window.getCenter()))
				.addComponent(new FreeMove())
				.addComponent(RenderingEngine.getMainCamera())
		);
		
		// Create a material
		new Material(
			"bricks", // The name of the material
			new Texture("bricks.png"), 0.5f, 4.0f, // The diffuse texture, the specular intensity and the specular power
			new Texture("bricks_normal.png"), // The normal map
			new Texture("bricks_disp.png"), 0.03f, -0.5f // The displacement map, along with the displacement map scale and the displacement map offset
		);
		
		// Create another material with just a diffuse texutre, and specular information
		new Material(
			"bull",
			new Texture("BullTex.jpg"), 0.5f, 4.0f
		);
		
		// Add a bull model to the scene
		addChild(new GameObject(new Vector3f(2.0f, 0.0f, 2.0f), new Quaternion(), 5.0f)
					.addComponent(new MeshRenderer(new Mesh("stier.obj"), new Material("bull"))));
		
		// Add a basic plane to the scene
		addChild(new GameObject(new Vector3f(0.0f, -1.0f, 0.0f))
					.addComponent(new MeshRenderer(new Mesh("plane.obj"), new Material("bricks"))));
		
		addLights();
	}
	
	private void addLights() {
		// Create a game object with a directional light component
		GameObject directionalLight = new GameObject().addComponent(
			new DirectionalLight(
				new Color(1.0f, 1.0f, 1.0f), // The color of the directional light
				0.1f // The intensity of the directional light
			)
		);
		
		// Create a game object with a point light component
		GameObject pointLight = new GameObject().addComponent(
			new PointLight(
				new Color(0.0f, 1.0f, 0.5f), // The color of the point light
				0.9f, // The intensity of the point light
				new Attenuation(0.0f, 0.0f, 1.0f) // The attenuation of the point light
			)
		);
		
		// Create a new game object with a spot light component
		GameObject spotLight = new GameObject().addComponent(
			new SpotLight(
				new Color(1.0f, 1.0f, 0.0f), // The color of the spot light
				1.0f, // The intensity of the pospotint light
				new Attenuation(0.0f, 0.0f, 0.05f), // The attenuation of the spot light
				0.7f // The cutoff range of the spot light
			)
		);
		
		// Translate and rotate the lights
		directionalLight.getTransform().setRotation(new Quaternion(new Vector3f(1.0f, 0.0f, 0.0f), (float)Math.toRadians(-45.0f)));
		
		spotLight.getTransform().getPosition().set(3.0f, 0.0f, 3.0f);
		spotLight.getTransform().setRotation(new Quaternion(new Vector3f(0.0f, 1.0f, 0.0f), (float)Math.toRadians(90.0f)));
		
		// Add the lights to the scene
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
	public void update(double delta) {
		Quaternion newRotation =
				getTransform().getLookAtRotation(
						RenderingEngine.getMainCamera().getTransform().getWorldPosition(), new Vector3f(0, 1, 0)); // Calculate the new rotation of the object
		
		getTransform().setRotation(getTransform().getRotation().nlerp(newRotation, (float)delta * 5.0f, true)); // Slowly rotate the game object towards the main camera
	}
}

```
