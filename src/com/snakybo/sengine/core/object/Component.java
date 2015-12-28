package com.snakybo.sengine.core.object;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.shader.Shader;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 */
public abstract class Component
{
	private GameObject parent;
	
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
	
	final void setParent(GameObject parent)
	{
		this.parent = parent;
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