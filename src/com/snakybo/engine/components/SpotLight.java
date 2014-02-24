package com.snakybo.engine.components;

import com.snakybo.engine.core.Vector3f;
import com.snakybo.engine.renderer.ForwardSpot;

public class SpotLight extends PointLight {
	private Vector3f direction;
	private float cutOff;
	
	public SpotLight(Vector3f color, float intensity, Vector3f attenuation, Vector3f direction, float cutOff) {
		super(color, intensity, attenuation);
		
		this.direction = direction.normalize();
		this.cutOff = cutOff;
		
		setShader(ForwardSpot.getInstance());
	}

	public void setDirection(Vector3f direction) { this.direction = direction.normalize(); }
	public void setCutoff(float cutoff) { this.cutOff = cutoff; }

	public Vector3f getDirection() { return direction; }
	public float getCutoff() { return cutOff; }
}
