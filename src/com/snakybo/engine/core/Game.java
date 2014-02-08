package com.snakybo.engine.core;

public abstract class Game {
	private GameObject root;
	
	/** Initialize the game */
	public void init() { }
	
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
	
	/** @return The root game object */
	public GameObject getRoot() {
		if(root == null)
			root = new GameObject();
		
		return root;
	}
}
