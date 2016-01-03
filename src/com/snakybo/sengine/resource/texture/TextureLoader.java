package com.snakybo.sengine.resource.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.snakybo.sengine.resource.ResourceLoader;
import com.snakybo.sengine.utils.Buffer;
import com.snakybo.sengine.utils.DirectoryManager;

/**
 * @author Kevin
 * @since Dec 10, 2015
 */
public abstract class TextureLoader
{
	public static class TextureData
	{
		private ByteBuffer data;
		
		private int width;
		private int height;
		
		public TextureData(ByteBuffer data, int width, int height)
		{
			this.data = data;
			this.width = width;
			this.height = height;
		}
		
		public final ByteBuffer getData()
		{
			return data;
		}
		
		public final int getWidth()
		{
			return width;
		}
		
		public final int getHeight()
		{
			return height;
		}
	}
	
	private static final String TEXTURE_FOLDER = "textures/";
	private static final String CURSOR_FOLDER = "textures/cursors/";
	
	static
	{
		DirectoryManager.check(TEXTURE_FOLDER);
		DirectoryManager.check(CURSOR_FOLDER);
	}
	
	public static TextureData loadTexture(String fileName)
	{
		return load(TEXTURE_FOLDER, fileName);
	}
	
	public static TextureData loadCursor(String fileName)
	{
		return load(CURSOR_FOLDER, fileName);
	}
	
	public static TextureData load(String folder, String fileName)
	{
		try
		{
			BufferedImage image = ImageIO.read(ResourceLoader.loadResource(folder + fileName));
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

			ByteBuffer data = Buffer.createByteBuffer(image.getHeight() * image.getWidth() * 4);
			
			boolean hasAlpha = image.getColorModel().hasAlpha();

			// Put each pixel in a Byte Buffer
			for(int y = 0; y < image.getHeight(); y++)
			{
				for(int x = 0; x < image.getWidth(); x++)
				{
					int pixel = pixels[y * image.getWidth() + x];

					byte alphaByte = hasAlpha ? (byte)((pixel >> 24) & 0xFF) : (byte)(0xFF);

					data.put((byte)((pixel >> 16) & 0xFF));
					data.put((byte)((pixel >> 8) & 0xFF));
					data.put((byte)((pixel) & 0xFF));
					data.put(alphaByte);
				}
			}

			data.flip();
			return new TextureData(data, image.getWidth(), image.getHeight());
		}
		catch(IOException e)
		{
			System.err.println("[Texture] Failed to load texture: " + fileName + " doesn't exist");
		}
		
		return null;
	}
}
