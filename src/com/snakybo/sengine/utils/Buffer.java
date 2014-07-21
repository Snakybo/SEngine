package com.snakybo.sengine.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;

import com.snakybo.sengine.utils.math.Matrix4f;
import com.snakybo.sengine.utils.math.Vector2f;
import com.snakybo.sengine.utils.math.Vector3f;

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
	
	public static IntBuffer createFlippedBufferi(List<Integer> values) {
		return createFlippedBuffer(Utils.toIntArray(values.toArray(new Integer[values.size()])));
	}
	
	public static FloatBuffer createFlippedBufferV2(List<Vector2f> values) {
		FloatBuffer buffer = createFloatBuffer(values.size() * 2);
		
		for(int i = 0; i < values.size(); i++) {
			Vector2f value = values.get(i);
			
			buffer.put(value.x);
			buffer.put(value.y);
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBufferV3(List<Vector3f> values) {
		FloatBuffer buffer = createFloatBuffer(values.size() * 3);
		
		for(int i = 0; i < values.size(); i++) {
			Vector3f value = values.get(i);
			
			buffer.put(value.x);
			buffer.put(value.y);
			buffer.put(value.z);
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
