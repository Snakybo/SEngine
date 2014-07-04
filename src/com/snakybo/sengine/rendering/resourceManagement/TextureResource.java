package com.snakybo.sengine.rendering.resourceManagement;

import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
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

public class TextureResource {
		private int[] textureIds;
		
		private int textureType;
		
		private int textureWidth;
		private int textureHeight;
		
		private int frameBuffer;
		private int renderBuffer;
		
		private int textureCount;
		private int refCount;
		
		public TextureResource(ByteBuffer buffer, int textureType, int filters, int wraps, int[] attachments, int width, int height, int textureCount) {
			textureIds = new int[textureCount];
			
			this.textureType = textureType;
			this.textureWidth = width;
			this.textureHeight = height;
			this.textureCount = textureCount;
			this.frameBuffer = 0;
			this.renderBuffer = 0;
			
			refCount = 1;
			
			initTextures(buffer, filters, wraps);
			initRenderTargets(attachments);
		}
		
		@Override
		protected void finalize() {
			if(frameBuffer != 0)
				glDeleteFramebuffers(frameBuffer);
			
			if(renderBuffer != 0)
				glDeleteRenderbuffers(renderBuffer);
			
			for(int textureId : textureIds)
				glDeleteBuffers(textureId);
		}
		
		public void bind(int textureNum) {
			glBindTexture(textureType, textureIds[textureNum]);
		}
		
		public void bindAsRenderTarget() {
			glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer);
			glViewport(0, 0, textureWidth, textureHeight);
		}
		
		public void addReference() {
			refCount++;
		}
		
		public boolean removeReference() {
			refCount--;
			
			return refCount == 0;
		}
		
		private void initTextures(ByteBuffer buffer, int filters, int wraps) {
			for(int i = 0; i < textureCount; i++) {
				textureIds[i] = glGenTextures();
				
				glBindTexture(textureType, textureIds[i]);
				
				if(wraps != 0) {
					glTexParameteri(textureType, GL_TEXTURE_WRAP_S, wraps);
					glTexParameteri(textureType, GL_TEXTURE_WRAP_T, wraps);
				}
				
				glTexParameterf(textureType, GL_TEXTURE_MIN_FILTER, filters);
				glTexParameterf(textureType, GL_TEXTURE_MAG_FILTER, filters);
				
				glTexImage2D(textureType, 0, GL_RGBA8, textureWidth, textureHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
				
				if(filters == GL_NEAREST_MIPMAP_NEAREST ||
						filters == GL_NEAREST_MIPMAP_LINEAR ||
						filters == GL_LINEAR_MIPMAP_NEAREST ||
						filters == GL_LINEAR_MIPMAP_LINEAR) {
					glGenerateMipmap(textureType);
					
					glTexParameterf(textureType, GL_TEXTURE_MAX_ANISOTROPY_EXT, glGetFloat(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
				}
			}
		}
		
		private void initRenderTargets(int[] attachments) {
			if(attachments == null)
				return;
			
			IntBuffer drawBuffers = IntBuffer.allocate(textureCount);
			boolean hasDepth = false;
			
			for(int i = 0; i < textureCount; i++) {
				if(attachments[i] == GL_DEPTH_ATTACHMENT) {
					drawBuffers.put(GL_NONE);
					hasDepth = true;
				} else {
					drawBuffers.put(attachments[i]);
				}
				
				if(attachments[i] == GL_NONE)
					continue;
				
				if(frameBuffer == 0) {
					frameBuffer = glGenFramebuffers();
					
					glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer);
				}
				
				glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, attachments[i], textureType, textureIds[i], 0);
			}
			
			if(frameBuffer == 0)
				return;
			
			if(!hasDepth) {
				renderBuffer = glGenRenderbuffers();
				
				glBindRenderbuffer(GL_RENDERBUFFER, renderBuffer);
				glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, textureWidth, textureHeight);
				glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderBuffer);
			}
			
			glDrawBuffers(drawBuffers);
			
			if(glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
				System.err.println("Framebuffer creation failed");
				System.exit(1);
			}
		}
	}