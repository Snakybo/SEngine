package com.snakybo.sengine.resource.mesh.loader;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.snakybo.sengine.resource.mesh.MeshResource;
import com.snakybo.sengine.resource.mesh.loader.obj.OBJModel;
import com.snakybo.sengine.utils.StringUtils;

/**
 * @author Kevin
 * @since Dec 19, 2015
 */
public class MeshLoader
{
	private static final String MESH_FOLDER = "./res/models/";
	
	public static MeshResource loadMesh(String fileName)
	{
		String name = null;
		String extension = null;
		
		if(fileName.lastIndexOf('.') == -1)
		{
			name = fileName;
			extension = StringUtils.getFileExtension(MESH_FOLDER, fileName);
		}
		else
		{
			name = fileName.substring(0, fileName.lastIndexOf('.'));
			extension = fileName.substring(fileName.lastIndexOf('.') + 1);
		}
		
		try
		{
			FileReader file = new FileReader(MESH_FOLDER + name + "." + extension);		
			IModel model = null;
			
			switch(extension)
			{
			case "obj":
				model = new OBJModel(file);
				break;
			default:
				System.err.println("[MeshLoader] The engine currently does not support " + extension + " models");
				System.exit(1);
			}
			
			return new MeshResource(model.toReadableModel());
		}
		catch(FileNotFoundException e)
		{
			System.err.println("[MeshLoader] No model with the name: " + (MESH_FOLDER + name + "." + extension) + " found");
			System.exit(1);
		}
		
		return null;
	}
}