package com.snakybo.sengine.components;

import com.snakybo.sengine.core.CoreEngine;
import com.snakybo.sengine.core.GameObject;
import com.snakybo.sengine.core.Transform;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Shader;

/** Component base class
 * 
 * <p>
 * Every component in the game should extend this class
 * </p>
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public abstract class Component {
	private GameObject parent;
	
	/** Handle component input
	 * @param delta The current delta time */
	public void input(float delta) {}
	
	/** Update the component every frame
	 * @param delta The current delta time */
	public void update(float delta) {}
	
	/** Render the component every frame
	 * @param shader The active shader
	 * @param renderingEngine The rendering engine */
	public void render(Shader shader, RenderingEngine renderingEngine) {}
	
	/** Add the component to the engine
	 * @param engine The core engine */
	public void addToEngine(CoreEngine engine) {}
	
	/** Set the parent game object of the component */
	public void setParent(GameObject parent) {
		this.parent = parent;
	}
	
	/** @return The transform of the parent game object */
	public Transform getTransform() {
		return parent.getTransform();
	}
}
