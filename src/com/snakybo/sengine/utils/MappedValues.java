package com.snakybo.sengine.utils;

import java.util.HashMap;

import com.snakybo.sengine.resource.Texture;
import com.snakybo.sengine.utils.math.Vector3f;

public abstract class MappedValues
{
	private HashMap<String, Vector3f> vector3fHashMap;
	private HashMap<String, Float> floatHashMap;
	private HashMap<String, Texture> textureHashMap;

	public MappedValues()
	{
		vector3fHashMap = new HashMap<String, Vector3f>();
		floatHashMap = new HashMap<String, Float>();
		textureHashMap = new HashMap<String, Texture>();
	}

	public void setVector3f(String name, Vector3f value)
	{
		vector3fHashMap.put(name, value);
	}

	public void setFloat(String name, float value)
	{
		floatHashMap.put(name, value);
	}

	public void setTexture(String name, Texture value)
	{
		textureHashMap.put(name, value);
	}

	public Vector3f getVector3f(String name)
	{
		Vector3f result = vector3fHashMap.get(name);

		if (result != null)
			return result;

		return new Vector3f(0, 0, 0);
	}

	public float getFloat(String name)
	{
		Float result = floatHashMap.get(name);

		if (result != null)
			return result;

		return 0;
	}

	public Texture getTexture(String name)
	{
		Texture result = textureHashMap.get(name);

		if (result != null)
			return result;

		return Texture.getDefaultTexture();
	}
}
