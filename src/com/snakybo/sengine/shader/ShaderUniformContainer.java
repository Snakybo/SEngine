package com.snakybo.sengine.shader;

import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.lighting.AmbientLight;
import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.resource.texture.Texture;

/**
 * @author Kevin
 * @since Dec 26, 2015
 */
public abstract class ShaderUniformContainer
{
	private static Map<String, Object> uniformData = new HashMap<String, Object>();
	
	static
	{
		set("ambient", AmbientLight.getAmbientColor());		
		set("sampler_diffuse", 0);
		set("sampler_normalMap", 1);
		set("sampler_dispMap", 2);
		set("sampler_shadowMap", 3);
		set("sampler_filterTexture", 0);
	}
	
	public static void set(String key, Object value)
	{
		uniformData.put(key, value);
	}
	
	public static <T> T get(Class<T> type, String name)
	{
		if(!uniformData.containsKey(name))
		{
			throw new IllegalArgumentException("No uniform data with the name: " + name + " found.");
		}
		
		return type.cast(uniformData.get(name));
	}
	
	public static Texture getTexture(String name)
	{
		return get(Texture.class, name);
	}
	
	public static Vector3f getVector3f(String name)
	{
		return get(Vector3f.class, name);
	}
	
	public static Vector2f getVector2f(String name)
	{
		return get(Vector2f.class, name);
	}
	
	public static float getFloat(String name)
	{
		return get(Float.class, name);
	}
	
	public static int getInteger(String name)
	{
		return get(Integer.class, name);
	}
	
	public static int getSampler(String name)
	{
		return getInteger("sampler_" + name);
	}
}
