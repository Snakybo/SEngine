package com.snakybo.sengine.resource.texture;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glGetFloat;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameterf;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_BASE_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
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

import com.snakybo.sengine.math.MathUtils;
import com.snakybo.sengine.resource.IResource;

public final class TextureResource implements IResource
{
	private int id;
	private int target;
	private int frameBuffer;
	private int renderBuffer;
	private int width;
	private int height;

	public TextureResource(int target, int width, int height, ByteBuffer data, int filters, int internalFormat, int format, boolean clamp, int attachments)
	{
		this.target = target;
		this.width = width;
		this.height = height;
		
		initTextures(data, filters, internalFormat, format, clamp);
		initRenderTargets(attachments);
	}
	
	@Override
	public void destroy()
	{
		glDeleteTextures(id);
		glDeleteFramebuffers(frameBuffer);
		glDeleteRenderbuffers(renderBuffer);
	}
	
	public void bind(int unit)
	{
		if(unit < 0 || unit >= 32)
		{
			throw new IllegalArgumentException("[Texture] The unit " + unit + " is out of bounds\n");
		}

		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(target, id);
	}

	public void bindAsRenderTarget()
	{
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		glViewport(0, 0, width, height);
	}
	
	private void initTextures(ByteBuffer data, int filters, int internalFormat, int format, boolean clamp)
	{
		id = glGenTextures();
		glBindTexture(target, id);

		glTexParameterf(target, GL_TEXTURE_MIN_FILTER, filters);
		glTexParameterf(target, GL_TEXTURE_MAG_FILTER, filters);

		if(clamp)
		{
			glTexParameteri(target, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(target, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
			glTexParameteri(target, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
		}

		glTexImage2D(target, 0, internalFormat, width, height, 0, format, GL_UNSIGNED_BYTE, data);

		boolean mipmapEnabled = filters == GL_NEAREST_MIPMAP_NEAREST || filters == GL_NEAREST_MIPMAP_LINEAR	|| filters == GL_LINEAR_MIPMAP_NEAREST || filters == GL_LINEAR_MIPMAP_LINEAR;
		if(mipmapEnabled)
		{
			glGenerateMipmap(target);

			float maxAnisotropic = MathUtils.clamp(glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT), 0f, 8f);
			glTexParameterf(target, GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropic);
		}
		else
		{
			glTexParameteri(target, GL_TEXTURE_BASE_LEVEL, 0);
			glTexParameteri(target, GL_TEXTURE_MAX_LEVEL, 0);
		}
	}

	private void initRenderTargets(int attachment)
	{
		if(attachment == GL_NONE)
		{
			return;
		}
		
		int drawBuffer;
		
		frameBuffer = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, frameBuffer);
		glFramebufferTexture2D(GL_FRAMEBUFFER, attachment, target, id, 0);
		
		if(attachment == GL_DEPTH_ATTACHMENT)
		{
			drawBuffer = GL_NONE;
		}
		else
		{
			renderBuffer = glGenRenderbuffers();
			drawBuffer = attachment;
			
			glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
			glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
			glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);			
		}
		
		glDrawBuffers(drawBuffer);
		
		if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
		{
			System.err.println("[Texture] Unable to create Framebuffer");
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