package com.snakybo.sengine.core;

import java.util.ArrayList;

import com.snakybo.sengine.components.Component;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Shader;

/** Game object class
 * 
 * <p>
 * Every object in the game is a game object
 * </p>
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class GameObject {
	private ArrayList<GameObject> children;
	private ArrayList<Component> components;
	
	private Transform transform;
	private CoreEngine engine;
	
	/** Constructor for the game object
	 * @param componentsToAdd A list of components to add to the game object */
	public GameObject(Component... componentsToAdd) {
		children = new ArrayList<GameObject>();
		components = new ArrayList<Component>();
		transform = new Transform();
		engine = null;
		
		for(Component component : componentsToAdd)
			addComponent(component);
	}
	
	/** Add a child to the game object
	 * @param child The child */
	public void addChild(GameObject child) {
		children.add(child);
		child.setEngine(engine);
		child.getTransform().setParent(transform);
	}
	
	/** Add a component to the game object
	 * @param component The component to add to the game object
	 * @return This game object, used for chaining */
	public GameObject addComponent(Component component) {
		components.add(component);
		component.setParent(this);
		
		return this;
	}
	
	/** Handle input for this game object and all of it's children
	 * @param delta The current delta time */
	public void inputAll(float delta) {
		input(delta);
		
		for(GameObject child : children)
			child.inputAll(delta);
	}
	
	/** Update this game object and all of it's children
	 * @param delta The current delta time */
	public void updateAll(float delta) {
		update(delta);
		
		for(GameObject child : children)
			child.updateAll(delta);
	}
	
	/** Render this game object and all of it's children
	 * @param shader The active shader
	 * @param renderingEngine The rendering engine */
	public void renderAll(Shader shader, RenderingEngine renderingEngine) {
		render(shader, renderingEngine);
		
		for(GameObject child : children)
			child.renderAll(shader, renderingEngine);
	}
	
	/** Handle input for the game object and it's components
	 * @param delta The current delta time */
	public void input(float delta) {
		transform.update();
		
		for(Component component : components)
			component.input(delta);
	}
	
	/** Update the game object and it's components
	 * @param delta The current delta time */
	public void update(float delta) {
		for(Component component : components)
			component.update(delta);
	}
	
	/** Render the game object and it's components
	 * @param shader The active shader
	 * @param renderingEngine The rendering engine */
	public void render(Shader shader, RenderingEngine renderingEngine) {
		for(Component component : components)
			component.render(shader, renderingEngine);
	}
	
	/** Set the core engine of the game object
	 * @param engine The core engine */
	public void setEngine(CoreEngine engine) {
		if(this.engine != engine) {
			this.engine = engine;
			
			for(Component component : components)
				component.addToEngine(engine);
			
			for(GameObject child : children)
				child.setEngine(engine);
		}
	}
	
	/** @return An list of all the game objects connected to this one */
	public ArrayList<GameObject> getAllAttached() {
		ArrayList<GameObject> result = new ArrayList<GameObject>();
		
		for(GameObject child : children)
			result.addAll(child.getAllAttached());
		
		result.add(this);
		
		return result;
	}
	
	/** @return The transform of the game object */
	public Transform getTransform() {
		return transform;
	}
}
