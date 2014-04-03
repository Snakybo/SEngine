package com.snakybo.engine.components.light;

import com.snakybo.engine.math.Vector3f;
import com.snakybo.engine.renderer.Shader;

/** @author Kevin Krol */
public class SpotLight extends PointLight {
	private float cutOff;
	
	/** Create a new spot light
	 * @param color The color of the light
	 * @param intensity The intensity of the light
	 * @param attenuation The attenuation of the light
	 * @param cutOff The point where the light should be cut off */
	public SpotLight(Vector3f color, float intensity, Vector3f attenuation, float cutOff) {
		super(color, intensity, attenuation);
		
		this.cutOff = cutOff;
		
		setShader(new Shader("forward/spot"));
	}
	
	/** Set the point where the light should be cut off */
	public void setCutOff(float cutOff) {
		this.cutOff = cutOff;
	}
	
	/** @return The direction of the directional light */
	public Vector3f getDirection() {
		return getTransform().getTransformedRotation().front();
	}
	
	/** @return The cut off of light */
	public float getCutoff() {
		return cutOff;
	}
}
