package com.snakybo.engine.components;

import com.snakybo.engine.core.Vector3f;
import com.snakybo.engine.renderer.Renderer;
import com.snakybo.engine.renderer.Shader;

public class BaseLight extends Component {
	private Vector3f color;
	private float intensity;
	
	private Shader shader;
	
	public BaseLight(Vector3f color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
	@Override
	public void addToRenderer(Renderer renderer) {
		renderer.addLight(this);
	}
	
	public void setShader(Shader shader) { this.shader = shader; }
	public void setColor(Vector3f color) { this.color = color; }
	public void setIntensity(float intensity) {	this.intensity = intensity;	}
	
	public Shader getShader() { return shader; }
	public Vector3f getColor() { return color; }
	public float getIntensity() { return intensity; }
}
