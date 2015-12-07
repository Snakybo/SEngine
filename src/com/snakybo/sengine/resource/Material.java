package com.snakybo.sengine.resource;

import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.resource.management.MaterialData;

public class Material
{
	public static final String DIFFUSE = "diffuse";
	public static final String NORMAL_MAP = "normalMap";
	public static final String DISP_MAP = "dispMap";
	public static final String SHADOW_MAP = "shadowMap";

	public static final String SPECULAR_INTENSITY = "specularIntensity";
	public static final String SPECULAR_POWER = "specularPower";

	public static final String DISP_MAP_SCALE = "dispMapScale";
	public static final String DISP_MAP_BIAS = "dispMapBias";

	private static Map<String, MaterialData> resourceMap = new HashMap<String, MaterialData>();

	private MaterialData resource;
	private String materialName;

	public Material()
	{
		this("");
	}

	public Material(String materialName)
	{
		this.materialName = materialName;

		if (!materialName.isEmpty())
		{
			MaterialData existingResource = resourceMap.get(materialName);

			if (existingResource == null)
			{
				System.err.println("Error: Material " + materialName + " has not been initialized!");
				System.exit(1);
			}

			resource = existingResource;
			resource.addReference();
		}
	}

	public Material(String materialName, Texture diffuse, float specularIntensity, float specularPower)
	{
		this(materialName, diffuse, specularIntensity, specularPower, new Texture());
	}

	public Material(String materialName, Texture diffuse, float specularIntensity, float specularPower,
			Texture normalMap)
	{
		this(materialName, diffuse, specularIntensity, specularPower, normalMap, new Texture());
	}

	public Material(String materialName, Texture diffuse, float specularIntensity, float specularPower,
			Texture normalMap, Texture dispMap)
	{
		this(materialName, diffuse, specularIntensity, specularPower, normalMap, dispMap, 0.0f);
	}

	public Material(String materialName, Texture diffuse, float specularIntensity, float specularPower, Texture normalMap, Texture dispMap, float dispMapScale)
	{
		this(materialName, diffuse, specularIntensity, specularPower, normalMap, dispMap, dispMapScale, 0.0f);
	}

	public Material(String materialName, Texture diffuse, float specularIntensity, float specularPower, Texture normalMap, Texture dispMap, float dispMapScale, float dispMapOffset)
	{
		this.materialName = materialName;

		resource = new MaterialData();
		resourceMap.put(materialName, resource);
		
		set(DIFFUSE, diffuse);
		set(NORMAL_MAP, normalMap);
		set(DISP_MAP, dispMap);

		set(SPECULAR_INTENSITY, specularIntensity);
		set(SPECULAR_POWER, specularPower);

		float baseBias = dispMapScale / 2.0f;
		set(DISP_MAP_SCALE, dispMapScale);
		set(DISP_MAP_BIAS, -baseBias + baseBias * dispMapOffset);
	}

	public Material(Material other)
	{
		materialName = other.materialName;
		resource = other.resource;

		resource.addReference();
	}
	
	public void destroy()
	{
		if(resource.removeReference() && !materialName.isEmpty())
		{
			resourceMap.remove(materialName);
		}
	}

	public void set(String name, Object value)
	{
		resource.set(name, value);
	}
	
	public <T extends Object> T get(Class<T> type, String name)
	{
		return resource.get(type, name);
	}
}