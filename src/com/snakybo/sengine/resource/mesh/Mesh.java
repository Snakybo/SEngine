package com.snakybo.sengine.resource.mesh;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import com.snakybo.sengine.resource.ResourceManager;
import com.snakybo.sengine.resource.mesh.loader.MeshLoader;
import com.snakybo.sengine.resource.mesh.loader.ReadableModel;

/** 
 * @author Kevin Krol
 * @since Jul 8, 2014
 */
public class Mesh
{
	private final String fileName;
	private final MeshResource resource;
	
	public Mesh(String fileName)
	{
		this.fileName = fileName;
		
		if(ResourceManager.has(fileName))
		{
			ResourceManager.add(fileName);
			resource = ResourceManager.get(MeshResource.class, fileName);
		}
		else
		{
			resource = MeshLoader.loadMesh(fileName);
			ResourceManager.add(fileName, resource);
		}
	}
	
	public Mesh(String meshName, ReadableModel model)
	{
		this.fileName = meshName;
		
		if(ResourceManager.has(fileName))
		{
			ResourceManager.add(fileName);
			resource = ResourceManager.get(MeshResource.class, fileName);
		}
		else
		{
			resource = new MeshResource(model);
			ResourceManager.add(fileName, resource);
		}
	}
	
	public Mesh(Mesh other)
	{
		fileName = other.fileName;
		resource = other.resource;

		ResourceManager.add(fileName);
	}
	
	@Override
	public Mesh clone()
	{
		return new Mesh(this);
	}
	
	public void destroy()
	{
		ResourceManager.remove(fileName);
	}
	
	public void draw()
	{
		draw(GL_TRIANGLES);
	}

	public void draw(int mode)
	{
		resource.draw(mode);
	}
	
	public static Mesh createPrimitive(Primitive primitive)
	{
		return primitive.get();
	}
}
