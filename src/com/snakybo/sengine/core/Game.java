package com.snakybo.sengine.core;

import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.rendering.RenderingEngine;

/** Base game class, your has to extend this class in order to work with the
 * engine
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public abstract class Game
{
	private static Game instance;
	
	private GameObject root;
	
	public void init()
	{
	}
	
	final void internalInit()
	{
		if(instance == null)
		{
			instance = this;
			root = new GameObject();
		}
	}
	
	final void update()
	{
		root.update();
	}
	
	final void render(RenderingEngine renderingEngine)
	{
		renderingEngine.render(root);
		renderingEngine.postRenderObjects();
	}
	
	public static void addChild(GameObject object)
	{
		if(instance != null)
		{
			instance.root.addChild(object);
		}
	}
	
	public static void removeChild(GameObject object)
	{
		if(instance != null)
		{
			instance.root.removeChild(object);
		}
	}
	
	public static void clearScene()
	{
		if(instance != null)
		{
			instance.root.removeAllChildren();
		}
	}
}
