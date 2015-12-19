package com.snakybo.sengine.resource.mesh.loader.obj;

/**
 * @author Kevin
 * @since Dec 19, 2015
 */
public final class OBJIndex
{
	private int vertex;
	private int texCoord;
	private int normal;
	
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
	
	public final void setVertex(int vertex)
	{
		this.vertex = vertex;
	}
	
	public final void setTexCoord(int texCoord)
	{
		this.texCoord = texCoord;
	}
	
	public final void setNormal(int normal)
	{
		this.normal = normal;
	}
	
	public final int getVertex()
	{
		return vertex;
	}
	
	public final int getTexCoord()
	{
		return texCoord;
	}
	
	public final int getNormal()
	{
		return normal;
	}
}