package com.snakybo.sengine.object.prefab;

import com.snakybo.sengine.components.renderer.MeshRenderer;
import com.snakybo.sengine.object.GameObject;
import com.snakybo.sengine.resource.material.Material;
import com.snakybo.sengine.resource.mesh.Mesh;

/**
 * @author Kevin
 * @since Jan 1, 2016
 */
public class Cube extends GameObject
{
	public Cube()
	{
		this(Material.createDefault());
	}
	
	public Cube(Material material)
	{
		addComponent(new MeshRenderer(new Mesh("default/cube.obj"), material));
	}
}
