package com.snakybo.sengine.components;

import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.Attenuation;
import com.snakybo.sengine.rendering.Shader;

/** Spot light component extends {@link BaseLight}
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class SpotLight extends PointLight {
	private float cutoff;
	
	/** Constructor for the spot light
	 * @param color The color of the light
	 * @param intensity The intensity of the light
	 * @param attenuation The Attenuation of the light
	 * @param cutoff The point at which the light cuts off */
	public SpotLight(Vector3f color, float intensity, Attenuation attenuation, float cutoff) {
		super(color, intensity, attenuation);
		
		this.cutoff = cutoff;
		
		setShader(new Shader("forward-spot"));
	}
	
	/** Set the cutoff of the light
	 * @param cutoff The new range at which the light cuts off */
	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
	}
	
	/** @return The direction of the spot light */
	public Vector3f getDirection() {
		return getTransform().getRotation().getForward();
	}
	
	/** @return The cutoff of the spot light */
	public float getCutoff() {
		return cutoff;
	}
}
