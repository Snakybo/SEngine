package com.snakybo.sengine.resource.texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import java.nio.ByteBuffer;

import com.snakybo.sengine.core.Time;
import com.snakybo.sengine.resource.ResourceManager;
import com.snakybo.sengine.resource.texture.TextureLoader.TextureData;

public final class Texture
{
	private final String fileName;	
	private final TextureResource resource;

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

	public Texture(int width, int height, ByteBuffer data, int target)
	{
		this(width, height, data, target, GL_LINEAR_MIPMAP_LINEAR);
	}

	public Texture(int width, int height, ByteBuffer data, int target, int filters)
	{
		this(width, height, data, target, filters, GL_RGBA);
	}

	public Texture(int width, int height, ByteBuffer data, int target, int filters, int internalFormat)
	{
		this(width, height, data, target, filters, internalFormat, GL_RGBA);
	}

	public Texture(int width, int height, ByteBuffer data, int target, int filters, int internalFormat, int format)
	{
		this(width, height, data, target, filters, internalFormat, format, false);
	}

	public Texture(int width, int height, ByteBuffer data, int target, int filters, int internalFormat, int format, boolean clamp)
	{
		this(width, height, data, target, filters, internalFormat, format, clamp, GL_NONE);
	}

	public Texture(int width, int height, ByteBuffer data, int target, int filters, int internalFormat, int format, boolean clamp, int attachments)
	{
		fileName = "texture_" + Time.getCurrentTimeMillis();
		resource = new TextureResource(target, width, height, data, filters, internalFormat, format, clamp, attachments);
		ResourceManager.add(fileName, resource);
	}

	public Texture(String fileName)
	{
		this(fileName, GL_TEXTURE_2D);
	}

	public Texture(String fileName, int target)
	{
		this(fileName, target, GL_LINEAR_MIPMAP_LINEAR);
	}

	public Texture(String fileName, int target, int filters)
	{
		this(fileName, target, filters, GL_RGBA);
	}

	public Texture(String fileName, int target, int filters, int internalFormat)
	{
		this(fileName, target, filters, internalFormat, GL_RGBA);
	}

	public Texture(String fileName, int target, int filters, int internalFormat, int format)
	{
		this(fileName, target, filters, internalFormat, format, false);
	}

	public Texture(String fileName, int target, int filters, int internalFormat, int format, boolean clamp)
	{
		this(fileName, target, filters, internalFormat, format, clamp, GL_NONE);
	}

	public Texture(String fileName, int target, int filters, int internalFormat, int format, boolean clamp, int attachments)
	{
		this.fileName = fileName;
		
		if(ResourceManager.has(fileName))
		{
			ResourceManager.add(fileName);
			resource = ResourceManager.get(TextureResource.class, fileName);
		}
		else
		{
			TextureData textureData = TextureLoader.loadTexture(fileName);
			resource = new TextureResource(target, textureData.getWidth(), textureData.getHeight(), textureData.getData(), filters, internalFormat, format, clamp, attachments);
			ResourceManager.add(fileName, resource);
		}
	}

	public Texture(Texture other)
	{
		fileName = other.fileName;
		resource = other.resource;

		ResourceManager.add(fileName);
	}
	
	@Override
	public Texture clone()
	{
		return new Texture(this);
	}

	public void destroy()
	{
		ResourceManager.remove(fileName);
	}

	public void bind()
	{
		bind(0);
	}

	public void bind(int unit)
	{
		resource.bind(unit);
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
}