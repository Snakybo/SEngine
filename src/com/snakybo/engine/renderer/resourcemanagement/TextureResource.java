package com.snakybo.engine.renderer.resourcemanagement;

import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;

/** @author Kevin Krol
 * @since Mar 24, 2014 */
public class TextureResource {
	private int textureId;
	
	private int references;
	
	public TextureResource() {
		this.textureId = glGenTextures();
		this.references = 1;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		glDeleteBuffers(textureId);
	}
	
	public void addReference() {
		references++;
	}
	
	public boolean removeReference() {
		references--;
		
		return references == 0;
	}
	
	public int getTextureId() {
		return textureId;
	}
}
