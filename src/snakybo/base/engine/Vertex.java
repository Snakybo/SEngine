package snakybo.base.engine;

public class Vertex {
	public static final int SIZE = 8;
	
	private Vector3f pos;
	private Vector2f texCoord;
	private Vector3f normal;
	
	public Vertex(Vector3f pos) {
		this(pos, new Vector2f(0, 0));
	}
	
	public Vertex(Vector3f pos, Vector2f texCoord) {
		this(pos, texCoord, new Vector3f(0,0,0));
	}
	
	public Vertex(Vector3f pos, Vector2f texCoord, Vector3f normal) {
		this.pos = pos;
		this.texCoord = texCoord;
		this.normal = normal;
	}
	
	/** Set pos */
	public void setPos(Vector3f pos) {
		this.pos = pos;
	}
	
	/** Set texture coordinates */
	public void setTexCoord(Vector2f texCoord) {
		this.texCoord = texCoord;
	}
	
	/** Set normal */
	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}

	/** @return Vector3f: Position of Vertex */
	public Vector3f getPos() {
		return pos;
	}

	/** @return Vector2f: Texture coordinates */
	public Vector2f getTexCoord() {
		return texCoord;
	}
	
	/** @return Vector3f: Normal value */
	public Vector3f getNormal() {
		return normal;
	}
}
