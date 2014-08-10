package com.snakybo.sengine.core.object;

import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.resource.Shader;

/** Base component class used by the engine, every component in the game has to extend this class in
 * order to work.
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public abstract class Component {
	private GameObject parent;
	
	/** This method should be overridden when you need to handle input in a component
	 * @param delta The delta time */
	protected void input(double delta) {}
	
	/** This method should be overridden when you need to update a component, or the game object it's
	 * attached to.
	 * @param delta The delta time */
	protected void update(double delta) {}
	
	/** This method should be overridden when you require a component to render something
	 * <p>
	 * The shader that's currently active is passed as a parameter in this method
	 * </p>
	 * @param shader The currently active shader */
	protected void render(RenderingEngine renderingEngine, Shader shader) {}
	
	/** This method is called when the {@link #parent} is added to the scene
	 * @param renderingEngine The rendering engine */
	protected void onAddedToScene(RenderingEngine renderingEngine) {}
	
	/** Set the parent of the component
	 * <p>
	 * This method should only be used internally by the engine
	 * </p>
	 * @param parent The new parent of the game object */
	public void setParent(GameObject parent) {
		this.parent = parent;
	}
	
	/** @return The {@link Transform} of the parent game object */
	public final Transform getTransform() {
		return parent.getTransform();
	}
}