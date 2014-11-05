# SEngine

A 3D game engine, made in Java using using OpenGL.

An [example](https://github.com/Snakybo/SEngine/tree/master/example/example) on how to use the engine.

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
- [Shadow mapping](#shadow-mapping)

## Planned features
- FXAA anti-aliasing
- Support for different types of models
- Dynamic shadows
- Profiling system
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

## Model loader
The engine has the ability to load 3D models. Currently only .obj is supported. Other filetypes are on the [Planned features](#planned-features) list.

## Automatic resource management
The engine automatically manages resources that have been loaded. Whenever you create a texture, mesh or shader it automatically checks if it has been loaded before. This saves a lot of time and memory when you try to load the same file multiple times. It also automatically frees any memory allocated to the file by OpenGL when the last reference to the file has been destroyed.

## Automatic shader uniform updating
The engine has the ability to automatically update most shader uniforms, it supports the GLSL datatypes ```float```, ```vec3``` , ```mat4``` and ```sampler2D```. These variables should be prefixed with ```T_```, ```M_``` or ```R_``` if it's for the ```Transform```, ```Material``` or ```Rendererer```.

## Anti-Aliasing
Currently the engine only supports [MSAA](http://en.wikipedia.org/wiki/Multisample_anti-aliasing), Other anti-aliasing techniques are on the [Planned features](#planned-features) list.

## Shadow Mapping
The engine now supports basic static shadow mapping, currently it's restricted to directional-lights only. The long term goal is to add shadow dynamic shadow mapping for all lights.
