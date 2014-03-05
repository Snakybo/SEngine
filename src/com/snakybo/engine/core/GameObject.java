package com.snakybo.engine.core;

import java.util.ArrayList;

import com.snakybo.engine.components.Component;
import com.snakybo.engine.renderer.Renderer;
import com.snakybo.engine.renderer.Shader;

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
		child.getTransform().setParent(transform);
		
		children.add(child);
	}
	
	/** Add a component to the game object
	 * @param component The component that extends {@link Component} */
	public GameObject addComponent(Component component) {
		component.setGameObject(this);
		
		components.add(component);
		
		return this;
	}
	
	/** Handle input for the game object
	 * @param delta The delta time */
	public void input(float delta) {
		transform.update();
		
		for(Component component : components)
			component.input(delta);
		
		for(GameObject child : children)
			child.input(delta);
	}
	
	/** Update the game object
	 * @param delta The delta time */
	public void update(float delta) {
		for(Component component : components)
			component.update(delta);
		
		for(GameObject child : children)
			child.update(delta);
	}
	
	/** Render the game object
	 * @param shader The shader the game object has to use */
	public void render(Shader shader, Renderer renderer) {
		for(Component component : components)
			component.render(shader, renderer);
		
		for(GameObject child : children)
			child.render(shader, renderer);
	}
	
	/** Add the game object to the rendering engine
	 * @param renderer The active rendering engine */
	public void addToRenderer(Renderer renderer) {
		for(Component component : components)
			component.addToRenderer(renderer);
		
		for(GameObject child : children)
			child.addToRenderer(renderer);
	}
	
	/** @return The transformation of the game object */
	public Transform getTransform() {
		return transform;
	}
}
