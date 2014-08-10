package com.snakybo.sengine.components.lighting;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.resource.Shader;
import com.snakybo.sengine.utils.Color;
import com.snakybo.sengine.utils.math.Matrix4f;

/** Base class for every light in the game
 * 
 * <p>This class extends the {@link Component} class</p>
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see Component */
public class BaseLight extends Component {
	private Color color;
	
	private Shader shader;
	private ShadowInfo shadowInfo;
	
	private float intensity;
	
	/** Constructor for the component
	 * @param color The color of the light
	 * @param intensity The intensity of the light
	 * @see Color*/
	public BaseLight(Color color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}
	
	@Override
	protected void onAddedToScene(RenderingEngine renderingEngine) {
		renderingEngine.addLight(this);
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
	
	/** Set the shader of the light
	 * @param shader The new shader to use */
	protected void setShader(Shader shader) {
		this.shader = shader;
	}
	
	/** Set the shadow info of the light
	 * @param shadowInfo The new shadow information */
	protected void setShadowInfo(ShadowInfo shadowInfo) {
		this.shadowInfo = shadowInfo;
	}
	
	/** @return The shader the light uses
	 * @see Shader */
	public Shader getShader() {
		return shader;
	}
	
	/** @return The shadow information of the light */
	public ShadowInfo getShadowInfo() {
		return shadowInfo;
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
	
	// TODO: JavaDocs for the ShadowInfo class
	public class ShadowInfo {
		private Matrix4f projection;
		
		public ShadowInfo(Matrix4f projection) {
			this.projection = projection;
		}
		
		public Matrix4f getProjection() {
			return projection;
		}
	}
}
