package com.snakybo.sengine.core;

import com.snakybo.sengine.rendering.RenderingEngine;

/** The base game class, with essential functionality
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public abstract class Game {
	private GameObject root;
	
	/** Initialize the game */
	public void init() {}
	
	/** Handle input in the game
	 * @param delta The current delta time */
	public void input(float delta) {
		getRootObject().inputAll(delta);
	}
	
	/** Update the game each frame
	 * @param delta The current delta time */
	public void update(float delta) {
		getRootObject().updateAll(delta);
	}
	
	/** Render the game each frame
	 * @param renderingEngine The rendering engine */
	public void render(RenderingEngine renderingEngine) {
		renderingEngine.render(getRootObject());
	}
	
	/** Add an game object to the scene engine */
	public void addObject(GameObject gameObject) {
		getRootObject().addChild(gameObject);
	}
	
	/** Get the root game object and create one if it doesnt exist */
	private GameObject getRootObject() {
		if(root == null)
			root = new GameObject();
		
		return root;
	}
	
	/** Set the core engine of the game
	 * @param engine The core engine */
	public void setEngine(CoreEngine engine) {
		getRootObject().setEngine(engine);
	}
}
