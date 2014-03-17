package com.snakybo.engine.renderer;

import java.util.HashMap;
import java.util.Map;

import com.snakybo.engine.core.Vector3f;

/** @author Kevin Krol */
public class Material {
	private Map<String, Texture> texturesHashMap;
	private Map<String, Vector3f> vector3fHashMap;
	private Map<String, Float> floatHashMap;
	
	public Material() {
		texturesHashMap = new HashMap<String, Texture>();
		vector3fHashMap = new HashMap<String, Vector3f>();
		floatHashMap = new HashMap<String, Float>();
	}
	
	/** Add a texture to the material
	 * @param name The name of the texture, used to get the texture
	 * @param value The texture */
	public void addTexture(String name, Texture value) {
		texturesHashMap.put(name, value);
	}
	
	/** Add a vector to the material
	 * @param name The name of the vector, used to get the vector
	 * @param value The vector */
	public void addVector3f(String name, Vector3f value) {
		vector3fHashMap.put(name, value);
	}
	
	/** Add a float to the material
	 * @param name The name of the float, used to get the float
	 * @param value The float */
	public void addFloat(String name, float value) {
		floatHashMap.put(name, value);
	}
	
	/** @return The texture with the given name
	 * @param name The name of the texture */
	public Texture getTexture(String name) {
		Texture result = texturesHashMap.get(name);
		
		if(result != null)
			return result;
		
		return new Texture(Texture.DEFAULT.getTextureId());
	}
	
	/** @return The vector with the given name
	 * @param name The name of the vector */
	public Vector3f getVector3f(String name) {
		Vector3f result = vector3fHashMap.get(name);
		
		if(result != null)
			return result;
		
		return new Vector3f();
	}
	
	/** @return The float with the given name
	 * @param name The name of the float */
	public float getFloat(String name) {
		Float result = floatHashMap.get(name);
		
		if(result != null)
			return result;
		
		return 0;
	}
}
