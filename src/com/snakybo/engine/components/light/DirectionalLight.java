package com.snakybo.engine.components.light;

import com.snakybo.engine.math.Vector3f;
import com.snakybo.engine.renderer.Shader;

/** @author Kevin Krol */
public class DirectionalLight extends BaseLight {
	/** Create a new directional light
	 * @param color The color of the light
	 * @param intensity The intensity of the light */
	public DirectionalLight(Vector3f color, float intensity) {
		super(color, intensity);
		
		setShader(new Shader("forward/directional"));
	}
	
	/** @return The direction of the directional light */
	public Vector3f getDirection() {
		return getTransform().getTransformedRotation().front();
	}
}
