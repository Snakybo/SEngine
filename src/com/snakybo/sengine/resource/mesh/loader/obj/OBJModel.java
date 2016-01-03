package com.snakybo.sengine.resource.mesh.loader.obj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.resource.mesh.loader.IModel;
import com.snakybo.sengine.resource.mesh.loader.ReadableModel;

public final class OBJModel implements IModel
{	
	private final OBJParser parser;
	
	public OBJModel(FileReader file)
	{
		parser = new OBJParser();
		
		try
		{
			parser.parse(new BufferedReader(file));
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	public ReadableModel toReadableModel()
	{
		ReadableModel model = new ReadableModel();
		ReadableModel normalModel = new ReadableModel();

		Map<OBJIndex, Integer> resultIndexMap = new HashMap<OBJIndex, Integer>();
		Map<Integer, Integer> normalIndexMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> indexMap = new HashMap<Integer, Integer>();

		for(OBJIndex index : parser.getIndices())
		{
			Vector3f vertex = parser.getVertex(index.getVertex());
			Vector2f texCoord = new Vector2f();
			Vector3f normal = new Vector3f();
			
			if(parser.hasTexCoords())
			{
				texCoord = parser.getTexCoord(index.getTexCoord());
			}
			
			if(parser.hasNormals())
			{
				normal = parser.getNormal(index.getNormal());
			}
			
			Integer modelVertexIndex = resultIndexMap.get(index);
			if(modelVertexIndex == null)
			{
				modelVertexIndex = model.getNumVertices();
				resultIndexMap.put(index, modelVertexIndex);
				
				model.addVertex(vertex);
				model.addTexCoord(texCoord);
				
				if(parser.hasNormals())
				{
					model.addNormal(normal);
				}
			}
			
			Integer normalModelIndex = normalIndexMap.get(index.getVertex());
			if(normalModelIndex == null)
			{
				normalModelIndex = normalModel.getNumVertices();
				normalIndexMap.put(index.getVertex(), normalModelIndex);
				
				normalModel.addVertex(vertex);
				normalModel.addTexCoord(texCoord);
				normalModel.addNormal(normal);
				normalModel.addTangent(new Vector3f());
			}
			
			model.addIndex(modelVertexIndex);
			normalModel.addIndex(normalModelIndex);
			indexMap.put(modelVertexIndex, normalModelIndex);
		}
		
		if(!parser.hasNormals())
		{
			normalModel.calcNormals();			
			for(int i = 0; i < model.getNumVertices(); i++)
			{
				model.addNormal(normalModel.getNormal(indexMap.get(i)));
			}
		}
		
		normalModel.calcTangents();		
		for(int i = 0; i < model.getNumVertices(); i++)
		{
			model.addTangent(normalModel.getTangent(indexMap.get(i)));
		}
		
		return model;
	}
}
