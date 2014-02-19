package com.snakybo.engine.components;

import com.snakybo.engine.core.Vector3f;
import com.snakybo.engine.renderer.ForwardDirectional;

public class DirectionalLight extends BaseLight {
	private Vector3f direction;
	
	public DirectionalLight(Vector3f color, float intensity, Vector3f direction) {
		super(color, intensity);
		
		this.direction = direction.normalize();
		
		setShader(ForwardDirectional.getInstance());
	}
	
	public void setDirection(Vector3f direction) { this.direction = direction.normalize(); }
	
	public Vector3f getDirection() { return direction; }
}
