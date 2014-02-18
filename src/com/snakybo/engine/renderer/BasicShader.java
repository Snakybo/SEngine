package com.snakybo.engine.renderer;

import com.snakybo.engine.core.Matrix4f;
import com.snakybo.engine.core.Transform;

public class BasicShader extends Shader {
	private static final BasicShader instance = new BasicShader();
	
	private BasicShader() {
		super();
		
		addVertexShaderFromFile("basicVertex.vs");
		addFragmentShaderFromFile("basicFragment.fs");
		compileShader();
		
		addUniform("transform");
		addUniform("color");
	}
	
	/** Update uniforms */
	public void updateUniforms(Transform transform, Material material) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = getRenderer().getCamera().getProjection().mul(worldMatrix);
		
		material.getTexture().bind();
		
		setUniform("transform", projectedMatrix);
		setUniform("color", material.getColor());
	}
	
	public static BasicShader getInstance() { return instance; }
}