package com.snakybo.sengine.core;

import com.snakybo.sengine.components.Transform;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Shader;

public abstract class Component {
	private GameObject parent;
	
	public void input(float delta) {}
	
	public void update(float delta) {}
	
	public void render(Shader shader, RenderingEngine renderingEngine) {}
	
	public void setParent(GameObject parent) {
		this.parent = parent;
	}
	
	public Transform getTransform() {
		return parent.getTransform();
	}
	
	public void addToEngine(SEngine engine) {}
}
