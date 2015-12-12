package com.snakybo.sengine.resource.loading;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.snakybo.sengine.utils.Utils;
import com.snakybo.sengine.utils.math.Vector2f;
import com.snakybo.sengine.utils.math.Vector3f;

public class OBJModel implements IModel
{
	private class OBJIndex
	{
		public int vertex;
		public int texCoord;
		public int normal;

		@Override
		public boolean equals(Object obj)
		{
			OBJIndex index = (OBJIndex)obj;

			return vertex == index.vertex && texCoord == index.texCoord && normal == index.normal;
		}

		@Override
		public int hashCode()
		{
			final int BASE = 17;
			final int MULTIPLIER = 31;

			int result = BASE;

			result = MULTIPLIER * result + vertex;
			result = MULTIPLIER * result + texCoord;
			result = MULTIPLIER * result + normal;

			return result;
		}
	}

	private List<OBJIndex> indices;
	private List<Vector3f> positions;
	private List<Vector2f> texCoords;
	private List<Vector3f> normals;

	private boolean hasTexCoords;
	private boolean hasNormals;

	public OBJModel(FileReader file)
	{
		indices = new ArrayList<OBJIndex>();
		positions = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();

		hasTexCoords = false;
		hasNormals = false;

		BufferedReader bufferedReader = null;

		try
		{
			bufferedReader = new BufferedReader(file);
			String line;

			while((line = bufferedReader.readLine()) != null)
			{
				String[] tokens = Utils.removeEmptyStrings(line.split(" "));

				switch(tokens[0])
				{
				case "v":
					positions.add(new Vector3f(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3])));
					break;
				case "vt":
					texCoords.add(new Vector2f(Float.valueOf(tokens[1]), Float.valueOf(tokens[2])));
					break;
				case "vn":
					normals.add(new Vector3f(Float.valueOf(tokens[1]), Float.valueOf(tokens[2]), Float.valueOf(tokens[3])));
					break;
				case "f":
					for(int i = 0; i < tokens.length - 3; i++)
					{
						indices.add(parseObjIndex(tokens[1]));
						indices.add(parseObjIndex(tokens[2 + i]));
						indices.add(parseObjIndex(tokens[3 + i]));
					}
					break;
				default:
					break;
				}
			}

			bufferedReader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public IndexedModel toIndexedModel()
	{
		IndexedModel model = new IndexedModel();
		IndexedModel normalModel = new IndexedModel();

		Map<OBJIndex, Integer> resultIndexMap = new HashMap<OBJIndex, Integer>();
		Map<Integer, Integer> normalIndexMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();

		for(int i = 0; i < indices.size(); i++)
		{
			OBJIndex currentIndex = indices.get(i);

			Vector3f currentPosition = positions.get(currentIndex.vertex);
			Vector2f currentTexCoord = hasTexCoords ? texCoords.get(currentIndex.texCoord) : new Vector2f(0, 0);
			Vector3f currentNormal = hasNormals ? normals.get(currentIndex.normal) : new Vector3f(0, 0, 0);

			Integer modelVertexIndex = resultIndexMap.get(currentIndex);

			if(modelVertexIndex == null)
			{
				modelVertexIndex = model.getPositions().length;
				resultIndexMap.put(currentIndex, modelVertexIndex);

				model.addVertex(currentPosition);
				model.addTexCoord(currentTexCoord);

				if(hasNormals)
				{
					model.addNormal(currentNormal);
				}

				model.addTangent(new Vector3f(0, 0, 0));
			}

			Integer normalModelIndex = normalIndexMap.get(currentIndex.vertex);

			if(normalModelIndex == null)
			{
				normalModelIndex = normalModel.getPositions().length;
				normalIndexMap.put(currentIndex.vertex, normalModelIndex);

				normalModel.addVertex(currentPosition);
				normalModel.addTexCoord(currentTexCoord);
				normalModel.addNormal(currentNormal);
				normalModel.addTangent(new Vector3f(0, 0, 0));
			}

			model.addIndex(modelVertexIndex);
			normalModel.addIndex(normalModelIndex);

			indexMap.put(modelVertexIndex, normalModelIndex);
		}

		if(!hasNormals)
		{
			normalModel.calcNormals();

			for(int i = 0; i < model.getPositions().length; i++)
			{
				model.addNormal(normalModel.getNormals()[indexMap.get(i)]);
			}
		}

		normalModel.calcTangents();

		for(int i = 0; i < model.getPositions().length; i++)
		{
			model.setTangent(i, normalModel.getTangents()[indexMap.get(i)]);
		}

		return model;
	}

	private OBJIndex parseObjIndex(String token)
	{
		String[] values = token.split("/");

		OBJIndex result = new OBJIndex();
		result.vertex = Integer.parseInt(values[0]) - 1;

		if(values.length > 1)
		{
			hasTexCoords = true;
			result.texCoord = Integer.parseInt(values[1]) - 1;

			if(values.length > 2)
			{
				hasNormals = true;
				result.normal = Integer.parseInt(values[2]) - 1;
			}
		}

		return result;
	}
}
