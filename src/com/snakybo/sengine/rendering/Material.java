package com.snakybo.sengine.rendering;

import java.util.HashMap;

import com.snakybo.sengine.rendering.resourceManagement.MappedValues;

/** Material class extends {@link MappedValues}
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class Material extends MappedValues {
	private HashMap<String, Texture> textureHashMap;
	
	/** Constructor for the material */
	public Material() {
		super();
		
		textureHashMap = new HashMap<String, Texture>();
	}
	
	/** Add a texture to the material
	 * @param name The name of the texture
	 * @param texture The texture */
	public void addTexture(String name, Texture texture) {
		textureHashMap.put(name, texture);
	}
	
	/** @return A texture from the material, or the default texture if it isn't found
	 * @param name The name of the texture */
	public Texture getTexture(String name) {
		Texture result = textureHashMap.get(name);
		if(result != null)
			return result;
		
		return new Texture("test.png");
	}
}
