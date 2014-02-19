package com.snakybo.engine.components;

import com.snakybo.engine.core.Transform;
import com.snakybo.engine.renderer.Renderer;
import com.snakybo.engine.renderer.Shader;

/** @author Kevin Krol
 *  @since Jan 31, 2014 */
public abstract class Component {
	/** Handle input for the component
	 * @param transform The transformation of the game object
	 * @param delta The delta time */
	public void input(Transform transform, float delta) { }
	
	/** Update the component
	 * @param transform The transformation of the game object
	 * @param delta The delta time */
	public void update(Transform transform, float delta) { }
	
	/** Render the component
	 * @param transform The transformation of the game object
	 * @param shader The shader the game object is using */
	public void render(Transform transform, Shader shader) { }
	
	public void addToRenderer(Renderer renderer) {
		
	}
}
