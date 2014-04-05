package com.snakybo.sengine.rendering.resourceManagement;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

/** Mesh resource
 * 
 * Resource manager for meshes
 * 
 * @author Kevin Krol
 * @since Apr 5, 2014 */
public class MeshResource {
	private int vbo;
	private int ibo;
	private int size;
	private int refCount;
	
	/** Constructor for the mesh resource
	 * @param size The size of the mesh */
	public MeshResource(int size) {
		vbo = glGenBuffers();
		ibo = glGenBuffers();
		
		this.size = size;
		this.refCount = 1;
	}
	
	@Override
	protected void finalize() {
		glDeleteBuffers(vbo);
		glDeleteBuffers(ibo);
	}
	
	/** Add a reference to the resource */
	public void addReference() {
		refCount++;
	}
	
	/** Remove a reference from the resource
	 * @return Whether or not there are no more references remaining */
	public boolean removeReference() {
		refCount--;
		
		return refCount == 0;
	}
	
	/** @return The VBO of the mesh */
	public int getVbo() {
		return vbo;
	}
	
	/** @return The IBO of the mesh */
	public int getIbo() {
		return ibo;
	}
	
	/** @return The size of the mesh */
	public int getSize() {
		return size;
	}
}
