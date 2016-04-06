# SEngine [![Build Status](https://travis-ci.org/Snakybo/SEngine.svg?branch=master)](https://travis-ci.org/Snakybo/SEngine)

A 3D game engine, made in Java using [LWJGL](https://www.lwjgl.org/).

## Core features:
- [Component system](#component-system)
- [Forward rendering](#forward-rendering)
- [Lighting](#lighting)
- [Normal maps](http://en.wikipedia.org/wiki/Normal_mapping)
- [Displacement maps](http://en.wikipedia.org/wiki/Displacement_mapping)
- [Model loader](#model-loader)
- [Automatic resource management](#automatic-resource-management)
- [Anti-Aliasing](#anti-aliasing)
- [Shadow mapping](#shadow-mapping)
- [Mipmaps](#mipmaps)
- [Skyboxes](#skyboxes)
- [Custom Cursors](#custom-cursors)

## Planned features
- Physically Based Rendering
- Support for different types of models
- Profiling system
- Physics

## Component system
When you create your game, you can fill a scene with objects. The objects are just a point in space with components. Components make up for all the game logic.

## Forward rendering
The engine uses a forward rendering system, [Physically Based Rendering](http://www.pbrt.org/) is on the [Planned features](#planned-features) list.

## Lighting
The engine supports ambient lighting, as well as directional lights, point lights and spot lights.

## Model loader
The engine has the ability to load 3D models. Currently only .obj is supported. Other filetypes are on the [Planned features](#planned-features) list.

## Automatic resource management
The engine automatically manages resources that have been loaded. Whenever you create a texture or mesh it automatically checks if it has been loaded before. It also automatically frees any memory allocated to the object when the last reference to the file has been removed.

## Anti-Aliasing
Currently the engine supports [multisample anti-aliasing](http://en.wikipedia.org/wiki/Multisample_anti-aliasing).

## Shadow Mapping
The engine makes use of [variance shadow mapping](http://http.developer.nvidia.com/GPUGems3/gpugems3_ch08.html), this feature is still under development.

## Mipmaps
There is support for texture mipmapping.

## Skyboxes
There is support for custom cubemap skyboxes.

## Custom Cursors
You can use any of the system-default cursors, or specify your own image.
