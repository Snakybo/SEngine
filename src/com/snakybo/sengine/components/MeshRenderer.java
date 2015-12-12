package com.snakybo.sengine.components;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.resource.material.Material;
import com.snakybo.sengine.resource.mesh.Mesh;
import com.snakybo.sengine.shader.Shader;

/** This class extends the {@link Component} class
 * <p>
 * This class has the ability to render meshes, it requires a {@link Mesh} and a
 * {@link Material}
 * </p>
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see Component
 * @see Mesh
 * @see Material */
public class MeshRenderer extends Component
{
	private Mesh mesh;
	private Material material;

	/** Constructor for the component
	 * @param mesh The mesh to render
	 * @param material The material to render the mesh with
	 * @see Mesh
	 * @see Material */
	public MeshRenderer(Mesh mesh, Material material)
	{
		this.mesh = mesh;
		this.material = material;
	}

	@Override
	protected void render(RenderingEngine renderingEngine, Shader shader)
	{
		shader.bind();
		shader.updateUniforms(getTransform(), material, renderingEngine);
		mesh.draw();
	}
}
