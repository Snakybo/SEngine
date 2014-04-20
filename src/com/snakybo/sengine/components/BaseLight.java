package com.snakybo.sengine.components;

import com.snakybo.sengine.core.Component;
import com.snakybo.sengine.core.CoreEngine;
import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.Shader;

/** Base light component extends {@link Component}
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class BaseLight extends Component {
	private Vector3f color;
	
	private Shader shader;
	
	private float intensity;
	
	/** Constructor for the base light
	 * @param color The color of the light
	 * @param intensity The intensity of the light */
	public BaseLight(Vector3f color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
	@Override
	protected void addToEngine(CoreEngine engine) {
		engine.getRenderingEngine().addLight(this);
	}
	
	/** Change the color of the light
	 * @param color The new color of the light */
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	/** Change the shader the light should use
	 * @param shader The new shader */
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
	/** Change the intensity of the light
	 * @param intensity The new intensity */
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	/** @return The color of the light */
	public Vector3f getColor() {
		return color;
	}
	
	/** @return The shader of the light */
	public Shader getShader() {
		return shader;
	}
	
	/** @return The intensity of the light */
	public float getIntensity() {
		return intensity;
	}
}
