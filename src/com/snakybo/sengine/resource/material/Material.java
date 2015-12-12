package com.snakybo.sengine.resource.material;

import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.resource.texture.Texture;
import com.snakybo.sengine.utils.IDataContainer;

public class Material implements IDataContainer
{
	private Map<String, Object> data;
	
	public Material()
	{
		data = new HashMap<String, Object>();
	}
	
	public Material(Texture diffuse, float specularIntensity, float specularPower)
	{
		this(diffuse, specularIntensity, specularPower, new Texture());
	}
	
	public Material(Texture diffuse, float specularIntensity, float specularPower, Texture normal)
	{
		this(diffuse, specularIntensity, specularPower, normal, new Texture(), 0, 0);
	}
	
	public Material(Texture diffuse, float specularIntensity, float specularPower, Texture normal, Texture disp, float dispMapScale, float dispMapOffset)
	{
		data = new HashMap<String, Object>();
		
		set("diffuse", diffuse);
		set("normalMap", normal);
		set("dispMap", disp);
		
		set("specularIntensity", specularIntensity);
		set("specularPower", specularPower);
		
		float baseBias = dispMapScale / 2f;
		set("dispMapScale", dispMapScale);
		set("dispMapBias", -baseBias + baseBias * dispMapOffset);
	}

	@Override
	public void set(String name, Object value)
	{
		data.put(name, value);
	}

	@Override
	public <T> T get(Class<T> type, String name)
	{
		if(!data.containsKey(name))
		{
			throw new IllegalArgumentException("No data with the name: " + name + " found.");
		}
		
		return type.cast(data.get(name));
	}
}