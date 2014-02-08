package com.snakybo.engine.rendering;

import com.snakybo.engine.core.Vector3f;

public class SpotLight {
	private PointLight pointLight;
	private Vector3f direction;
	private float cutOff;
	
	public SpotLight(PointLight pointLight, Vector3f direction, float cutOff) {
		this.pointLight = pointLight;
		this.direction = direction.normalize();
		this.cutOff = cutOff;
	}

	public void setPointLight(PointLight pointLight) {this.pointLight = pointLight; }
	public void setDirection(Vector3f direction) { this.direction = direction.normalize(); }
	public void setCutoff(float cutoff) { this.cutOff = cutoff; }

	public PointLight getPointLight() { return pointLight; }
	public Vector3f getDirection() { return direction; }
	public float getCutoff() { return cutOff; }
}
