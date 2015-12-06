/**
 * 
 */
package com.snakybo.sengine.rendering;

import com.snakybo.sengine.utils.math.Matrix4f;

/**
 * @author Kevin
 *
 */
public class ShadowMap
{
	
	
	public static class ShadowInfo
	{
		private Matrix4f projection;

		public ShadowInfo(Matrix4f projection)
		{
			this.projection = projection;
		}

		public Matrix4f getProjection()
		{
			return projection;
		}
	}
}
