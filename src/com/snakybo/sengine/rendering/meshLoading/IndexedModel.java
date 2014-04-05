package com.snakybo.sengine.rendering.meshLoading;

import java.util.ArrayList;

import com.snakybo.sengine.core.Vector2f;
import com.snakybo.sengine.core.Vector3f;

/** Indexed model
 * 
 * Used for mesh loading
 * 
 * @author Kevin Krol
 * @since Apr 5, 2014 */
public class IndexedModel {
	private ArrayList<Vector3f> positions;
	private ArrayList<Vector2f> texCoords;
	private ArrayList<Vector3f> normals;
	private ArrayList<Integer> indices;
	
	/** Constructor for the indexed model */
	public IndexedModel() {
		positions = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		indices = new ArrayList<Integer>();
	}
	
	/** Calculate the normals of the indexed model */
	public void calcNormals() {
		for(int i = 0; i < indices.size(); i += 3) {
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);
			
			Vector3f v1 = positions.get(i1).sub(positions.get(i0));
			Vector3f v2 = positions.get(i2).sub(positions.get(i0));
			
			Vector3f normal = v1.cross(v2).normalize();
			
			normals.get(i0).set(normals.get(i0).add(normal));
			normals.get(i1).set(normals.get(i1).add(normal));
			normals.get(i2).set(normals.get(i2).add(normal));
		}
		
		for(int i = 0; i < normals.size(); i++)
			normals.get(i).set(normals.get(i).normalize());
	}
	
	/** @return The positions of the indexed model */
	public ArrayList<Vector3f> getPositions() {
		return positions;
	}
	
	/** @return The texture coordinates of the indexed model */
	public ArrayList<Vector2f> getTexCoords() {
		return texCoords;
	}
	
	/** @return The normals of the indexed model */
	public ArrayList<Vector3f> getNormals() {
		return normals;
	}
	
	/** @return The indices of the indexed model */
	public ArrayList<Integer> getIndices() {
		return indices;
	}
}
