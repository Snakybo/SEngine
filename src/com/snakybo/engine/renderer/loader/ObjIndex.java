package com.snakybo.engine.renderer.loader;

/** @author Kevin Krol
 * @since Mar 12, 2014 */
public class ObjIndex {
	public int vertexIndex;
	public int texCoordIndex;
	public int normalIndex;
	
	@Override
	public boolean equals(Object obj) {
		ObjIndex index = (ObjIndex)obj;
		
		return vertexIndex == index.vertexIndex &&
				texCoordIndex == index.texCoordIndex &&
				normalIndex == index.texCoordIndex;
	}
	
	@Override
	public int hashCode() {
		final int prime = 17;
		final int multiplier = 31;
		
		int result = prime;
		
		result += multiplier * result + vertexIndex;
		result += multiplier * result + texCoordIndex;
		result += multiplier * result + normalIndex;
		
		return result;
	}
}
