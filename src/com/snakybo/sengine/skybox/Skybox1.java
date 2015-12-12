package com.snakybo.sengine.skybox;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE_MODE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_FUNC;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glGetInteger;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.core.object.Transform;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.resource.mesh.Mesh;
import com.snakybo.sengine.resource.mesh.Primitive;
import com.snakybo.sengine.resource.texture.CubeMap;
import com.snakybo.sengine.shader.Shader;

/**
 * @author Kevin
 * @since Dec 10, 2015
 */
public class Skybox1
{
	private static final String SKYBOX_SHADER_NAME = "internal/skybox";
	
	private Transform transform;
	private CubeMap cubeMap;
	private Shader shader;
	private Mesh mesh;
	
	public Skybox1(CubeMap cubeMap)
	{
		this.transform = new Transform();
		this.cubeMap = cubeMap;
		this.shader = new Shader(SKYBOX_SHADER_NAME);
		this.mesh = Primitive.CUBE;
	}
	
	public Skybox1(String front, String back, String left, String right, String top, String bottom)
	{
		this(new CubeMap(front, back, left, right, top, bottom));
	}
	
	public void render(RenderingEngine renderingEngine)
	{
		int cullFaceMode = glGetInteger(GL_CULL_FACE_MODE);
		int depthFuncMode = glGetInteger(GL_DEPTH_FUNC);
		
		glCullFace(GL_FRONT);
		glDepthFunc(GL_LEQUAL);
		
		Vector3f cameraPosition = Camera.getMainCamera().getTransform().getPosition();
		transform.setPosition(new Vector3f(cameraPosition.x, cameraPosition.y, cameraPosition.z));
		transform.setScale(5000);
		
		shader.bind();
		shader.updateUniforms(transform, null, renderingEngine);
		
		cubeMap.bind();
		mesh.draw();
		
		glCullFace(cullFaceMode);
		glDepthFunc(depthFuncMode);
	}
}
