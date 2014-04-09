package com.snakybo.sengine.components;

import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.Attenuation;
import com.snakybo.sengine.rendering.Shader;

/** Point light component extends {@link BaseLight}
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class PointLight extends BaseLight {
	private static final int COLOR_DEPTH = 256;
	
	private Attenuation attenuation;
	
	private float range;
	
	/** Constructor for the point light
	 * @param color The color of the light
	 * @param intensity The intensity of the light
	 * @param attenuation The Attenuation of the light */
	public PointLight(Vector3f color, float intensity, Attenuation attenuation) {
		super(color, intensity);
		
		final float a = attenuation.getExponent();
		final float b = attenuation.getLinear();
		final float c = attenuation.getConstant() - COLOR_DEPTH * getIntensity() * getColor().max();
		
		this.attenuation = attenuation;
		this.range = (float)((-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a));
		
		setShader(new Shader("forward-point"));
	}
	
	/** Set the range of the point light
	 * @param range The new range */
	public void setRange(float range) {
		this.range = range;
	}
	
	/** @return The Attenuation of the point light */
	public Attenuation getAttenuation() {
		return attenuation;
	}
	
	/** @return The range of the point light */
	public float getRange() {
		return range;
	}
}
