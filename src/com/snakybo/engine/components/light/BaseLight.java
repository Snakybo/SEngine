package com.snakybo.engine.components.light;

import com.snakybo.engine.components.Component;
import com.snakybo.engine.math.Vector3f;
import com.snakybo.engine.renderer.Renderer;
import com.snakybo.engine.renderer.Shader;

/** @author Kevin Krol */
public class BaseLight extends Component {
	private Vector3f color;
	private float intensity;
	
	private Shader shader;
	
	/** Create a new base light
	 * @param color The color of the light
	 * @param intensity The intensity of the light */
	public BaseLight(Vector3f color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
	@Override
	public void addToRenderer(Renderer renderer) {
		renderer.addLight(this);
	}
	
	/** Change which shader the light should use */
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
	/** Set the color of the light */
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	/** Set the intensity of the light */
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	/** @return The shader the light uses */
	public Shader getShader() {
		return shader;
	}
	
	/** @return The color of the light */
	public Vector3f getColor() {
		return color;
	}
	
	/** @return The intensity of the light */
	public float getIntensity() {
		return intensity;
	}
}
