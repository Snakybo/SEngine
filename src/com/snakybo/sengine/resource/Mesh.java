package com.snakybo.sengine.resource;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.resource.loading.IModel;
import com.snakybo.sengine.resource.loading.IndexedModel;
import com.snakybo.sengine.resource.loading.OBJModel;
import com.snakybo.sengine.resource.management.MeshData;

/** @author Kevin Krol
 * @since Jul 8, 2014 */
public class Mesh {
	private static final String MESH_FOLDER = "./res/models/";
	private static final String DEFAULT_MESH = "internal/cube.obj";
	
	private static Map<String, MeshData> resourceMap = new HashMap<String, MeshData>();
	
	private MeshData resource;
	private String fileName;
	
	public Mesh() {
		this(DEFAULT_MESH);
	}
	
	public Mesh(String fileName) {
		this.fileName = fileName;
		
		MeshData existingResource = resourceMap.get(fileName);
		
		if(existingResource != null) {
			resource = existingResource;
			resource.addReference();
		} else {
			loadMesh(fileName);
			resourceMap.put(fileName, resource);
		}
	}
	
	public Mesh(String meshName, IndexedModel model) {
		this.fileName = meshName;
		
		MeshData existingResource = resourceMap.get(fileName);
		
		if(existingResource != null) {
			resource = existingResource;
			resource.addReference();
		} else {
			resource = new MeshData(model);
			resourceMap.put(fileName, resource);
		}
	}
	
	public Mesh(Mesh other) {
		fileName = other.fileName;
		resource = other.resource;
		
		resource.addReference();
	}
	
	@Override
	protected void finalize() throws Throwable {
		try {
			if(resource.removeReference() && !fileName.isEmpty())
				resourceMap.remove(fileName);
		} finally {
			super.finalize();
		}
	}
	
	public void draw() {
		resource.draw();
	}
	
	private void loadMesh(String fileName) {
		try {
			if(fileName.lastIndexOf('.') == -1)
				throw new IllegalArgumentException("Invalid file name passed: make sure to add the extension to the file name: " + fileName);
			
			String ext = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
			IModel model = null;
			
			switch(ext) {
			case ".obj":
				model = new OBJModel(new FileReader(MESH_FOLDER + fileName));
				break;
			default:
				System.err.println("The file extension of the model " + fileName + " is not supported by the engine");
				System.exit(1);
			}
			
			resource = new MeshData(model.toIndexedModel());
		} catch(FileNotFoundException e) {
			System.err.println("Error loading mesh: The mesh " + fileName + " doesn't exist. Using the default mesh");
			loadMesh(DEFAULT_MESH);
		}
	}
}
