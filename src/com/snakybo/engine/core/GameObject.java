package com.snakybo.engine.core;

import java.util.ArrayList;

import com.snakybo.engine.rendering.Shader;

/** @author Kevin Krol
 *  @since Jan 31, 2014 */
public class GameObject {
	private ArrayList<GameObject> children;
	private ArrayList<Component> components;
	
	private Transform transform;
	
	public GameObject() {
		children = new ArrayList<GameObject>();
		components = new ArrayList<Component>();
		
		transform = new Transform();
	}
	
	/** Add a child game object to this game object
	 * @param child The game object */
	public void addChild(GameObject child) {
		children.add(child);
	}
	
	/** Add a component to the game object
	 * @param component The component that extends {@link Component} */
	public void addComponent(Component component) {
		components.add(component);
	}
	
	/** Handle input for the game object
	 * @param delta The delta time */
	public void input(float delta) {
		for(Component component : components)
			component.input(transform, delta);
		
		for(GameObject child : children)
			child.input(delta);
	}
	
	/** Update the game object
	 * @param delta The delta time */
	public void update(float delta) {
		for(Component component : components)
			component.update(transform, delta);
		
		for(GameObject child : children)
			child.update(delta);
	}
	
	/** Render the game object
	 * @param shader The shader the game object has to use */
	public void render(Shader shader) {
		for(Component component : components)
			component.render(transform, shader);
		
		for(GameObject child : children)
			child.render(shader);
	}
	
	/** @return The transformation of the game object */
	public Transform getTransform() {
		return transform;
	}
}
