package com.snakybo.sengine.resource;

import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.resource.loading.IndexedModel;
import com.snakybo.sengine.resource.loading.ObjModel;
import com.snakybo.sengine.resource.management.MeshData;

/** @author Kevin Krol
 * @since Jul 8, 2014 */
public class Mesh {
	private static Map<String, MeshData> resourceMap = new HashMap<String, MeshData>();
	
	private MeshData resource;
	private String fileName;
	
	public Mesh() {
		this("cube.obj");
	}
	
	public Mesh(String fileName) {
		this.fileName = fileName;
		
		MeshData existingResource = resourceMap.get(fileName);
		
		if(existingResource != null) {
			resource = existingResource;
			resource.addReference();
		} else {
			loadMesh();
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
	
	private void loadMesh() {
		final String MODEL_PATH = "./res/models/";
		
		String ext = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
		
		switch(ext) {
		case ".obj":
			IndexedModel model = new ObjModel(MODEL_PATH + fileName).toIndexedModel();
			resource = new MeshData(model);
			break;
		default:
			System.err.println("The file extension of the model " + fileName + " is not supported by the engine");
			System.exit(1);
		}
	}
}
