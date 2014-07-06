# SEngine

A work in progress 3D game engine made in Java using OpenGL.

## Core features:
- [Component system](#component-system)
- [Forward rendering](#forward-rendering)
- [Lighting](#lighting)
- Normal maps
- Displacement maps
- [Model loader](#model-loader)
- [Automatic resource management](#automatic-resource-management)
- [Automatic shader uniform updating](#automatic-shader-uniform-updating)
- [Prefab system](#prefab-system)
- [Anti-Aliasing](#anti-aliasing)

## Planned features
- [ ] FXAA anti-aliasing
- [ ] Support for different types of models
- [ ] Dynamic shadows
- [ ] Profiling system
- [ ] Deffered rendering

## [Examples](#basic-game-example)

------------------------

## Component system
SEngine uses a system similar to [Unity 3D](http://unity3d.com/), It offers a scene that you can fill with game objects, each game object can have multiple components attached to them. This offers an unique way of developing, since there are no hard-connections between game objects or components.

## Forward rendering
The engine uses a forward rendering system, meaning it's fast and relatively easy on the GPU. Deffered rendering support is on the [Planned features](#planned-features) list.

## Lighting
The engine supports different kinds of lighting, including:
- Ambient light
- Directional light
- Point light
- Spot light

It's fairly easy to create your own light.
Every light extends the base class ```BaseLight```, which holds information about the ```color```, ```shader```, and ```intensity``` of the light. You simply have to call ```setShader(new Shader("name-of-shader"));``` to set the shader of the light.

## Model loader
The engine has the ability to load 3D models. Currently only .obj is supported. Other filetypes are on the [Planned features](#planned-features) list.

## Automatic resource management
The engine automatically manages resources that have been loaded. Whenever you create a texture, mesh or shader it automatically checks if it has been loaded before. This saves a lot of time and memory when you try to load the same file multiple times. It also automatically frees any memory allocated to the file by OpenGL when the last reference to the file has been destroyed.

## Automatic shader unfiorm updating
The engine has the ability to automatically update most shader uniforms, it supports the GLSL datatypes ```float```, ```vec3``` , ```mat4``` and ```sampler2D```. These variables should be prefixed with ```T_```, ```M_``` or ```R_``` if it's for the ```Transform```, ```Material``` or ```Rendererer```.

## Prefab system
The engine has support for a basic prefab system. This allows the developer to create a game object in a prefab file, add components and modify any variables. This allows for easy editing of objects, without having to rebuild every time you change a variable.

## Anti-Aliasing
Currently the engine only supports [MSAA](http://en.wikipedia.org/wiki/Multisample_anti-aliasing), Other anti-aliasing techniques are on the [Planned features](#planned-features) list.

------------------------

## Basic game example

Main
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

TestGame
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
		Camera camera = Camera.initPerspectiveCamera((float)Math.toRadians(70.0f), (float)Window.getWidth() / (float)Window.getHeight(), 0.01f, 1000.0f); // Create a camera and set it to a perspective projection
		
		addChild(new GameObject(new FreeLook(0.5f), new FreeMove(10.0f), camera)); // Add a new game object to the scene, with the FreeLook, FreeMove and camera components.
		
		addChild(Prefab.load("test")); // Load a prefab and add it to the scene
	}
}
```

LookAtComponent
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

Test prefab
```
Mesh plane.obj // Set the mesh of the game object

Material Diffuse bricks.jpg // Set the diffuse texture of the material
Material NormalMap bricks_normal.jpg // Set the normal map of the material
Material DispMap bricks_disp.png 0.04 -0.5 // Set the displacement map of the material
Material Specular 0.5 4 // Set the specular reflection of the material

Transform Position 0.0 -1.0 5.0 // Set the position of the game object

Component com.snakybo.game.LookAtComponent // add the LookAtComponent to the game object
```
