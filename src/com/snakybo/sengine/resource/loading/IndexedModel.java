package com.snakybo.sengine.resource.loading;

import java.util.ArrayList;
import java.util.List;

import com.snakybo.sengine.utils.math.Vector2f;
import com.snakybo.sengine.utils.math.Vector3f;

// FIXME: Normal maps aren't working anymore

/** @author Kevin Krol
 * @since Jul 8, 2014 */
public class IndexedModel {
	private List<Integer> indices;
	private List<Vector3f> positions;
	private List<Vector2f> texCoords;
	private List<Vector3f> normals;
	private List<Vector3f> tangents;
	
	public IndexedModel() {
		this(new ArrayList<Integer>(), new ArrayList<Vector3f>(), new ArrayList<Vector2f>());
	}
	
	public IndexedModel(List<Integer> indices, List<Vector3f> positions, List<Vector2f> texCoords) {
		this(indices, positions, texCoords, new ArrayList<Vector3f>());
	}
	
	public IndexedModel(List<Integer> indices, List<Vector3f> positions, List<Vector2f> texCoords, List<Vector3f> normals) {
		this(indices, positions, texCoords, normals, new ArrayList<Vector3f>());
	}
	
	public IndexedModel(List<Integer> indices, List<Vector3f> positions, List<Vector2f> texCoords, List<Vector3f> normals, List<Vector3f> tangents) {
		this.indices = indices;
		this.positions = positions;
		this.texCoords = texCoords;
		this.normals = normals;
		this.tangents = tangents;
	}
	
	public boolean isValid() {
		return	positions.size() == texCoords.size() &&
				texCoords.size() == normals.size() &&
				normals.size() == tangents.size();
	}
	
	public void calcNormals() {
		normals.clear();
		
		for(int i = 0; i < positions.size(); i++)
			normals.add(new Vector3f(0.0f, 0.0f, 0.0f));
		
		for(int i = 0; i < indices.size(); i += 3) {
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);
			
			Vector3f v1 = positions.get(i1).sub(positions.get(i0));
			Vector3f v2 = positions.get(i2).sub(positions.get(i0));
			
			Vector3f normal = v1.cross(v2).normalized();
			
			normals.get(i0).set(normals.get(i0).add(normal));
			normals.get(i1).set(normals.get(i1).add(normal));
			normals.get(i2).set(normals.get(i2).add(normal));
		}
		
		for(int i = 0; i < normals.size(); i++)
			normals.get(i).set(normals.get(i).normalized());
	}
	
	public void calcTangents() {
		tangents.clear();
		
		for(int i = 0; i < positions.size(); i++)
			tangents.add(new Vector3f(0.0f, 0.0f, 0.0f));
		
		for(int i = 0; i < indices.size(); i += 3) {
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);
			
			Vector3f edge1 = positions.get(i1).sub(positions.get(i0));
			Vector3f edge2 = positions.get(i2).sub(positions.get(i0));
			
			float deltaU1 = texCoords.get(i1).x - texCoords.get(i0).x;
			float deltaV1 = texCoords.get(i1).y - texCoords.get(i0).y;
			float deltaU2 = texCoords.get(i2).x - texCoords.get(i0).x;
			float deltaV2 = texCoords.get(i2).y - texCoords.get(i0).y;
			
			float dividend = (deltaU1 * deltaV2 - deltaU2 * deltaV1);
			float f = dividend == 0 ? 0.0f : 1.0f / dividend;
			
			Vector3f tangent = new Vector3f(0.0f, 0.0f, 0.0f);
			tangent.x = f * (deltaV2 * edge1.x - deltaV1 * edge2.x);
			tangent.y = f * (deltaV2 * edge1.y - deltaV1 * edge2.y);
			tangent.z = f * (deltaV2 * edge1.z - deltaV1 * edge2.z);
			
			tangents.get(i0).set(tangents.get(i0).add(tangent));
			tangents.get(i1).set(tangents.get(i1).add(tangent));
			tangents.get(i2).set(tangents.get(i2).add(tangent));
		}
		
		for(int i = 0; i < tangents.size(); i++)
			tangents.get(i).set(tangents.get(i).normalized());
	}
	
	public IndexedModel finish() {
		if(isValid())
			return this;
		
		if(texCoords.size() == 0)
			for(int i = 0; i < positions.size(); i++)
				addTexCoord(new Vector2f(0.0f, 0.0f));
		
		if(normals.size() == 0)
			calcNormals();
		
		if(tangents.size() == 0)
			calcTangents();
		
		return this;
	}
	
	public void addVertex(Vector3f vertex) {
		positions.add(vertex);
	}
	
	public void addTexCoord(Vector2f texCoord) {
		texCoords.add(texCoord);
	}
	
	public void addNormal(Vector3f normal) {
		normals.add(normal);
	}
	
	public void addTangent(Vector3f tangent) {
		tangents.add(tangent);
	}
	
	public void addFace(int vertIndex0, int vertIndex1, int vertIndex2) {
		indices.add(vertIndex0);
		indices.add(vertIndex1);
		indices.add(vertIndex2);
	}
	
	public List<Integer> getIndices() {
		return indices;
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
	
	public List<Vector3f> getTangents() {
		return tangents;
	}
}
