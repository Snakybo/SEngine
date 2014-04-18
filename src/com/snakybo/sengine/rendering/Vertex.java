package com.snakybo.sengine.rendering;

import com.snakybo.sengine.core.utils.Vector2f;
import com.snakybo.sengine.core.utils.Vector3f;

/** Vertex class
 * 
 * @author Kevin Krol
 * @since Apr 5, 2014 */
public class Vertex {
	public static final int SIZE = 11;
	
	private Vector3f position;
	private Vector2f texCoord;
	private Vector3f normal;
	private Vector3f tangent;
	
	/** Constructor for the vertex
	 * @param position The position of the vertex */
	public Vertex(Vector3f position) {
		this(position, new Vector2f(0, 0));
	}
	
	/** Constructor for the vertex
	 * @param position The position of the vertex
	 * @param texCoord The texture coordinates of the vertex */
	public Vertex(Vector3f position, Vector2f texCoord) {
		this(position, texCoord, new Vector3f(0, 0, 0));
	}
	
	/** Constructor for the vertex
	 * @param position The position of the vertex
	 * @param texCoord The texture coordinates of the vertex
	 * @param normal The normal coordinates of the vertex */
	public Vertex(Vector3f position, Vector2f texCoord, Vector3f normal) {
		this(position, texCoord, normal, new Vector3f(0, 0, 0));
	}
	
	/** Constructor for the vertex
	 * @param position The position of the vertex
	 * @param texCoord The texture coordinates of the vertex
	 * @param normal The normal coordinates of the vertex
	 * @param tangent The tangent of the vertex */
	public Vertex(Vector3f position, Vector2f texCoord, Vector3f normal, Vector3f tangent) {
		this.position = position;
		this.texCoord = texCoord;
		this.normal = normal;
		this.tangent = tangent;
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
	
	/** Set the normal position of the vertex
	 * @param normal The new normal position */
	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}
	
	/** Set the tangent of the vertex
	 * @param tangent The new tangent position */
	public void setTangent(Vector3f tangent) {
		this.normal = tangent;
	}
	
	/** @return The position of the vertex */
	public Vector3f getPosition() {
		return position;
	}
	
	/** @return The texture coordinates of the vertex */
	public Vector2f getTexCoord() {
		return texCoord;
	}
	
	/** @return The normal position of the vertex */
	public Vector3f getNormal() {
		return normal;
	}
	
	/** @return The tangent of the vertex */
	public Vector3f getTangent() {
		return tangent;
	}
}
