package com.snakybo.engine.components;

import com.snakybo.engine.renderer.Material;
import com.snakybo.engine.renderer.Mesh;
import com.snakybo.engine.renderer.Shader;

/** @author Kevin Krol
 *  @since Feb 1, 2014 */
public class MeshRenderer extends Component {
	private Mesh mesh;
	private Material material;
	
	public MeshRenderer(Mesh mesh, Material material) {
		this.mesh = mesh;
		this.material = material;
	}
	
	@Override
	public void render(Shader shader) {
		shader.bind();
		shader.updateUniforms(getTransform(), material);
		mesh.draw();
	}
}
