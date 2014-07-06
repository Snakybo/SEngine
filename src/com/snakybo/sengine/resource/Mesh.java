package com.snakybo.sengine.resource;

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

import com.snakybo.sengine.rendering.Vertex;
import com.snakybo.sengine.resource.loading.IndexedModel;
import com.snakybo.sengine.resource.loading.OBJModel;
import com.snakybo.sengine.resource.management.MeshData;
import com.snakybo.sengine.utils.Buffer;
import com.snakybo.sengine.utils.Utils;
import com.snakybo.sengine.utils.math.Vector3f;

public class Mesh {
	private static HashMap<String, MeshData> loadedModels = new HashMap<String, MeshData>();
	private MeshData resource;
	private String fileName;
	
	public Mesh(String fileName) {
		this.fileName = fileName;
		MeshData oldResource = loadedModels.get(fileName);
		
		if(oldResource != null) {
			resource = oldResource;
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
	protected void finalize() {
		if(resource.removeReference() && !fileName.isEmpty())
			loadedModels.remove(fileName);
	}
	
	private void addVertices(Vertex[] vertices, int[] indices, boolean calcNormals) {
		if(calcNormals) {
			calcNormals(vertices, indices);
		}
		
		resource = new MeshData(indices.length);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		GL15.glBufferData(GL_ARRAY_BUFFER, Buffer.createFlippedBuffer(vertices), GL_STATIC_DRAW);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, Buffer.createFlippedBuffer(indices), GL_STATIC_DRAW);
	}
	
	public void draw() {
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		
		glBindBuffer(GL_ARRAY_BUFFER, resource.getVbo());
		glVertexAttribPointer(0, 3, GL_FLOAT, false, Vertex.SIZE * 4, 0);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, Vertex.SIZE * 4, 12);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, Vertex.SIZE * 4, 20);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 32);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, Vertex.SIZE * 4, 44);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, resource.getIbo());
		glDrawElements(GL_TRIANGLES, resource.getSize(), GL_UNSIGNED_INT, 0);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
	}
	
	private void calcNormals(Vertex[] vertices, int[] indices) {
		for(int i = 0; i < indices.length; i += 3) {
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];
			
			Vector3f v1 = vertices[i1].getPosition().sub(vertices[i0].getPosition());
			Vector3f v2 = vertices[i2].getPosition().sub(vertices[i0].getPosition());
			
			Vector3f normal = v1.cross(v2).normalized();
			
			vertices[i0].setNormal(vertices[i0].getNormal().add(normal));
			vertices[i1].setNormal(vertices[i1].getNormal().add(normal));
			vertices[i2].setNormal(vertices[i2].getNormal().add(normal));
		}
		
		for(int i = 0; i < vertices.length; i++)
			vertices[i].setNormal(vertices[i].getNormal().normalized());
	}
	
	private Mesh loadMesh(String fileName) {
		String[] splitArray = fileName.split("\\.");
		String ext = splitArray[splitArray.length - 1];
		
		if(!ext.equals("obj")) {
			System.err.println("Error: '" + ext + "' file format not supported for mesh data.");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		OBJModel test = new OBJModel("./res/models/" + fileName);
		IndexedModel model = test.toIndexedModel();
		model.calcNormals();
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		
		for(int i = 0; i < model.getPositions().size(); i++) {
			vertices.add(new Vertex(model.getPositions().get(i), model.getTexCoords().get(i),
					model.getNormals().get(i), model.getTangents().get(i)));
		}
		
		Vertex[] vertexData = new Vertex[vertices.size()];
		vertices.toArray(vertexData);
		
		Integer[] indexData = new Integer[model.getIndices().size()];
		model.getIndices().toArray(indexData);
		
		addVertices(vertexData, Utils.toIntArray(indexData), false);
		
		return this;
	}
}
