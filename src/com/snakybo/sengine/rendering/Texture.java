package com.snakybo.sengine.rendering;

import static org.lwjgl.opengl.GL11.GL_CLAMP;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_REPEAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.snakybo.sengine.core.utils.Buffer;
import com.snakybo.sengine.rendering.resourceManagement.TextureResource;

public class Texture {
	public static final int TEXTURE_TYPE_2D = GL_TEXTURE_2D;
	
	public static final int FILTER_LINEAR = GL_LINEAR;
	public static final int FILTER_NEAREST = GL_NEAREST;
	
	public static final int FILTER_LINEAR_MIPMAP_NEAREST = GL_LINEAR_MIPMAP_NEAREST;
	public static final int FILTER_LINEAR_MIPMAP_LINEAR = GL_LINEAR_MIPMAP_LINEAR;
	public static final int FILTER_NEAREST_MIPMAP_NEAREST = GL_NEAREST_MIPMAP_NEAREST;
	public static final int FILTER_NEAREST_MIPMAP_LINEAR = GL_NEAREST_MIPMAP_LINEAR;
	
	public static final int WRAP_NONE = 0;
	public static final int WRAP_CLAMP = GL_CLAMP;
	public static final int WRAP_REPEAT = GL_REPEAT;
	
	private static HashMap<String, TextureResource> loadedTextures = new HashMap<String, TextureResource>();
	
	private TextureResource resource;
	private String fileName;
	
	public Texture(ByteBuffer buffer, int textureType, int filters, int wraps, int[] attachments, int width, int height, int textureCount) {
		fileName = "";
		resource = new TextureResource(buffer, textureType, filters, wraps, attachments, width, height, textureCount);
	}
	
	public Texture(String fileName) {
		this(fileName, TEXTURE_TYPE_2D, FILTER_LINEAR_MIPMAP_LINEAR, WRAP_REPEAT);
	}
	
	public Texture(String fileName, int textureType, int textureFilters, int textureWraps) {
		this(fileName, textureType, textureFilters, textureWraps, new int[] {GL_NONE});
	}
	
	public Texture(String fileName, int textureType, int textureFilters, int textureWraps, int[] attachments) {
		this.fileName = fileName;
		
		TextureResource oldResource = loadedTextures.get(fileName);
		
		if(oldResource != null) {
			resource = oldResource;
			resource.addReference();
		} else {
			loadTexture(fileName, textureType, textureFilters, textureWraps, attachments);
			loadedTextures.put(fileName, resource);
		}
	}
	
	@Override
	protected void finalize() {
		if(resource.removeReference() && !fileName.isEmpty())
			loadedTextures.remove(fileName);
	}
	
	public void bind() {
		bind(0);
	}
	
	public void bind(int samplerSlot) {
		assert (samplerSlot >= 0 && samplerSlot <= 31);
		glActiveTexture(GL_TEXTURE0 + samplerSlot);
		resource.bind(0);
	}
	
	public void bindAsRenderTarget() {
		resource.bindAsRenderTarget();
	}
	
	private void loadTexture(String fileName, int textureType, int filters, int wraps, int[] attachments) {
		try {
			BufferedImage image = ImageIO.read(new File("./res/textures/" + fileName));
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
			
			ByteBuffer buffer = Buffer.createByteBuffer(image.getHeight() * image.getWidth() * 4);
			boolean hasAlpha = image.getColorModel().hasAlpha();
			
			for(int y = 0; y < image.getHeight(); y++) {
				for(int x = 0; x < image.getWidth(); x++) {
					int pixel = pixels[y * image.getWidth() + x];
					
					buffer.put((byte)((pixel >> 16) & 0xFF));
					buffer.put((byte)((pixel >> 8) & 0xFF));
					buffer.put((byte)((pixel) & 0xFF));
					if(hasAlpha)
						buffer.put((byte)((pixel >> 24) & 0xFF));
					else
						buffer.put((byte)(0xFF));
				}
			}
			
			buffer.flip();
			
			resource = new TextureResource(buffer, textureType, filters, wraps, attachments, image.getWidth(), image.getHeight(), 1);
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
