package com.snakybo.sengine.core;

import java.util.ArrayList;

import com.snakybo.sengine.components.Transform;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Shader;

public class GameObject {
	private ArrayList<GameObject> children;
	private ArrayList<Component> components;
	
	private Transform transform;
	private SEngine engine;
	
	public GameObject(Component... args) {
		children = new ArrayList<GameObject>();
		components = new ArrayList<Component>();
		transform = new Transform();
		engine = null;
		
		for(int i = 0; i < args.length; i++) {
			components.add(args[i]);
			args[i].setParent(this);
		}
	}
	
	public void addChild(GameObject child) {
		children.add(child);
		child.setEngine(engine);
		child.getTransform().setParent(transform);
	}
	
	public GameObject addComponent(Component component) {
		components.add(component);
		component.setParent(this);
		
		return this;
	}
	
	public void inputAll(float delta) {
		input(delta);
		
		for(GameObject child : children)
			child.inputAll(delta);
	}
	
	public void updateAll(float delta) {
		update(delta);
		
		for(GameObject child : children)
			child.updateAll(delta);
	}
	
	public void renderAll(Shader shader, RenderingEngine renderingEngine) {
		render(shader, renderingEngine);
		
		for(GameObject child : children)
			child.renderAll(shader, renderingEngine);
	}
	
	public void input(float delta) {
		transform.update();
		
		for(Component component : components)
			component.input(delta);
	}
	
	public void update(float delta) {
		for(Component component : components)
			component.update(delta);
	}
	
	public void render(Shader shader, RenderingEngine renderingEngine) {
		for(Component component : components)
			component.render(shader, renderingEngine);
	}
	
	public ArrayList<GameObject> getAllAttached() {
		ArrayList<GameObject> result = new ArrayList<GameObject>();
		
		for(GameObject child : children)
			result.addAll(child.getAllAttached());
		
		result.add(this);
		return result;
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public void setEngine(SEngine engine) {
		if(this.engine != engine) {
			this.engine = engine;
			
			for(Component component : components)
				component.addToEngine(engine);
			
			for(GameObject child : children)
				child.setEngine(engine);
		}
	}
}
