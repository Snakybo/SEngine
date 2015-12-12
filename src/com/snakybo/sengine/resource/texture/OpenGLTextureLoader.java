package com.snakybo.sengine.resource.texture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import com.snakybo.sengine.utils.Buffer;

/**
 * @author Kevin
 * @since Dec 10, 2015
 */
class OpenGLTextureLoader
{
	private static final String TEXTURE_FOLDER = "./res/textures/";
	
	private ByteBuffer data;
	
	private int width;
	private int height;
	
	public OpenGLTextureLoader(String fileName)
	{
		try
		{
			BufferedImage image = ImageIO.read(new File(TEXTURE_FOLDER + fileName));
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

			data = Buffer.createByteBuffer(image.getHeight() * image.getWidth() * 4);
			width = image.getWidth();
			height = image.getHeight();

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
		}
		catch(IOException e)
		{
			System.err.println("[Texture] Failed to load texture: " + fileName + " doesn't exist");
		}
	}
	
	public ByteBuffer getData()
	{
		return data;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
}
