package com.snakybo.sengine.resource.mesh;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.snakybo.sengine.resource.ResourceManager;
import com.snakybo.sengine.resource.mesh.loading.IModel;
import com.snakybo.sengine.resource.mesh.loading.IndexedModel;
import com.snakybo.sengine.resource.mesh.loading.OBJModel;

/** 
 * @author Kevin Krol
 * @since Jul 8, 2014
 */
public class Mesh
{
	private static final String MESH_FOLDER = "./res/models/";
	
	private final String fileName;
	private final MeshData resource;
	
	public Mesh(String fileName)
	{
		this.fileName = fileName;
		
		if(ResourceManager.has(fileName))
		{
			ResourceManager.add(fileName);
			resource = ResourceManager.get(MeshData.class, fileName);
		}
		else
		{
			resource = loadMesh(fileName);
			ResourceManager.add(fileName, resource);
		}
	}

	public Mesh(String meshName, IndexedModel model)
	{
		this.fileName = meshName;
		
		if(ResourceManager.has(fileName))
		{
			ResourceManager.add(fileName);
			resource = ResourceManager.get(MeshData.class, fileName);
		}
		else
		{
			resource = new MeshData(model);
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
		resource.draw();
	}

	private MeshData loadMesh(String fileName)
	{
		try
		{
			if(fileName.lastIndexOf('.') == -1)
			{
				throw new IllegalArgumentException("[Mesh] Invalid file name passed: make sure to add the extension to the file name: " + fileName);
			}

			String ext = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
			IModel model = null;

			switch(ext)
			{
			case ".obj":
				model = new OBJModel(new FileReader(MESH_FOLDER + fileName));
				break;
			default:
				System.err.println("[Mesh] The file extension of the model " + fileName + " is not supported by the engine");
				System.exit(1);
			}

			return new MeshData(model.toIndexedModel());
		}
		catch(FileNotFoundException e)
		{
			throw new IllegalArgumentException("[Mesh] Error loading mesh: The mesh " + fileName + " doesn't exist.");
		}
	}
}
