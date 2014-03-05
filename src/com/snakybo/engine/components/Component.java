package com.snakybo.engine.components;

import com.snakybo.engine.core.GameObject;
import com.snakybo.engine.core.Transform;
import com.snakybo.engine.renderer.Renderer;
import com.snakybo.engine.renderer.Shader;

/** @author Kevin Krol
 * @since Jan 31, 2014 */
public abstract class Component {
	private GameObject gameObject;
	
	/** Handle input for the component
	 * @param transform The transformation of the game object
	 * @param delta The delta time */
	public void input(float delta) {}
	
	/** Update the component
	 * @param transform The transformation of the game object
	 * @param delta The delta time */
	public void update(float delta) {}
	
	/** Render the component
	 * @param transform The transformation of the game object
	 * @param shader The shader the game object is using */
	public void render(Shader shader, Renderer renderer) {}
	
	public void addToRenderer(Renderer renderer) {}
	
	public void setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}
	
	public Transform getTransform() {
		return gameObject.getTransform();
	}
}
