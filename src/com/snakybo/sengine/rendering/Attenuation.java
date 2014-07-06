package com.snakybo.sengine.rendering;

import com.snakybo.sengine.utils.math.Vector3f;

public class Attenuation extends Vector3f {
	public Attenuation(float constant, float linear, float exponent) {
		super(constant, linear, exponent);
	}
	
	public void setConstant(float constant) {
		x = constant;
	}
	
	public void setLinear(float linear) {
		y = linear;
	}
	
	public void setExponent(float exponent) {
		z = exponent;
	}
	
	public float getConstant() {
		return x;
	}
	
	public float getLinear() {
		return y;
	}
	
	public float getExponent() {
		return z;
	}
}
