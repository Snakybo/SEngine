package com.snakybo.sengine.rendering;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL15;

import com.snakybo.sengine.core.utils.Buffer;
import com.snakybo.sengine.core.utils.Utils;
import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.meshLoading.IndexedModel;
import com.snakybo.sengine.rendering.meshLoading.OBJModel;
import com.snakybo.sengine.rendering.resourceManagement.MeshResource;

/** Mesh class
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class Mesh {
	private static HashMap<String, MeshResource> loadedModels = new HashMap<String, MeshResource>();
	
	private MeshResource resource;
	private String fileName;
	
	/** Constructor for the mesh
	 * @param fileName The mesh to load */
	public Mesh(String fileName) {
		this.fileName = fileName;
		
		MeshResource oldResource = loadedModels.get(fileName);
		
		if(oldResource != null) {
			resource = oldResource;
			resource.addReference();
		} else {
			loadMesh(fileName);
			loadedModels.put(fileName, resource);
		}
	}
	
	/** Constructor for the mesh
	 * @param vertices The vertices of the mesh
	 * @param indices The indices of the mesh */
	public Mesh(Vertex[] vertices, int[] indices) {
		this(vertices, indices, false);
	}
	
	/** Constructor for the mesh
	 * @param vertices The vertices of the mesh
	 * @param indices The indices of the mesh
	 * @param calcNormal Whether or not to calculate the normals of the mesh */
	public Mesh(Vertex[] vertices, int[] indices, boolean calcNormals) {
		fileName = "";
		
		addVertices(vertices, indices, calcNormals);
	}
	
	@Override
	protected void finalize() {
		if(resource.removeReference() && !fileName.isEmpty()) {
			loadedModels.remove(fileName);
		}
	}
	
	/** Draw the mesh */
	public void draw() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
	}
	
	/** Add vertices to the mesh
	 * @param vertices The vertices to add
	 * @param indices The indices to add
	 * @param calcNormals Whether or not to calculate the normals of the mesh */
	private void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals) {
		if(calcNormals) {
			calcNormals(vertices, indices);
		}
		
		resource = new MeshResource(indices.length);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		GL15.glBufferData(GL_ARRAY_BUFFER, Buffer.createFlippedBuffer(vertices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Buffer.createFlippedBuffer(indices), GL_STATIC_DRAW);
	}
	
	/** Calculate the normals of the mesh
	 * @param vertices The vertices of the mesh
	 * @param indices The indices of the mesh */
	private void calcNormals(Vertex[] vertices, int[] indices) {
		for(int i = 0; i < indices.length; i += 3) {
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];
			
			Vector3f v1 = vertices[i1].getPosition().sub(vertices[i0].getPosition());
			Vector3f v2 = vertices[i2].getPosition().sub(vertices[i0].getPosition());
			
			Vector3f normal = v1.cross(v2).normalize();
			
			vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
			vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
			vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
		}
		
		for(int i = 0; i < vertices.length; i++)
			vertices[i].setNormal(vertices[i].getNormal().normalize());
	}
	
	/** Load a mesh file
	 * @param fileName The name of the file
	 * @return A mesh created from the file */
	private Mesh loadMesh(String fileName) {
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
		if(!ext.equals("obj")) {
			System.err.println("Error: File format not supported for mesh data: " + ext);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		OBJModel test = new OBJModel("./res/models/" + fileName);
		IndexedModel model = test.toIndexedModel();
		model.calcNormals();
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		
		for(int i = 0; i < model.getPositions().size(); i++)
			vertices.add(new Vertex(model.getPositions().get(i), model.getTexCoords().get(i), model.getNormals().get(i), model.getTangents().get(i)));
		
		Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);
		
		Integer[] indexData = new Integer[model.getIndices().size()];
		model.getIndices().toArray(indexData);
		
		addVertices(vertexData, Utils.toIntArray(indexData), false);
		
		return null;
	}
}
