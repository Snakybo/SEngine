package com.snakybo.sengine.resource.management;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_BASE_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_RENDERBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindRenderbuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glDeleteRenderbuffers;
import static org.lwjgl.opengl.GL30.glFramebufferRenderbuffer;
import static org.lwjgl.opengl.GL30.glFramebufferTexture2D;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL30.glGenRenderbuffers;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL30.glRenderbufferStorage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.snakybo.sengine.utils.IReferenceCounter;
import com.snakybo.sengine.utils.math.MathUtils;

public class TextureData implements IReferenceCounter
{
	private IntBuffer textureIds;

	private int textureTarget;
	private int frameBuffer;
	private int renderBuffer;
	private int numTextures;
	private int width;
	private int height;

	private int refCount;

	public TextureData(int textureTarget, int width, int height, int numTextures, ByteBuffer data, int filters, int internalFormat, int format, boolean clamp, int attachments)
	{
		this.textureIds = BufferUtils.createIntBuffer(numTextures);
		this.textureTarget = textureTarget;
		this.numTextures = numTextures;
		this.width = width;
		this.height = height;

		frameBuffer = 0;
		renderBuffer = 0;
		refCount = 0;

		initTextures(data, filters, internalFormat, format, clamp);
		initRenderTargets(attachments);
	}

	@Override
	protected void finalize() throws Throwable
	{
		try
		{
			glDeleteFramebuffers(frameBuffer);
			glDeleteRenderbuffers(renderBuffer);

			for(int textureId : textureIds.array())
			{
				glDeleteBuffers(textureId);
			}
		}
		finally
		{
			super.finalize();
		}
	}

	@Override
	public void addReference()
	{
		refCount++;
	}

	@Override
	public boolean removeReference()
	{
		refCount--;

		return refCount == 0;
	}

	@Override
	public int getReferenceCount()
	{
		return refCount;
	}

	public void bind(int textureNum)
	{
		glBindTexture(textureTarget, textureIds.get(textureNum));
	}

	public void bindAsRenderTarget()
	{
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer);
		glViewport(0, 0, width, height);
	}

	private void initTextures(ByteBuffer data, int filters, int internalFormat, int format, boolean clamp)
	{
		glGenTextures(textureIds);

		for(int i = 0; i < numTextures; i++)
		{
			glBindTexture(textureTarget, textureIds.get(i));

			glTexParameterf(textureTarget, GL_TEXTURE_MIN_FILTER, filters);
			glTexParameterf(textureTarget, GL_TEXTURE_MAG_FILTER, filters);

			if(clamp)
			{
				glTexParameteri(textureTarget, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
				glTexParameteri(textureTarget, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			}

			glTexImage2D(textureTarget, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);

			boolean mipmapEnabled = filters == GL_NEAREST_MIPMAP_NEAREST || filters == GL_NEAREST_MIPMAP_LINEAR	|| filters == GL_LINEAR_MIPMAP_NEAREST || filters == GL_LINEAR_MIPMAP_LINEAR;

			if(mipmapEnabled)
			{
				glGenerateMipmap(textureTarget);

				float maxAnisotropic = MathUtils.clamp(glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT), 0.0f, 8.0f);
				assert (maxAnisotropic >= 0.0f && maxAnisotropic <= 8.0f);

				glTexParameterf(textureTarget, GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropic);
			}
			else
			{
				glTexParameteri(textureTarget, GL_TEXTURE_BASE_LEVEL, 0);
				glTexParameteri(textureTarget, GL_TEXTURE_MAX_LEVEL, 0);
			}
		}
	}

	private void initRenderTargets(int attachments)
	{
		if(attachments == 0)
		{
			return;
		}

		IntBuffer drawBuffers = BufferUtils.createIntBuffer(32);
		assert (numTextures <= 32);

		boolean hasDepth = false;

		for(int i = 0; i < numTextures; i++)
		{
			if(attachments == GL_DEPTH_ATTACHMENT)
			{
				drawBuffers.put(GL_NONE);
				hasDepth = true;
			}
			else
			{
				drawBuffers.put(attachments);
			}

			if(attachments == GL_NONE)
			{
				continue;
			}

			if(frameBuffer == 0)
			{
				frameBuffer = glGenFramebuffers();
				glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer);
			}

			glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, attachments, textureTarget, textureIds.get(i), 0);
		}

		if(frameBuffer == 0)
		{
			return;
		}

		if(!hasDepth)
		{
			renderBuffer = glGenRenderbuffers();

			glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
			glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
			glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);
		}

		glDrawBuffers(drawBuffers);

		if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
		{
			System.err.println("Framebuffer creation failed");
			System.exit(1);
		}

		glBindFramebuffer(GL_FRAMEBUFFER, 0);
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