package com.snakybo.engine.components;

import com.snakybo.engine.core.Vector3f;
import com.snakybo.engine.renderer.ForwardPoint;

public class PointLight extends BaseLight {
	private Vector3f position;
	
	private float constant;
	private float linear;
	private float exponent;
	private float range;
	
	public PointLight(Vector3f color, float intensity, float constant, float linear, float exponent, Vector3f position, float range) {
		super(color, intensity);
		
		this.position = position;
		this.constant = constant;
		this.linear = linear;
		this.exponent = exponent;
		this.range = range;
		
		setShader(ForwardPoint.getInstance());
	}
	
	public void setPosition(Vector3f position) { this.position = position; }
	public void setConstant(float constant) { this.constant = constant; }
	public void setLinear(float linear) { this.linear = linear; }
	public void setExponent(float exponent) { this.exponent = exponent; }
	public void setRange(float range) { this.range = range; }
	
	public Vector3f getPosition() { return position; }
	public float getConstant() { return constant; }
	public float getLinear() { return linear; }
	public float getExponent() { return exponent; }
	public float getRange() { return range; }
}
