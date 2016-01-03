package com.snakybo.sengine.resource.material;

import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.resource.texture.Texture;

public final class Material
{
	private Map<String, Object> data;
	
	public Material()
	{
		data = new HashMap<String, Object>();
	}

	public void set(String name, Object value)
	{
		data.put(name, value);
	}

	public <T> T get(Class<T> type, String name)
	{
		if(!data.containsKey(name))
		{
			throw new IllegalArgumentException("No data with the name: " + name + " found.");
		}
		
		return type.cast(data.get(name));
	}
	
	public static Material createDefault()
	{
		return createDefault(new Texture("default/diffuse.png"), 4, 8);
	}
	
	public static Material createDefault(Texture diffuse, float specularIntensity, float specularPower)
	{
		return createDefault(diffuse, specularIntensity, specularPower, new Texture("default/normal.png"));
	}
	
	public static Material createDefault(Texture diffuse, float specularIntensity, float specularPower, Texture normal)
	{
		return createDefault(diffuse, specularIntensity, specularPower, normal, new Texture("default/displacement.png"), 0, 0);
	}
	
	public static Material createDefault(Texture diffuse, float specularIntensity, float specularPower, Texture normal, Texture disp, float dispMapScale, float dispMapOffset)
	{
		Material result = new Material();
		
		result.set("diffuse", diffuse);
		result.set("normalMap", normal);
		result.set("dispMap", disp);
		
		result.set("specularIntensity", specularIntensity);
		result.set("specularPower", specularPower);
		
		float baseBias = dispMapScale / 2f;
		result.set("dispMapScale", dispMapScale);
		result.set("dispMapBias", -baseBias + baseBias * dispMapOffset);
		
		return result;
	}
}