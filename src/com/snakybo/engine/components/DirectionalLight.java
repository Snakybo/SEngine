package com.snakybo.engine.components;

import com.snakybo.engine.core.Vector3f;
import com.snakybo.engine.renderer.ForwardDirectional;

/** @author Kevin Krol */
public class DirectionalLight extends BaseLight {
	public DirectionalLight(Vector3f color, float intensity) {
		super(color, intensity);
		
		setShader(ForwardDirectional.getInstance());
	}
	
	public Vector3f getDirection() {
		return getTransform().getTransformedRotation().front();
	}
}
