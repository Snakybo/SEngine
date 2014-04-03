package com.snakybo.engine.renderer;

import java.util.HashMap;
import java.util.Map;

import com.snakybo.engine.renderer.resourcemanagement.MappedValues;

/** @author Kevin Krol */
public class Material extends MappedValues {
	private Map<String, Texture> textureMap;
	
	public Material() {
		textureMap = new HashMap<String, Texture>();
	}
	
	/** Add a texture to the material
	 * @param name The name of the texture, used to get the texture
	 * @param value The texture */
	public void addTexture(String name, Texture value) {
		textureMap.put(name, value);
	}
	
	/** @return The texture with the given name
	 * @param name The name of the texture */
	public Texture getTexture(String name) {
		Texture result = textureMap.get(name);
		
		if(result != null)
			return result;
		
		return new Texture("default texture.png");
	}
}
