package com.snakybo.engine.renderer;

import com.snakybo.engine.core.Matrix4f;
import com.snakybo.engine.core.Transform;

/** @author Kevin Krol
 * @since Feb 10, 2014 */
public class ForwardAmbient extends Shader {
	private static final ForwardAmbient instance = new ForwardAmbient();
	
	private ForwardAmbient() {
		addVertexShaderFromFile("forward/ambient.vertex.glsl");
		addFragmentShaderFromFile("forward/ambient.fragment.glsl");
		
		setAttribLocation("position", 0);
		setAttribLocation("texCoord", 1);
		
		compileShader();
		
		addUniform("MVP");
		addUniform("ambientIntensity");
	}
	
	/** @see #updateUniforms(Transform, Material, Renderer) */
	public void updateUniforms(Transform transform, Material material, Renderer renderer) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = renderer.getCamera().getProjection().scale(worldMatrix);
		
		material.getTexture("diffuse").bind();
		
		setUniform("MVP", projectedMatrix);
		setUniform("ambientIntensity", renderer.getAmbientLight());
	}
	
	/** @return The instance of this shader */
	public static ForwardAmbient getInstance() {
		return instance;
	}
}
