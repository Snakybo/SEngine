package com.snakybo.sengine.rendering;

import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.utils.IDataContainer;

/**
 * @author Kevin
 * @since Dec 10, 2015
 */
public interface IRenderingEngine extends IDataContainer
{	
	/**
	 * Render the object.
	 * @param obj The object to render.
	 */
	void render(GameObject obj);
	
	/**
	 * Get the sampler slot of a texture.
	 * @param samplerName The name of the sampler.
	 * @return The slot in the sampler.
	 */
	int getTextureSamplerSlot(String samplerName);
}
