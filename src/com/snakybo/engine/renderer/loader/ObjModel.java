package com.snakybo.engine.renderer.loader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.snakybo.engine.core.Util;
import com.snakybo.engine.core.Vector2f;
import com.snakybo.engine.core.Vector3f;

/** @author Kevin Krol
 * @since Mar 12, 2014 */
public class ObjModel {
	private List<Vector3f> positions;
	private List<Vector2f> texCoords;
	private List<Vector3f> normals;
	private List<ObjIndex> indices;
	
	private boolean hasTexCoords;
	private boolean hasNormals;
	
	public ObjModel(String fileName) {
		positions = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		indices = new ArrayList<ObjIndex>();
		
		hasTexCoords = false;
		hasNormals = false;
		
		BufferedReader meshReader = null;
		
		try {
			meshReader = new BufferedReader(new FileReader(fileName));
			String line;
			
			while((line = meshReader.readLine()) != null) {
				String[] tokens = line.split(" ");
				tokens = Util.removeEmptyStrings(tokens);
				
				if(tokens.length == 0 || tokens[0].equals("#")) {
					continue;
				} else if(tokens[0].equals("v")) {
					positions.add(new Vector3f(Float.valueOf(tokens[1]), Float
							.valueOf(tokens[2]), Float.valueOf(tokens[3])));
				} else if(tokens[0].equals("vt")) {
					texCoords.add(new Vector2f(Float.valueOf(tokens[1]), Float
							.valueOf(tokens[2])));
				} else if(tokens[0].equals("vn")) {
					normals.add(new Vector3f(Float.valueOf(tokens[1]), Float
							.valueOf(tokens[2]), Float.valueOf(tokens[3])));
				} else if(tokens[0].equals("f")) {
					for(int i = 0; i < tokens.length - 3; i++) {
						indices.add(parseObjIndex(tokens[1]));
						indices.add(parseObjIndex(tokens[2 + i]));
						indices.add(parseObjIndex(tokens[3 + i]));
					}
				}
			}
			
			meshReader.close();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public IndexModel toIndexModel() {
		IndexModel resultModel = new IndexModel();
		IndexModel normalModel = new IndexModel();
		
		HashMap<ObjIndex, Integer> resultIndexMap = new HashMap<ObjIndex, Integer>();
		HashMap<Integer, Integer> normalIndexMap = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> indexMap = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < indices.size(); i++) {
			ObjIndex currentIndex = indices.get(i);
			
			Vector3f currentPosition = positions.get(currentIndex.vertexIndex);
			Vector2f currentTexCoord = new Vector2f();
			Vector3f currentNormal = new Vector3f();
			
			if(hasTexCoords)
				currentTexCoord = texCoords.get(currentIndex.texCoordIndex);
			
			if(hasNormals)
				currentNormal = normals.get(currentIndex.normalIndex);
			
			Integer modelVertexIndex = resultIndexMap.get(currentIndex);
			Integer normalModelIndex = normalIndexMap.get(currentIndex.vertexIndex);
			
			if(modelVertexIndex == null) {
				modelVertexIndex = resultModel.getPositions().size();
				resultIndexMap.put(currentIndex, modelVertexIndex);
				
				resultModel.getPositions().add(currentPosition);
				resultModel.getTexCoords().add(currentTexCoord);
				
				if(hasNormals)
					resultModel.getNormals().add(currentNormal);
			}
			
			if(normalModelIndex == null) {
				normalModelIndex = normalModel.getPositions().size();
				normalIndexMap.put(currentIndex.vertexIndex, normalModelIndex);
				
				normalModel.getPositions().add(currentPosition);
				normalModel.getTexCoords().add(currentTexCoord);
				normalModel.getNormals().add(currentNormal);
			}
			
			resultModel.getIndices().add(modelVertexIndex);
			normalModel.getIndices().add(normalModelIndex);
			
			indexMap.put(modelVertexIndex, normalModelIndex);
		}
		
		if(!hasNormals) {
			normalModel.calculateNormals();
			
			for(int i = 0; i < resultModel.getPositions().size(); i++)
				resultModel.getNormals().add(normalModel.getNormals().get(indexMap.get(i)));
		}
		
		return resultModel;
	}
	
	private ObjIndex parseObjIndex(String token) {
		String[] values = token.split("/");
		ObjIndex result = new ObjIndex();
		
		result.vertexIndex = Integer.parseInt(values[0]) - 1;
		
		if(values.length > 1) {
			hasTexCoords = true;
			
			result.texCoordIndex = Integer.parseInt(values[1]) - 1;
			
			if(values.length > 2) {
				hasNormals = true;
				
				result.normalIndex = Integer.parseInt(values[2]) - 1;
			}
		}
		
		return result;
	}
}
