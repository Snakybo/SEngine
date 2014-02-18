package com.snakybo.engine.renderer;

import com.snakybo.engine.core.Vector2f;
import com.snakybo.engine.core.Vector3f;

public class Vertex {
	public static final int SIZE = 8;
	
	private Vector3f pos;
	private Vector2f texCoord;
	private Vector3f normal;
	
	public Vertex(Vector3f pos) {
		this(pos, Vector2f.ZERO);
	}
	
	public Vertex(Vector3f pos, Vector2f texCoord) {
		this(pos, texCoord, Vector3f.ZERO);
	}
	
	public Vertex(Vector3f pos, Vector2f texCoord, Vector3f normal) {
		this.pos = pos;
		this.texCoord = texCoord;
		this.normal = normal;
	}
	
	public void setPos(Vector3f pos) { this.pos = pos; }
	public void setTexCoord(Vector2f texCoord) { this.texCoord = texCoord; }
	public void setNormal(Vector3f normal) { this.normal = normal; }

	public Vector3f getPos() { return pos; }
	public Vector2f getTexCoord() { return texCoord; }
	public Vector3f getNormal() { return normal; }
}
