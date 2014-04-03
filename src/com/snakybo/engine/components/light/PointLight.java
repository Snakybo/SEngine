package com.snakybo.engine.components.light;

import com.snakybo.engine.math.Vector3f;
import com.snakybo.engine.renderer.Shader;

/** @author Kevin Krol */
public class PointLight extends BaseLight {
	private static final int COLOR_DEPTH = 256;
	
	private Vector3f attenuation;
	private float range;
	
	/** Create a new point light
	 * @param color The color of the light
	 * @param intensity The intensity of the light
	 * @param attenuation The attenuation of the light */
	public PointLight(Vector3f color, float intensity, Vector3f attenuation) {
		super(color, intensity);
		
		this.attenuation = attenuation;
		
		float a = attenuation.getZ();
		float b = attenuation.getY();
		float c = attenuation.getX() - COLOR_DEPTH * getIntensity() * getColor().max();
		
		this.range = (float)(-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
		
		setShader(new Shader("forward/point"));
	}
	
	/** Set the constant value of the attenuation */
	public void setConstant(float constant) {
		this.attenuation.setX(constant);
	}
	
	/** Set the linear value of the attenuation */
	public void setLinear(float linear) {
		this.attenuation.setY(linear);
	}
	
	/** Set the exponent value of the attenuation */
	public void setExponent(float exponent) {
		this.attenuation.setZ(exponent);
	}
	
	/** Set the range of the light */
	public void setRange(float range) {
		this.range = range;
	}
	
	/** @return The constant value of the attenuation */
	public float getConstant() {
		return attenuation.getX();
	}
	
	/** @return The linear value of the attenuation */
	public float getLinear() {
		return attenuation.getY();
	}
	
	/** @return The exponent value of the attenuation */
	public float getExponent() {
		return attenuation.getZ();
	}
	
	/** @return The range of the light */
	public float getRange() {
		return range;
	}
}
