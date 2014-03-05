package com.snakybo.engine.core;

import com.snakybo.engine.renderer.Renderer;

/** @author Kevin Krol */
public abstract class Game {
	private GameObject root;
	
	/** Initialize the game */
	public void init() {}
	
	/** Handle input for the game
	 * @param delta The delta time */
	public void input(float delta) {
		getRoot().input(delta);
	}
	
	/** Update the game
	 * @param delta The delta time */
	public void update(float delta) {
		getRoot().update(delta);
	}
	
	/** Render the game
	 * @param renderer The rendering engine */
	public void render(Renderer renderer) {
		renderer.render(getRoot());
	}
	
	/** Add a game object to the game
	 * @param object The game object to add to the game */
	public void addObject(GameObject object) {
		getRoot().addChild(object);
	}
	
	/** @return The root game object */
	private GameObject getRoot() {
		if(root == null)
			root = new GameObject();
		
		return root;
	}
}
