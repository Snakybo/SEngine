package com.snakybo.sengine.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.snakybo.sengine.rendering.Vertex;
import com.snakybo.sengine.utils.math.Matrix4f;

public class Buffer {
	public static FloatBuffer createFloatBuffer(int size) {
		return BufferUtils.createFloatBuffer(size);
	}
	
	public static IntBuffer createIntBuffer(int size) {
		return BufferUtils.createIntBuffer(size);
	}
	
	public static ByteBuffer createByteBuffer(int size) {
		return BufferUtils.createByteBuffer(size);
	}
	
	public static IntBuffer createFlippedBuffer(int... values) {
		IntBuffer buffer = createIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Vertex[] vertices) {
		FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.SIZE);
		
		for(int i = 0; i < vertices.length; i++) {
			buffer.put(vertices[i].getPosition().x);
			buffer.put(vertices[i].getPosition().y);
			buffer.put(vertices[i].getPosition().z);
			
			buffer.put(vertices[i].getTexCoord().x);
			buffer.put(vertices[i].getTexCoord().y);
			
			buffer.put(vertices[i].getNormal().x);
			buffer.put(vertices[i].getNormal().y);
			buffer.put(vertices[i].getNormal().z);
			
			buffer.put(vertices[i].getTangent().x);
			buffer.put(vertices[i].getTangent().y);
			buffer.put(vertices[i].getTangent().z);
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Matrix4f value) {
		FloatBuffer buffer = createFloatBuffer(4 * 4);
		
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				buffer.put(value.get(i, j));
		
		buffer.flip();
		
		return buffer;
	}
}
