package com.snakybo.sengine.components.lighting;

import com.snakybo.sengine.core.Component;
import com.snakybo.sengine.core.SEngine;
import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.Shader;

public class BaseLight extends Component {	
	private Vector3f color;
	
	private Shader shader;
	
	private float intensity;
	
	public BaseLight(Vector3f color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
	@Override
	public void addToEngine(SEngine engine) {
		engine.getRenderingEngine().addLight(this);
	}
	
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
	public Shader getShader() {
		return shader;
	}
	
	public Vector3f getColor() {
		return color;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public float getIntensity() {
		return intensity;
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
}
