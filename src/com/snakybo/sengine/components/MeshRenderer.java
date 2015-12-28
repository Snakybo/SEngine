package com.snakybo.sengine.components;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.resource.material.Material;
import com.snakybo.sengine.resource.mesh.Mesh;
import com.snakybo.sengine.shader.Shader;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 */
public class MeshRenderer extends Component
{
	private Mesh mesh;
	private Material material;
	
	public MeshRenderer(Mesh mesh)
	{
		this(mesh, Material.createDefault());
	}
	
	public MeshRenderer(Mesh mesh, Material material)
	{
		this.mesh = mesh;
		this.material = material;
	}
	
	@Override
	protected void onDestroy()
	{
		mesh.destroy();
	}
	
	@Override
	protected void render(Shader shader, Camera camera)
	{
		shader.bind();
		shader.updateUniforms(getTransform(), material, camera);
		mesh.draw();
	}
}
