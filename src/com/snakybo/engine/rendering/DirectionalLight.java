package com.snakybo.engine.rendering;

import com.snakybo.engine.core.Vector3f;

public class DirectionalLight {
	private BaseLight base;
	private Vector3f direction;
	
	public DirectionalLight(BaseLight base, Vector3f direction) {
		this.base = base;
		this.direction = direction.normalize();
	}
	
	public void setBase(BaseLight base) { this.base = base; }
	public void setDirection(Vector3f direction) { this.direction = direction.normalize(); }
	
	public BaseLight getBase() { return base; }
	public Vector3f getDirection() { return direction; }
}
