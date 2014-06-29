package com.snakybo.sengine.components.lighting;

import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.Attenuation;
import com.snakybo.sengine.rendering.Shader;

public class PointLight extends BaseLight {
	private static final int COLOR_DEPTH = 256;
	
	private Attenuation attenuation;
	
	private float range;
	
	public PointLight(Vector3f color, float intensity, Attenuation attenuation) {
		super(color, intensity);
		
		this.attenuation = attenuation;
		
		float a = attenuation.getExponent();
		float b = attenuation.getLinear();
		float c = attenuation.getConstant() - COLOR_DEPTH * getIntensity() * getColor().max();
		
		this.range = (float)((-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a));
		
		setShader(new Shader("forward/point"));
	}
	
	public float getRange() {
		return range;
	}
	
	public void setRange(float range) {
		this.range = range;
	}
	
	public Attenuation getAttenuation() {
		return attenuation;
	}
}
