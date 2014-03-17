package com.snakybo.engine.renderer;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

/** @author Kevin Krol */
public class Texture {
	public static final Texture DEFAULT = new Texture("default texture.png");
	
	private int textureId;
	
	/** Create a new texture and load it from a file
	 * @param fileName The name of the texture file */
	public Texture(String fileName) {
		this(loadTexture(fileName));
	}
	
	/** Create a new texture based of an existing one
	 * @param id The Id of the texture */
	public Texture(int id) {
		this.textureId = id;
	}
	
	/** Bind the texture */
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, textureId);
	}
	
	/** @return The ID of the texture */
	public int getTextureId() {
		return textureId;
	}
	
	/** Load a texture file
	 * @param fileName The name of the texture file
	 * @return The ID of the texture */
	private static int loadTexture(String fileName) {
		BufferedImage texture = null;
		
		try {
			texture = ImageIO.read(new File("./res/textures/" + fileName));
			
			int[] pixels = new int[texture.getWidth() * texture.getHeight()];
			texture.getRGB(0, 0, texture.getWidth(), texture.getHeight(), pixels, 0,
					texture.getWidth());
			
			ByteBuffer buffer =
					BufferUtils.createByteBuffer(texture.getWidth() * texture.getHeight() * 4);
			
			for(int y = 0; y < texture.getHeight(); y++) {
				for(int x = 0; x < texture.getWidth(); x++) {
					int pixel = pixels[y * texture.getWidth() + x];
					buffer.put((byte)((pixel >> 16) & 0xFF));
					buffer.put((byte)((pixel >> 8) & 0xFF));
					buffer.put((byte)(pixel & 0xFF));
					buffer.put((byte)((pixel >> 24) & 0xFF));
				}
			}
			
			buffer.flip();
			
			int textureID = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, textureID);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, texture.getWidth(), texture.getHeight(), 0,
					GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			
			return textureID;
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return 0;
	}
}
