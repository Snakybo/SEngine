package com.snakybo.sengine.core;

import com.snakybo.sengine.rendering.RenderingEngine;

/** The base game class, with essential functionality
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public abstract class Game {
	private GameObject root;
	
	/** Initialize the game */
	protected void init(CoreEngine coreEngine) {}
	
	/** Handle input in the game
	 * @param delta The current delta time */
	protected void input(float delta) {
		root.inputAll(delta);
	}
	
	/** Update the game each frame
	 * @param delta The current delta time */
	protected void update(float delta) {
		root.updateAll(delta);
	}
	
	/** Render the game each frame
	 * @param renderingEngine The rendering engine */
	protected void render(RenderingEngine renderingEngine) {
		renderingEngine.render(root);
	}
	
	/** Add an game object to the scene engine */
	public void addChild(GameObject gameObject) {
		root.addChild(gameObject);
	}
	
	/** Set the core engine of the game
	 * @param engine The core engine */
	public void setEngine(CoreEngine engine) {
		if(root == null)
			root = new GameObject();
		
		root.setEngine(engine);
	}
}
