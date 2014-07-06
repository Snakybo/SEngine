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
