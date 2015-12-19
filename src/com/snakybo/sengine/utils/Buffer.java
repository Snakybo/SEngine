package com.snakybo.sengine.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.math.Vector3f;

public class Buffer
{
	public static FloatBuffer createFloatBuffer(int size)
	{
		return BufferUtils.createFloatBuffer(size);
	}

	public static IntBuffer createIntBuffer(int size)
	{
		return BufferUtils.createIntBuffer(size);
	}

	public static ByteBuffer createByteBuffer(int size)
	{
		return BufferUtils.createByteBuffer(size);
	}

	public static IntBuffer createFlippedBufferi(Iterable<Integer> values, int count)
	{
		IntBuffer buffer = createIntBuffer(count);
		
		for(Integer value : values)
		{
			buffer.put(value);
		}
		
		buffer.flip();
		return buffer;
	}

	public static FloatBuffer createFlippedBuffer2f(Iterable<Vector2f> values, int count)
	{
		FloatBuffer buffer = createFloatBuffer(count * 2);
		
		for(Vector2f value : values)
		{
			buffer.put(value.x);
			buffer.put(value.y);
		}

		buffer.flip();
		return buffer;
	}

	public static FloatBuffer createFlippedBuffer3f(Iterable<Vector3f> values, int count)
	{
		FloatBuffer buffer = createFloatBuffer(count * 3);
		
		for(Vector3f value : values)
		{
			buffer.put(value.x);
			buffer.put(value.y);
			buffer.put(value.z);
		}
		
		buffer.flip();
		return buffer;
	}

	public static FloatBuffer createFlippedBuffer(Matrix4f value)
	{
		FloatBuffer buffer = createFloatBuffer(4 * 4);

		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				buffer.put(value.get(i, j));
			}
		}

		buffer.flip();
		return buffer;
	}
}
