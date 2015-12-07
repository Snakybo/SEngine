package com.snakybo.sengine.resource;

import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.snakybo.sengine.resource.management.TextureData;
import com.snakybo.sengine.utils.Buffer;

public class Texture
{
	private static final String TEXTURE_FOLDER = "./res/textures/";
	
	private static Map<String, TextureData> resourceMap = new HashMap<String, TextureData>();	
	
	private TextureData resource;
	private String fileName;

	public Texture()
	{
		this(0);
	}

	public Texture(int width)
	{
		this(width, 0);
	}

	public Texture(int width, int height)
	{
		this(width, height, null);
	}

	public Texture(int width, int height, ByteBuffer data)
	{
		this(width, height, data, GL_TEXTURE_2D);
	}

	public Texture(int width, int height, ByteBuffer data, int textureTarget)
	{
		this(width, height, data, textureTarget, GL_LINEAR_MIPMAP_LINEAR);
	}

	public Texture(int width, int height, ByteBuffer data, int textureTarget, int filters)
	{
		this(width, height, data, textureTarget, filters, GL_RGBA);
	}

	public Texture(int width, int height, ByteBuffer data, int textureTarget, int filters, int internalFormat)
	{
		this(width, height, data, textureTarget, filters, internalFormat, GL_RGBA);
	}

	public Texture(int width, int height, ByteBuffer data, int textureTarget, int filters, int internalFormat, int format)
	{
		this(width, height, data, textureTarget, filters, internalFormat, format, false);
	}

	public Texture(int width, int height, ByteBuffer data, int textureTarget, int filters, int internalFormat, int format, boolean clamp)
	{
		this(width, height, data, textureTarget, filters, internalFormat, format, clamp, GL_NONE);
	}

	public Texture(int width, int height, ByteBuffer data, int textureTarget, int filters, int internalFormat, int format, boolean clamp, int attachments)
	{
		fileName = "";
		resource = new TextureData(textureTarget, width, height, 1, data, filters, internalFormat, format, clamp, attachments);
	}

	public Texture(String fileName)
	{
		this(fileName, GL_TEXTURE_2D);
	}

	public Texture(String fileName, int textureTarget)
	{
		this(fileName, textureTarget, GL_LINEAR_MIPMAP_LINEAR);
	}

	public Texture(String fileName, int textureTarget, int filters)
	{
		this(fileName, textureTarget, filters, GL_RGBA);
	}

	public Texture(String fileName, int textureTarget, int filters, int internalFormat)
	{
		this(fileName, textureTarget, filters, internalFormat, GL_RGBA);
	}

	public Texture(String fileName, int textureTarget, int filters, int internalFormat, int format)
	{
		this(fileName, textureTarget, filters, internalFormat, format, false);
	}

	public Texture(String fileName, int textureTarget, int filters, int internalFormat, int format, boolean clamp)
	{
		this(fileName, textureTarget, filters, internalFormat, format, clamp, GL_NONE);
	}

	public Texture(String fileName, int textureTarget, int filters, int internalFormat, int format, boolean clamp, int attachments)
	{
		this.fileName = fileName;

		TextureData existingResource = resourceMap.get(fileName);

		if(existingResource != null)
		{
			resource = existingResource;
			resource.addReference();
		}
		else
		{
			loadTexture(fileName, textureTarget, filters, internalFormat, format, clamp, attachments);
		}
	}

	public Texture(Texture other)
	{
		fileName = other.fileName;
		resource = other.resource;

		resource.addReference();
	}

	public void destroy()
	{
		if(resource.removeReference() && !fileName.isEmpty())
		{
			resourceMap.remove(fileName);
		}
	}

	public void bind()
	{
		bind(0);
	}

	public void bind(int unit)
	{
		if(unit < 0 || unit >= 32)
		{
			throw new IllegalArgumentException("The unit " + unit + " is out of bounds\n");
		}

		glActiveTexture(GL_TEXTURE0 + unit);
		resource.bind(0);
	}

	public void bindAsRenderTarget()
	{
		resource.bindAsRenderTarget();
	}

	public int getWidth()
	{
		return resource.getWidth();
	}

	public int getHeight()
	{
		return resource.getHeight();
	}

	private void loadTexture(String fileName, int textureTarget, int filters, int internalFormat, int format, boolean clamp, int attachments)
	{
		try
		{
			BufferedImage image = ImageIO.read(new File(TEXTURE_FOLDER + fileName));
			int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());

			ByteBuffer data = Buffer.createByteBuffer(image.getHeight() * image.getWidth() * 4);

			boolean hasAlpha = image.getColorModel().hasAlpha();

			// Put each pixel in a Byte Buffer
			for(int y = 0; y < image.getHeight(); y++)
			{
				for(int x = 0; x < image.getWidth(); x++)
				{
					int pixel = pixels[y * image.getWidth() + x];

					byte alphaByte = hasAlpha ? (byte) ((pixel >> 24) & 0xFF) : (byte) (0xFF);

					data.put((byte) ((pixel >> 16) & 0xFF));
					data.put((byte) ((pixel >> 8) & 0xFF));
					data.put((byte) ((pixel) & 0xFF));
					data.put(alphaByte);
				}
			}

			data.flip();

			resource = new TextureData(textureTarget, image.getWidth(), image.getHeight(), 1, data, filters, internalFormat, format, clamp, attachments);

			resourceMap.put(fileName, resource);
		}
		catch(IOException e)
		{
			System.err.println("Failed to load texture: " + fileName + " doesn't exist");
		}
	}
}