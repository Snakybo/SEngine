package com.snakybo.sengine.texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.resource.management.TextureData;

public class Texture
{
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
		OpenGLTexture texture = new OpenGLTexture(fileName);
			
		resource = new TextureData(textureTarget, texture.getWidth(), texture.getHeight(), 1, texture.getData(), filters, internalFormat, format, clamp, attachments);
		resourceMap.put(fileName, resource);
	}
}