package com.snakybo.sengine.core.object.prefab;

import com.snakybo.sengine.components.MeshRenderer;
import com.snakybo.sengine.math.Quaternion;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.resource.material.Material;
import com.snakybo.sengine.resource.mesh.Mesh;

/**
 * @author Kevin
 * @since Jan 1, 2016
 */
public class CapsulePrefab extends Prefab
{
	public CapsulePrefab()
	{
		this(new Vector3f());
	}
	
	public CapsulePrefab(Vector3f position)
	{
		this(position, new Quaternion());
	}
	
	public CapsulePrefab(Vector3f position, Quaternion rotation)
	{
		this(position, rotation, new Vector3f(1, 1, 1));
	}
	
	public CapsulePrefab(Vector3f position, Quaternion rotation, Vector3f scale)
	{
		this(position, rotation, scale, Material.createDefault());
	}
	
	public CapsulePrefab(Material material)
	{
		this(new Vector3f(), material);
	}
	
	public CapsulePrefab(Vector3f position, Material material)
	{
		this(position, new Quaternion(), material);
	}
	
	public CapsulePrefab(Vector3f position, Quaternion rotation, Material material)
	{
		this(position, rotation, new Vector3f(1, 1, 1), material);
	}
	
	public CapsulePrefab(Vector3f position, Quaternion rotation, Vector3f scale, Material material)
	{
		super(position, rotation, scale);
		
		addComponent(new MeshRenderer(new Mesh("default/plane.obj"), material));
	}
}
