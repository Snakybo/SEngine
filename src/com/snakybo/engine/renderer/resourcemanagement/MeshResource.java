package com.snakybo.engine.renderer.resourcemanagement;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

/** @author Kevin Krol
 * @since Mar 19, 2014 */
public class MeshResource {
	private int vbo;
	private int ibo;
	private int size;
	
	private int references;
	
	public MeshResource(int size) {
		vbo = glGenBuffers();
		ibo = glGenBuffers();
		
		this.size = size;
		this.references = 1;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		glDeleteBuffers(vbo);
		glDeleteBuffers(ibo);
	}
	
	public void addReference() {
		references++;
	}
	
	public boolean removeReference() {
		references--;
		
		return references == 0;
	}
	
	public int getVbo() {
		return vbo;
	}
	
	public int getIbo() {
		return ibo;
	}
	
	public int getSize() {
		return size;
	}
}
