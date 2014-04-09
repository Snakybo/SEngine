package com.snakybo.sengine.components;

import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.Shader;

/** Directional light component extends {@link BaseLight}
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class DirectionalLight extends BaseLight {
	/** Constructor for the directional light
	 * @param color The color of the light
	 * @param intensity The intensity of the light */
	public DirectionalLight(Vector3f color, float intensity) {
		super(color, intensity);
		
		setShader(new Shader("forward-directional"));
	}
	
	/** @return The direction of the light */
	public Vector3f getDirection() {
		return getTransform().getRotation().getForward();
	}
}
