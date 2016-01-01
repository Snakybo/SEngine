package com.snakybo.sengine.object.prefab;

import com.snakybo.sengine.math.Quaternion;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.object.GameObject;

/**
 * @author Kevin
 * @since Jan 1, 2016
 */
public abstract class Prefab extends GameObject
{
	public Prefab(Vector3f position, Quaternion rotation, Vector3f scale)
	{
		super(position, rotation, scale);
	}
}