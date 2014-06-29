package com.snakybo.sengine.core;

import com.snakybo.sengine.rendering.RenderingEngine;

public abstract class Game {
	private GameObject root;
	
	public void init() {}
	
	public void input(float delta) {
		getRootObject().inputAll(delta);
	}
	
	public void update(float delta) {
		getRootObject().updateAll(delta);
	}
	
	public void render(RenderingEngine renderingEngine) {
		renderingEngine.render(getRootObject());
	}
	
	public void addObject(GameObject object) {
		getRootObject().addChild(object);
	}
	
	private GameObject getRootObject() {
		if(root == null)
			root = new GameObject();
		
		return root;
	}
	
	public void setEngine(SEngine engine) {
		getRootObject().setEngine(engine);
	}
}
