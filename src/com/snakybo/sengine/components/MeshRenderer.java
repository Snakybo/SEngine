package com.snakybo.sengine.components;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.rendering.Material;
import com.snakybo.sengine.resource.Mesh;
import com.snakybo.sengine.resource.Shader;

/** This class extends the {@link Component} class
 * 
 * <p>
 * This class has the ability to render meshes, it requires a {@link Mesh_Old} and a {@link Material}
 * </p>
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see Component
 * @see Mesh_Old
 * @see Material */
public class MeshRenderer extends Component {
	private Mesh mesh;
	private Material material;
	
	/** Constructor for the component
	 * @param mesh_Old The mesh to render
	 * @param material The material to render the mesh with
	 * @see Mesh_Old
	 * @see Material */
	public MeshRenderer(Mesh mesh_Old, Material material) {
		this.mesh = mesh_Old;
		this.material = material;
	}
	
	@Override
	protected void render(Shader shader) {
		shader.bind();
		shader.updateUniforms(getTransform(), material);
		mesh.draw();
	}
}
