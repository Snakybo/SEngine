package com.snakybo.sengine.object.prefab;

import com.snakybo.sengine.components.renderer.MeshRenderer;
import com.snakybo.sengine.object.GameObject;
import com.snakybo.sengine.resource.material.Material;
import com.snakybo.sengine.resource.mesh.Mesh;

/**
 * @author Kevin
 * @since Jan 1, 2016
 */
public class Plane extends GameObject
{
	public Plane()
	{
		this(Material.createDefault());
	}
	
	public Plane(Material material)
	{
		addComponent(new MeshRenderer(new Mesh("default/plane.obj"), material));
	}
}
