package com.snakybo.sengine.components;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.resource.Shader;
import com.snakybo.sengine.utils.Color;

/** Base class for every light in the game
 * 
 * <p>This class extends the {@link Component} class</p>
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see Component */
public class Light extends Component {
	private Color color;
	
	private Shader shader;
	
	private float intensity;
	
	/** Constructor for the component
	 * @param color The color of the light
	 * @param intensity The intensity of the light
	 * @see Color*/
	public Light(Color color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
	@Override
	protected void onAddedToScene(RenderingEngine renderingEngine) {
		renderingEngine.addLight(this);
	}
	
	/** Set the shader of the light
	 * @param shader The new shader to use */
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
	/** Set the color of the light
	 * @param color The new color to use */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/** Set the intensity of the light
	 * @param intensity The new intensity */
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	/** @return The shader the light uses
	 * @see Shader */
	public Shader getShader() {
		return shader;
	}
	
	/** @return The color of the light
	 * @see Color */
	public Color getColor() {
		return color;
	}
	
	/** @return The intensity of the light */
	public float getIntensity() {
		return intensity;
	}
}