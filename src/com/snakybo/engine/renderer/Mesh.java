package com.snakybo.engine.renderer;

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

import com.snakybo.engine.core.Util;
import com.snakybo.engine.math.Vector3f;
import com.snakybo.engine.renderer.loader.IndexModel;
import com.snakybo.engine.renderer.loader.ObjModel;
import com.snakybo.engine.renderer.resourcemanagement.MeshResource;

/** @author Kevin Krol */
public class Mesh {
	private static HashMap<String, MeshResource> loadedModels = new HashMap<String, MeshResource>();
	
	private MeshResource resource;
	
	private String fileName;
	
	public Mesh(String fileName) {
		this.fileName = fileName;
		
		MeshResource existingResource = loadedModels.get(fileName);
		
		if(existingResource != null) {
			resource = existingResource;
			resource.addReference();
		} else {
			loadMesh(fileName);
			
			loadedModels.put(fileName, resource);
		}
	}
	
	public Mesh(Vertex[] vertices, int[] indices) {
		this(vertices, indices, false);
	}
	
	public Mesh(Vertex[] vertices, int[] indices, boolean calcNormals) {
		fileName = "";
		
		addVertices(vertices, indices, calcNormals);
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		if(resource.removeReference() && !fileName.isEmpty())
			loadedModels.remove(fileName);
	}
	
	/** Add vertices to the Mesh */
	private void addVertices(Vertex[] vertices, int[] indices, boolean calculateNormals) {
		if(calculateNormals)
			calculateNormals(vertices, indices);
		
		resource = new MeshResource(indices.length);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glBufferData(GL_ARRAY_BUFFER, Util.createFlippedBuffer(vertices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Util.createFlippedBuffer(indices), GL_STATIC_DRAW);
	}
	
	/** Draw vertices */
	public void draw() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
	}
	
	/** Calculate normals */
	private void calculateNormals(Vertex[] vertices, int[] indices) {
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
		
		for(int i = 0; i < vertices.length; i++) {
			vertices[i].setNormal(vertices[i].getNormal().normalize());
		}
	}
	
	/** Load a 3D model */
	private void loadMesh(String fileName) {
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
		
		if(!ext.equals("obj")) {
			System.err.println("Error: File format not supported for mesh data: " + ext);
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		ObjModel test = new ObjModel("./res/models/" + fileName);
		IndexModel model = test.toIndexModel();
		model.calculateNormals();
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		
		for(int i = 0; i < model.getPositions().size(); i++)
			vertices.add(new Vertex(model.getPositions().get(i), model.getTexCoords().get(i), model.getNormals().get(i)));
		
		Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);
		
		Integer[] indexData = new Integer[model.getIndices().size()];
		model.getIndices().toArray(indexData);
		
		addVertices(vertexData, Util.toIntArray(indexData), false);
	}
}
