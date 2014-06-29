package com.snakybo.sengine.components.lighting;

import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.Shader;

public class DirectionalLight extends BaseLight {
	public DirectionalLight(Vector3f color, float intensity) {
		super(color, intensity);
		
		setShader(new Shader("forward/directional"));
	}
	
	public Vector3f getDirection() {
		return getTransform().getTransformedRotation().getForward();
	}
}
