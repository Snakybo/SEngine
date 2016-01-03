package com.snakybo.sengine.object;

import com.snakybo.sengine.rendering.Camera;
import com.snakybo.sengine.shader.Shader;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 */
public abstract class Component
{
	GameObject parent;
	
	protected void onAddedToScene()
	{
	}
	
	protected void onRemovedFromScene()
	{
	}
	
	protected void onDestroy()
	{
	}
	
	protected void update()
	{
	}
	
	protected void render(Shader shader, Camera camera)
	{
	}
	
	public final GameObject getParent()
	{
		return parent;
	}
	
	public final Transform getTransform()
	{
		return parent.getTransform();
	}
}