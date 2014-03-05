package com.snakybo.engine.components;

import com.snakybo.engine.core.Vector3f;
import com.snakybo.engine.renderer.ForwardSpot;

public class SpotLight extends PointLight {
	private float cutOff;
	
	public SpotLight(Vector3f color, float intensity, Vector3f attenuation, float cutOff) {
		super(color, intensity, attenuation);
		
		this.cutOff = cutOff;
		
		setShader(ForwardSpot.getInstance());
	}

	public void setCutoff(float cutoff) { this.cutOff = cutoff; }

	public Vector3f getDirection() { return getTransform().getTransformedRotation().forward(); }
	public float getCutoff() { return cutOff; }
}
