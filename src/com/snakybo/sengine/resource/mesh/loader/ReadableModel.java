package com.snakybo.sengine.resource.mesh.loader;

import java.util.ArrayList;
import java.util.List;

import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.math.Vector3f;

/**
 * @author Kevin Krol
 * @since Jul 8, 2014
 */
public final class ReadableModel
{	
	private List<Vector3f> vertices;
	private List<Vector2f> texCoords;
	private List<Vector3f> normals;
	private List<Vector3f> tangents;
	private List<Integer> indices;
	
	public ReadableModel()
	{
		vertices = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		tangents = new ArrayList<Vector3f>();
		indices = new ArrayList<Integer>();
	}
	
	public final void calcNormals()
	{
		for(int i = 0; i < indices.size(); i += 3)
		{
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);
			
			Vector3f v1 = vertices.get(i1).sub(vertices.get(i0));
			Vector3f v2 = vertices.get(i2).sub(vertices.get(i0));
			
			Vector3f normal = v1.cross(v2).normalized();
			
			normals.get(i0).set(normals.get(i0).add(normal));
			normals.get(i1).set(normals.get(i1).add(normal));
			normals.get(i2).set(normals.get(i2).add(normal));
		}
		
		for(int i = 0; i < normals.size(); i++)
		{
			normals.get(i).set(normals.get(i).normalized());
		}
	}

	public final void calcTangents()
	{
		for(int i = 0; i < indices.size(); i += 3)
		{
			int i0 = indices.get(i);
			int i1 = indices.get(i + 1);
			int i2 = indices.get(i + 2);

			Vector3f edge1 = vertices.get(i1).sub(vertices.get(i0));
			Vector3f edge2 = vertices.get(i2).sub(vertices.get(i0));

			float deltaU1 = texCoords.get(i1).x - texCoords.get(i0).x;
			float deltaV1 = texCoords.get(i1).y - texCoords.get(i0).y;
			float deltaU2 = texCoords.get(i2).x - texCoords.get(i0).x;
			float deltaV2 = texCoords.get(i2).y - texCoords.get(i0).y;

			float dividend = deltaU1 * deltaV2 - deltaU2 * deltaV1;
			float f = dividend == 0 ? 0 : 1 / dividend;

			Vector3f tangent = new Vector3f();
			tangent.x = f * (deltaV2 * edge1.x - deltaV1 * edge2.x);
			tangent.y = f * (deltaV2 * edge1.y - deltaV1 * edge2.y);
			tangent.z = f * (deltaV2 * edge1.z - deltaV1 * edge2.z);

			tangents.get(i0).set(tangents.get(i0).add(tangent));
			tangents.get(i1).set(tangents.get(i1).add(tangent));
			tangents.get(i2).set(tangents.get(i2).add(tangent));
		}

		for(int i = 0; i < tangents.size(); i++)
		{
			tangents.get(i).set(tangents.get(i).normalized());
		}
	}
	
	public final void addVertex(Vector3f vertex)
	{
		vertices.add(vertex);
	}
	
	public final void addTexCoord(Vector2f texCoord)
	{
		texCoords.add(texCoord);
	}
	
	public final void addNormal(Vector3f normal)
	{
		normals.add(normal);
	}
	
	public final void addTangent(Vector3f tangent)
	{
		tangents.add(tangent);
	}
	
	public final void addIndex(int index)
	{
		indices.add(index);
	}
	
	public final Iterable<Vector3f> getVertices()
	{
		return vertices;
	}

	public final Iterable<Vector2f> getTexCoords()
	{
		return texCoords;
	}

	public final Iterable<Vector3f> getNormals()
	{
		return normals;
	}

	public final Iterable<Vector3f> getTangents()
	{
		return tangents;
	}
	
	public final Iterable<Integer> getIndices()
	{
		return indices;
	}
	
	public final Vector3f getVertex(int index)
	{
		return vertices.get(index);
	}
	
	public final Vector2f getTexCoord(int index)
	{
		return texCoords.get(index);
	}
	
	public final Vector3f getNormal(int index)
	{
		return normals.get(index);
	}
	
	public final Vector3f getTangent(int index)
	{
		return tangents.get(index);
	}
	
	public final int getIndex(int index)
	{
		return indices.get(index);
	}
	
	public final int getNumTriangles()
	{
		return getNumIndices() / 3;
	}
	
	public final int getNumVertices()
	{
		return vertices.size();
	}
	
	public final int getNumIndices()
	{
		return indices.size();
	}
}
