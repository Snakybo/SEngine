package com.snakybo.engine.renderer;

import com.snakybo.engine.core.Matrix4f;
import com.snakybo.engine.core.Transform;

/** @author Kevin Krol
 *  @since Feb 10, 2014 */
public class ForwardAmbient extends Shader {
	private static final ForwardAmbient instance = new ForwardAmbient();
	
	private ForwardAmbient() {
		super();
		
		addVertexShaderFromFile("forward-ambient-vertex.glsl");
		addFragmentShaderFromFile("forward-ambient-fragment.glsl");
		
		setAttribLocation("position" , 0);
		setAttribLocation("texCoord", 1);
		
		compileShader();
		
		addUniform("MVP");
		addUniform("ambientIntensity");
	}
	
	/** Update uniforms */
	public void updateUniforms(Transform transform, Material material) {
		Matrix4f worldMatrix = transform.getTransformation();
		Matrix4f projectedMatrix = getRenderer().getCamera().getProjection().mul(worldMatrix);
		
		material.getTexture().bind();
		
		setUniform("MVP", projectedMatrix);
		setUniform("ambientIntensity", getRenderer().getAmbientLight());
	}
	
	public static ForwardAmbient getInstance() { return instance; }
}
