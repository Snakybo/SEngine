package com.snakybo.sengine.rendering;

import com.snakybo.sengine.resource.Texture;
import com.snakybo.sengine.utils.MappedValues;

public class Material extends MappedValues {
	public Material() {
		super();
		
		addDiffuseTexture(new Texture("default.png"));
		addNormalMap(new Texture("default_normal.jpg"));
		addDispMap(new Texture("default_disp.png"), 0.0f, 0.0f);
		addSpecular(1.0f, 8.0f);
	}
	
	public void addDiffuseTexture(Texture texture) {
		setTexture("diffuse", texture);
	}
	
	public void addNormalMap(Texture texture) {
		setTexture("normalMap", texture);
	}
	
	public void addDispMap(Texture texture, float scale, float offset) {
		float baseBias = scale / 2.0f;
		
		setTexture("dispMap", texture);
		
		setFloat("dispMapScale", scale);
		setFloat("dispMapBias", -baseBias + baseBias * offset);
	}
	
	public void addSpecular(float intensity, float power) {
		setFloat("specularIntensity", intensity);
		setFloat("specularPower", power);
	}
}
