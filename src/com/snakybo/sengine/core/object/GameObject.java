package com.snakybo.sengine.core.object;

import java.util.ArrayList;
import java.util.List;

import com.snakybo.sengine.resource.Shader;

/** The game object class, every object in the scene is a game object
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class GameObject {
	private ArrayList<GameObject> children;
	private ArrayList<Component> components;
	
	private Transform transform;
	
	/** Constructor for the game object
	 * @param args Optionally pass in components */
	public GameObject(Component... args) {
		children = new ArrayList<GameObject>();
		components = new ArrayList<Component>();
		transform = new Transform();
		
		for(int i = 0; i < args.length; i++)
			addComponent(args[i]);
	}
	
	/** Set a game object as a child of this game object, the child object will also receive any
	 * transformation this game object receives
	 * @param child The game object to set as a child */
	public void addChild(GameObject child) {
		children.add(child);
		child.getTransform().setParent(transform);
	}
	
	/** Add a {@link Component} to the game object.
	 * @param component The component to add to the game object
	 * @return This game object, used for chaining
	 * @see Component */
	public GameObject addComponent(Component component) {
		components.add(component);
		component.setParent(this);
		
		return this;
	}
	
	/** Handle input for the game object, all it's components and it's children
	 * @param delta The delta time */
	public void inputAll(double delta) {
		input(delta);
		
		for(GameObject child : children)
			child.inputAll(delta);
	}
	
	/** Update the game object, all it's components and it's children
	 * @param delta The delta time */
	public void updateAll(double delta) {
		update(delta);
		
		for(GameObject child : children)
			child.updateAll(delta);
	}
	
	/** Render the game object, all it's components and it's children
	 * @param delta The delta time */
	public void renderAll(Shader shader) {
		render(shader);
		
		for(GameObject child : children)
			child.renderAll(shader);
	}
	
	/** Handle input for every component of this game object
	 * @param delta The delta time */
	public void input(double delta) {
		transform.update();
		
		for(Component component : components)
			component.input(delta);
	}
	
	/** Update every component of the game object
	 * @param delta The delta time */
	public void update(double delta) {
		for(Component component : components)
			component.update(delta);
	}
	
	/** Render every component of the game object
	 * @param delta The delta time */
	public void render(Shader shader) {
		for(Component component : components)
			component.render(shader);
	}
	
	/** This method creates an array, containing all the children of this game object, and the
	 * children of every child
	 * @return An array containing every game object that's attached to this one */
	public GameObject[] getAllAttached() {
		List<GameObject> result = getAllAttachedInternal();
		
		return result.toArray(new GameObject[result.size()]);
	}
	
	/** @return The {@link Transform} of the game object */
	public Transform getTransform() {
		return transform;
	}
	
	/** This method is used internally by {@link #getAllAttached()}. But instead of an array it
	 * returns a list
	 * @return A list containing every game object attached to this one */
	private ArrayList<GameObject> getAllAttachedInternal() {
		ArrayList<GameObject> result = new ArrayList<GameObject>();
		
		for(GameObject child : children)
			result.addAll(child.getAllAttachedInternal());
		
		result.add(this);
		return result;
	}
}
