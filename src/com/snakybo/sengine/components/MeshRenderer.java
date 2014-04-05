package com.snakybo.sengine.components;

import com.snakybo.sengine.rendering.Material;
import com.snakybo.sengine.rendering.Mesh;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Shader;

/** Mesh renderer component extends {@link Component}
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class MeshRenderer extends Component {
	private Mesh mesh;
	private Material material;
	
	/** Constructor for the mesh renderer
	 * @param mesh The mesh to render
	 * @param material The material to render the mesh with */
	public MeshRenderer(Mesh mesh, Material material) {
		this.mesh = mesh;
		this.material = material;
	}
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine) {
		shader.bind();
		shader.updateUniforms(getTransform(), material, renderingEngine);
		mesh.draw();
	}
}
