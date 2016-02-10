package com.snakybo.sengine.object;

import java.util.HashSet;
import java.util.Set;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.shader.Shader;

/**
 * @author Kevin
 * @since Jan 2, 2016
 */
public abstract class GameObjectInternal
{
	private static Set<GameObject> gameObjects = new HashSet<GameObject>();
	private static Set<GameObject> gameObjectsToAdd = new HashSet<GameObject>();
	
	public static void updateInternal()
	{
		Set<GameObject> destroyed = new HashSet<GameObject>();
		
		for(GameObject gameObject : gameObjectsToAdd)
		{
			gameObjects.add(gameObject);
		}
		
		for(GameObject gameObject : gameObjects)
		{
			if(gameObject.destroyed)
			{
				destroyed.add(gameObject);
			}
		}
		
		for(GameObject gameObject : destroyed)
		{
			gameObjects.remove(gameObject);
		}
		
		gameObjectsToAdd.clear();
	}
	
	public static void updateGameObjects()
	{
		for(GameObject gameObject : gameObjects)
		{
			gameObject.update();
		}
	}
	
	public static void renderGameObjects(Shader shader, Camera camera)
	{
		for(GameObject gameObject : gameObjects)
		{
			gameObject.render(shader, camera);
		}
	}
	
	static void add(GameObject gameObject)
	{
		if(!gameObjectsToAdd.contains(gameObject))
		{
			gameObjectsToAdd.add(gameObject);
		}
	}
}