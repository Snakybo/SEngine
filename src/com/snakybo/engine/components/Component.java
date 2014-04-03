package com.snakybo.engine.components;

import com.snakybo.engine.core.GameObject;
import com.snakybo.engine.core.Transform;
import com.snakybo.engine.renderer.Renderer;
import com.snakybo.engine.renderer.Shader;

/** @author Kevin Krol
 * @since Jan 31, 2014 */
public abstract class Component {
	private GameObject gameObject;
	
	/** Use this method to handle input in your component
	 * @param delta The delta time */
	public void input(float delta) {}
	
	/** Use this method to update your component
	 * @param delta The delta time */
	public void update(float delta) {}
	
	/** Use this method to render your component
	 * @param shader The shader the game object is using
	 * @param renderer The rendering engine */
	public void render(Shader shader, Renderer renderer) {}
	
	// IMPROVEME: Change this for something more generic
	/** Add the component to the renderer */
	public void addToRenderer(Renderer renderer) {}
	
	// IMPROVEME: Remove the ability to externally change the game object
	/** Set the game object of the component */
	public void setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}
	
	/** @return The transform of the game object */
	public Transform getTransform() {
		return gameObject.getTransform();
	}
}
