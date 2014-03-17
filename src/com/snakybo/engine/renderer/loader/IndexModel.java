package com.snakybo.engine.renderer.loader;

import java.util.ArrayList;
import java.util.List;

import com.snakybo.engine.core.Vector2f;
import com.snakybo.engine.core.Vector3f;

/** @author Kevin Krol
 * @since Mar 12, 2014 */
public class IndexModel {
	private List<Vector3f> positions;
	private List<Vector2f> texCoords;
	private List<Vector3f> normals;
	private List<Integer> indices;
	
	public IndexModel() {
		positions = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		indices = new ArrayList<Integer>();
	}
	
	/** Calculate normals */
	public void calculateNormals() {
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
	
	public List<Vector3f> getPositions() {
		return positions;
	}
	
	public List<Vector2f> getTexCoords() {
		return texCoords;
	}
	
	public List<Vector3f> getNormals() {
		return normals;
	}
	
	public List<Integer> getIndices() {
		return indices;
	}
}
