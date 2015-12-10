/**
 * 
 */
package com.snakybo.sengine.skybox;

import com.snakybo.sengine.core.object.Transform;
import com.snakybo.sengine.resource.Material;
import com.snakybo.sengine.shader.Shader;

/**
 * @author Kevin
 *
 */
public class SkyBoxShader extends Shader
{
	@Override
	protected void updateUniforms(Transform transform, Material material, String name, String type)
	{
		System.out.println(name + " " + type);
	}
}