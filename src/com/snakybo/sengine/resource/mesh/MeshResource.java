package com.snakybo.sengine.resource.mesh;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

import com.snakybo.sengine.resource.IResource;
import com.snakybo.sengine.resource.mesh.loader.ReadableModel;
import com.snakybo.sengine.utils.Buffer;

/**
 * @author Kevin Krol
 * @since Jul 8, 2014
 */
public class MeshResource implements IResource
{
	private static final int NUM_BUFFERS = 5;

	private static final int POSITION_VB = 0;
	private static final int TEXCOORD_VB = 1;
	private static final int NORMAL_VB = 2;
	private static final int TANGENT_VB = 3;
	private static final int INDEX_VB = 4;

	private IntBuffer vertexArrayObject;
	private IntBuffer vertexArrayBuffers;

	private int drawCount;

	public MeshResource(ReadableModel model)
	{
		if(!model.isValid())
		{
			throw new RuntimeException("[Mesh] Invalid model");
		}
		
		FloatBuffer positionBuffer = Buffer.createFlippedBuffer3f(model.getPositions(), model.getNumVertices());
		FloatBuffer texCoordBuffer = Buffer.createFlippedBuffer2f(model.getTexCoords(), model.getNumVertices());
		FloatBuffer normalBuffer = Buffer.createFlippedBuffer3f(model.getNormals(), model.getNumVertices());
		FloatBuffer tangentBuffer = Buffer.createFlippedBuffer3f(model.getTangents(), model.getNumVertices());
		IntBuffer indexBuffer = Buffer.createFlippedBufferi(model.getIndices(), model.getNumIndices());
		
		initialize(positionBuffer, texCoordBuffer, normalBuffer, tangentBuffer, indexBuffer, model.getNumIndices());
	}
	
	private void initialize(FloatBuffer positions, FloatBuffer texCoords, FloatBuffer normals, FloatBuffer tangents, IntBuffer indices, int drawCount)
	{
		this.vertexArrayObject = BufferUtils.createIntBuffer(1);
		this.vertexArrayBuffers = BufferUtils.createIntBuffer(NUM_BUFFERS);
		this.drawCount = drawCount;
		
		glGenVertexArrays(vertexArrayObject);
		glBindVertexArray(vertexArrayObject.get(0));
		
		glGenBuffers(vertexArrayBuffers);
		
		glBindBuffer(GL_ARRAY_BUFFER, vertexArrayBuffers.get(POSITION_VB));
		glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);
		
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0L);
		
		glBindBuffer(GL_ARRAY_BUFFER, vertexArrayBuffers.get(TEXCOORD_VB));
		glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
		
		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0L);
		
		glBindBuffer(GL_ARRAY_BUFFER, vertexArrayBuffers.get(NORMAL_VB));
		glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
		
		glEnableVertexAttribArray(2);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0L);
		
		glBindBuffer(GL_ARRAY_BUFFER, vertexArrayBuffers.get(TANGENT_VB));
		glBufferData(GL_ARRAY_BUFFER, tangents, GL_STATIC_DRAW);
		
		glEnableVertexAttribArray(3);
		glVertexAttribPointer(3, 3, GL_FLOAT, false, 0, 0L);
		
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vertexArrayBuffers.get(INDEX_VB));
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
	}

	@Override
	public void destroy()
	{
		glDeleteBuffers(vertexArrayBuffers);
		glDeleteVertexArrays(vertexArrayObject);
	}

	public void draw(int mode)
	{
		glBindVertexArray(vertexArrayObject.get(0));
		glDrawElements(mode, drawCount, GL_UNSIGNED_INT, 0);
	}
}
