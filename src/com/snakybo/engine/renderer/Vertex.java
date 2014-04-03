package com.snakybo.engine.renderer;

import com.snakybo.engine.math.Vector2f;
import com.snakybo.engine.math.Vector3f;

/** @author Kevin Krol */
public class Vertex {
	/** The total size of the vertex: {@value} */
	public static final int SIZE = 8;
	
	private Vector3f position;
	private Vector2f texCoord;
	private Vector3f normal;
	
	/** Create a new vertex
	 * @param position The position of the vertex */
	public Vertex(Vector3f pos) {
		this(pos, new Vector2f());
	}
	
	/** Create a new vertex
	 * @param position The position of the vertex
	 * @param texCoord The texture coordinates of the vertex */
	public Vertex(Vector3f pos, Vector2f texCoord) {
		this(pos, texCoord, new Vector3f());
	}
	
	/** Create a new vertex
	 * @param position The position of the vertex
	 * @param texCoord The texture coordinates of the vertex
	 * @param normal The normals of the vertex */
	public Vertex(Vector3f position, Vector2f texCoord, Vector3f normal) {
		this.position = position;
		this.texCoord = texCoord;
		this.normal = normal;
	}
	
	/** Set the position of the vertex
	 * @param position The new position */
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	/** Set the texture coordinates of the vertex
	 * @param texCoord The new texture coordinates */
	public void setTexCoord(Vector2f texCoord) {
		this.texCoord = texCoord;
	}
	
	/** Set the normals of the vertex
	 * @param normal The new normals */
	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}
	
	/** @return The position of the vertex */
	public Vector3f getPosition() {
		return position;
	}
	
	/** @return The texture coordinates of the vertex */
	public Vector2f getTexCoord() {
		return texCoord;
	}
	
	/** @return The normals of the vertex */
	public Vector3f getNormal() {
		return normal;
	}
}
