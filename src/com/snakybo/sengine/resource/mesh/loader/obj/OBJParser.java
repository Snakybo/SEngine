package com.snakybo.sengine.resource.mesh.loader.obj;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.utils.StringUtils;

/**
 * @author Kevin
 * @since Dec 19, 2015
 */
public final class OBJParser
{
	private static final boolean INVERSE_NORMALS = true;
	
	private List<Vector3f> vertices;
	private List<Vector2f> texCoords;
	private List<Vector3f> normals;	
	private List<OBJIndex> indices;

	private boolean hasTexCoords;
	private boolean hasNormals;
	
	public OBJParser()
	{
		indices = new ArrayList<OBJIndex>();
		vertices = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
	}
	
	public final void parse(BufferedReader reader) throws IOException 
	{
		String line;
			
		while((line = reader.readLine()) != null)
		{
			String[] tokens = StringUtils.removeEmptyStrings(line.split(" "));
			
			if(tokens.length == 0 || tokens[0].equals("#"))
			{
				continue;
			}
			
			if(parseVertex(tokens))
			{
				continue;
			}
			
			if(parseTexCoord(tokens))
			{
				continue;
			}
			
			if(parseNormal(tokens))
			{
				continue;
			}
			
			if(parseFace(tokens))
			{
				continue;
			}
		}
		
		reader.close();
	}
	
	private final boolean parseVertex(String[] tokens)
	{
		if(!tokens[0].equals("v"))
		{
			return false;
		}
		
		float x = Float.valueOf(tokens[1]);
		float y = Float.valueOf(tokens[2]);
		float z = Float.valueOf(tokens[3]);
		
		vertices.add(new Vector3f(x, y, z));
		
		return true;
	}
	
	private final boolean parseTexCoord(String[] tokens)
	{
		if(!tokens[0].equals("vt"))
		{
			return false;
		}
		
		float x = Float.valueOf(tokens[1]);
		float y = Float.valueOf(tokens[2]);
		
		if(INVERSE_NORMALS)
		{
			y = 1 - y;
		}
		
		texCoords.add(new Vector2f(x, y));		
		return true;
	}
	
	private final boolean parseNormal(String[] tokens)
	{
		if(!tokens[0].equals("vn"))
		{
			return false;
		}
		
		float x = Float.valueOf(tokens[1]);
		float y = Float.valueOf(tokens[2]);
		float z = Float.valueOf(tokens[3]);
		
		normals.add(new Vector3f(x, y, z));
		
		return true;
	}
	
	private final boolean parseFace(String[] tokens)
	{
		if(!tokens[0].equals("f"))
		{
			return false;
		}
		
		for(int i = 0; i < tokens.length - 3; i++)
		{
			OBJIndex i1 = parseOBJIndex(tokens[1]);
			OBJIndex i2 = parseOBJIndex(tokens[2 + i]);
			OBJIndex i3 = parseOBJIndex(tokens[3 + i]);
			
			indices.add(i1);
			indices.add(i2);
			indices.add(i3);
		}
		
		return true;
	}
	
	private final OBJIndex parseOBJIndex(String token)
	{
		String[] tokens = token.split("/");		

		OBJIndex result = new OBJIndex();
		result.setVertex(Integer.parseInt(tokens[0]) - 1);

		if(tokens.length > 1)
		{
			if(!tokens[1].isEmpty())
			{
				hasTexCoords = true;
				result.setTexCoord(Integer.parseInt(tokens[1]) - 1);
			}

			if(tokens.length > 2)
			{
				hasNormals = true;
				result.setNormal(Integer.parseInt(tokens[2]) - 1);
			}
		}

		return result;
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
	
	public final Iterable<OBJIndex> getIndices()
	{
		return indices;
	}
	
	public final int getNumVertices()
	{
		return vertices.size();
	}
	
	public final int getNumTexCoords()
	{
		return texCoords.size();
	}
	
	public final int getNumNormals()
	{
		return normals.size();
	}
	
	public final int getNumIndices()
	{
		return indices.size();
	}
	
	public final boolean hasTexCoords()
	{
		return hasTexCoords;
	}
	
	public final boolean hasNormals()
	{
		return hasNormals;
	}
}
