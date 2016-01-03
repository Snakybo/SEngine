package com.snakybo.sengine.object;

import java.util.ArrayList;
import java.util.List;

import com.snakybo.sengine.rendering.Camera;
import com.snakybo.sengine.shader.Shader;

/**
 * @author Kevin
 * @since Jan 2, 2016
 */
public abstract class GameObjectInternal
{
	private static List<GameObject> gameObjects = new ArrayList<GameObject>();
	private static List<GameObject> gameObjectsToAdd = new ArrayList<GameObject>();
	private static List<GameObject> gameObjectsToDestroy = new ArrayList<GameObject>();
	
	public static void updateInternal()
	{
		for(GameObject gameObject : gameObjectsToAdd)
		{
			gameObject.addToScene();
			gameObjects.add(gameObject);
		}
		
		for(GameObject gameObject : gameObjectsToDestroy)
		{
			gameObject.removeFromScene();
			gameObject.destroy();
			gameObjects.remove(gameObject);
		}
		
		gameObjectsToAdd.clear();
		gameObjectsToDestroy.clear();
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
	
	static void remove(GameObject gameObject)
	{
		if(!gameObjectsToDestroy.contains(gameObject))
		{
			gameObjectsToDestroy.add(gameObject);
		}
	}
}