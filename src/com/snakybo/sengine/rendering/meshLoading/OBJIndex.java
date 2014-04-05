package com.snakybo.sengine.rendering.meshLoading;

/** OBJ Index 
 * 
 * Used for .obj model loading
 * 
 * @author Kevin Krol
 * @since Apr 5, 2014 */
public class OBJIndex {
	public int vertexIndex;
	public int texCoordIndex;
	public int normalIndex;
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(obj == null)
			return false;
		
		if(!(obj instanceof OBJIndex))
			return false;
		
		OBJIndex other = (OBJIndex)obj;
		
		if(normalIndex != other.normalIndex) 
			return false;
		
		if(texCoordIndex != other.texCoordIndex) 
			return false;
		
		if(vertexIndex != other.vertexIndex) 
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		
		int result = 1;
		
		result = prime * result + normalIndex;
		result = prime * result + texCoordIndex;
		result = prime * result + vertexIndex;
		
		return result;
	}
}
