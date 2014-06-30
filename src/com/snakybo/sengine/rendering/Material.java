package com.snakybo.sengine.rendering;

import java.util.HashMap;

import com.snakybo.sengine.rendering.resourceManagement.MappedValues;

public class Material extends MappedValues {
	private HashMap<String, Texture> textureHashMap;
	
	public Material() {
		super();
		textureHashMap = new HashMap<String, Texture>();

		addDiffuseTexture(new Texture("default.png"));
		addNormalMap(new Texture("default_normal.jpg"));
		addDispMap(new Texture("default_disp.png"), 0.0f, 0.0f);
		addSpecular(1.0f, 8.0f);
	}
	
	public void addDiffuseTexture(Texture texture) {
		addTexture("diffuse", texture);
	}
	
	public void addNormalMap(Texture texture) {
		addTexture("normalMap", texture);
	}
	
	public void addDispMap(Texture texture, float scale, float offset) {
		float baseBias = scale / 2.0f;
		
		addTexture("dispMap", texture);
		
		addFloat("dispMapScale", scale);
		addFloat("dispMapBias", -baseBias + baseBias * offset);
	}
	
	public void addSpecular(float intensity, float power) {
		addFloat("specularIntensity", intensity);
		addFloat("specularPower", power);
	}
	
	public void addTexture(String name, Texture texture) {
		textureHashMap.put(name, texture);
	}
	
	public Texture getTexture(String name) {
		Texture result = textureHashMap.get(name);
		if(result != null)
			return result;
		
		return new Texture("test.png");
	}
}
