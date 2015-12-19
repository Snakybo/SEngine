# SEngine [![Build Status](https://travis-ci.org/Snakybo/SEngine.svg?branch=master)](https://travis-ci.org/Snakybo/SEngine)

A 3D game engine, made in Java using using OpenGL.

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
- [Skyboxes](#skyboxes)

## Planned features
- Physically Based Rendering
- Support for different types of models
- Profiling system
- Physics

## Component system
SEngine uses a system similar to [Unity 3D](http://unity3d.com/), It offers a scene which you can fill with game objects. You can attach multiple components to each game object.

## Forward rendering
The engine uses a forward rendering system, meaning it's fast and relatively easy on the GPU. Physically Based Rendering is on the [Planned features](#planned-features) list.

## Lighting
The engine has support for a multitude of lights, such as directional lights, point lights and spot lights.

## Model loader
The engine has the ability to load 3D models. Currently only .obj is supported. Other filetypes are on the [Planned features](#planned-features) list.

## Automatic resource management
The engine automatically manages resources that have been loaded. Whenever you create a texture or mesh it automatically checks if it has been loaded before. This saves a lot of time and memory when you try to load the same file multiple times. It also automatically frees any memory allocated to the file by OpenGL when the last reference to the file has been destroyed.

## Anti-Aliasing
Currently the engine supports [Multisample anti-aliasing](http://en.wikipedia.org/wiki/Multisample_anti-aliasing).

## Shadow Mapping
The engine now supports basic shadow mapping, currently it's restricted to directional-lights only. The short term goal is to add shadow dynamic shadow mapping for all lights.

## Skyboxes
There is support for custom cubemap skyboxes.
