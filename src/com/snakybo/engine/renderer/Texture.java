package com.snakybo.engine.renderer;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.snakybo.engine.core.Util;
import com.snakybo.engine.renderer.resourcemanagement.TextureResource;

/** @author Kevin Krol */
public class Texture {
	private static HashMap<String, TextureResource> loadedTextures = new HashMap<String, TextureResource>();
	
	private TextureResource resource;
	
	private String fileName;
	
	/** Create a new texture and load it from a file
	 * @param fileName The name of the texture file */
	public Texture(String fileName) {
		this.fileName = fileName;

		TextureResource existingResource = loadedTextures.get(fileName);
		
		if(existingResource != null) {
			resource = existingResource;
			resource.addReference();
		} else {
			resource = loadTexture(fileName);
			loadedTextures.put(fileName, resource);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		if(resource.removeReference() && !fileName.isEmpty())
			loadedTextures.remove(fileName);
	}
	
	public void bind() {
		bind(0);
	}
	
	/** Bind the texture */
	public void bind(int samplerSlot) {
		assert(samplerSlot >= 0 && samplerSlot <= 32);
		glActiveTexture(GL_TEXTURE0 + samplerSlot);
		
		glBindTexture(GL_TEXTURE_2D, resource.getTextureId());
	}
	
	/** @return The ID of the texture */
	public int getTextureId() {
		return resource.getTextureId();
	}
	
	/** Load a texture file
	 * @param fileName The name of the texture file
	 * @return The ID of the texture */
	private static TextureResource loadTexture(String fileName) {
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File("./res/textures/" + fileName));
			
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
			
			ByteBuffer buffer = Util.createByteBuffer(image.getWidth() * image.getHeight() * 4);

			for(int y = 0; y < image.getHeight(); y++) {
				for(int x = 0; x < image.getWidth(); x++) {
					int pixel = pixels[y * image.getWidth() + x];
					buffer.put((byte)((pixel >> 16) & 0xFF));
					buffer.put((byte)((pixel >> 8) & 0xFF));
					buffer.put((byte)(pixel & 0xFF));
					
					if(image.getColorModel().hasAlpha()) {
						buffer.put((byte)((pixel >> 24) & 0xFF));
					} else {
						buffer.put((byte)(0xFF));
					}
				}
			}
			
			buffer.flip();
			
			TextureResource resource = new TextureResource();
			glBindTexture(GL_TEXTURE_2D, resource.getTextureId());
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0,
					GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			
			return resource;
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
}
