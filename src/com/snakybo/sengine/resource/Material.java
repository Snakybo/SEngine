package com.snakybo.sengine.resource;

import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.resource.management.MaterialData;
import com.snakybo.sengine.utils.MappedValues;
import com.snakybo.sengine.utils.math.Vector3f;

public class Material extends MappedValues {
	public static final String DIFFUSE = "diffuse";
	public static final String NORMAL_MAP = "normalMap";
	public static final String DISP_MAP = "dispMap";
	
	public static final String SPECULAR_INTENSITY = "specularIntensity";
	public static final String SPECULAR_POWER = "specularPower";
	
	public static final String DISP_MAP_SCALE = "dispMapScale";
	public static final String DISP_MAP_BIAS = "dispMapBias";
	
	private static final Texture DEFAULT_NORMAL_TEXTURE = new Texture("internal/default_normal.png");
	private static final Texture DEFAULT_DISP_TEXTURE = new Texture("internal/default_disp.png");
	
	private static Map<String, MaterialData> resourceMap = new HashMap<String, MaterialData>();
	
	private MaterialData resource;
	private String materialName;
	
	public Material() {
		this("");
	}
	
	public Material(String materialName) {
		this.materialName = materialName;
		
		if(!materialName.isEmpty()) {
			MaterialData existingResource = resourceMap.get(materialName);
			
			if(existingResource == null) {
				System.err.println("Error: Material " + materialName + " has not been initialized!");
				System.exit(1);
			}
			
			resource = existingResource;
			resource.addReference();
		}
	}
	
	public Material(String materialName, Texture diffuse, float specularIntensity, float specularPower) {		
		this(materialName, diffuse, specularIntensity, specularPower, new Texture(DEFAULT_NORMAL_TEXTURE));
	}
	
	public Material(String materialName, Texture diffuse, float specularIntensity, float specularPower, Texture normalMap) {
		this(materialName, diffuse, specularIntensity, specularPower, normalMap, new Texture(DEFAULT_DISP_TEXTURE));
	}
	
	public Material(String materialName, Texture diffuse, float specularIntensity, float specularPower, Texture normalMap, Texture dispMap) {
		this(materialName, diffuse, specularIntensity, specularPower, normalMap, dispMap, 0.0f);
	}
	
	public Material(String materialName, Texture diffuse, float specularIntensity, float specularPower, Texture normalMap, Texture dispMap, float dispMapScale) {
		this(materialName, diffuse, specularIntensity, specularPower, normalMap, dispMap, dispMapScale, 0.0f);
	}
	
	public Material(String materialName, Texture diffuse, float specularIntensity, float specularPower, Texture normalMap, Texture dispMap, float dispMapScale, float dispMapOffset) {
		this.materialName = materialName;
		
		resource = new MaterialData();
		resourceMap.put(materialName, resource);
		
		if(diffuse == null)
			diffuse = Texture.getDefaultTexture();
		
		if(normalMap == null)
			normalMap = DEFAULT_NORMAL_TEXTURE;
		
		if(dispMap == null)
			dispMap = DEFAULT_DISP_TEXTURE;
		
		resource.setTexture(DIFFUSE, diffuse);
		resource.setTexture(NORMAL_MAP, normalMap);
		resource.setTexture(DISP_MAP, dispMap);
		
		resource.setFloat(SPECULAR_INTENSITY, specularIntensity);
		resource.setFloat(SPECULAR_POWER, specularPower);
		
		float baseBias = dispMapScale / 2.0f;
		resource.setFloat(DISP_MAP_SCALE, dispMapScale);
		resource.setFloat(DISP_MAP_BIAS, -baseBias + baseBias * dispMapOffset);
	}
	
	public Material(Material other) {
		materialName = other.materialName;
		resource = other.resource;
		
		resource.addReference();
	}
	
	@Override
	protected void finalize() throws Throwable {
		try {
			if(resource.removeReference() && !materialName.isEmpty())
				resourceMap.remove(materialName);
		} finally {
			super.finalize();
		}
	}
	
	public void setFloat(String name, float value) {
		resource.setFloat(name, value);
	}
	
	public void setVector3f(String name, Vector3f value) {
		resource.setVector3f(name, value);
	}
	
	public void setTexture(String name, Texture value) {
		resource.setTexture(name, value);
	}
	
	public float getFloat(String name) {
		return resource.getFloat(name);
	}
	
	public Vector3f getVector3f(String name) {
		return resource.getVector3f(name);
	}
	
	public Texture getTexture(String name) {
		return resource.getTexture(name);
	}
	
	public static Texture getDefaultNormalMap() {
		return DEFAULT_NORMAL_TEXTURE;
	}
	
	public static Texture getDefaultDiffuseMap() {
		return DEFAULT_DISP_TEXTURE;
	}
}