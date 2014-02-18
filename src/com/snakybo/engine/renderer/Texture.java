package com.snakybo.engine.renderer;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

public class Texture {
	private int id;
	
	public Texture(String fileName) {
		this(loadTexture(fileName));
	}
	
	public Texture(int id) {
		this.id = id;
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	public int getID() {
		return id;
	}
	
	/** Load a texture */
	private static int loadTexture(String fileName) {
		BufferedImage texture = null;
		
		try {
			texture = ImageIO.read(new File("./res/textures/" + fileName));
			
			int[] pixels = new int[texture.getWidth() * texture.getHeight()];
			texture.getRGB(0, 0, texture.getWidth(), texture.getHeight(), pixels, 0, texture.getWidth());
			
			ByteBuffer buffer = BufferUtils.createByteBuffer(texture.getWidth() * texture.getHeight() * 4);
			
			for(int y = 0; y < texture.getHeight(); y++) {
				for(int x = 0; x < texture.getWidth(); x++) {
					int pixel = pixels[y * texture.getWidth() + x];
					buffer.put((byte) ((pixel >> 16) & 0xFF));
					buffer.put((byte) ((pixel >> 8) & 0xFF));
					buffer.put((byte) (pixel & 0xFF));
					buffer.put((byte) ((pixel >> 24) & 0xFF));
				}
			}
			
			buffer.flip();
			
			int textureID = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, textureID);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, texture.getWidth(), texture.getHeight(), 0 , GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			
			return textureID;
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return 0;
	}
}
