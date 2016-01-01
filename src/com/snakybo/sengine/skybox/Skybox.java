package com.snakybo.sengine.skybox;

import static org.lwjgl.opengl.GL11.GL_CULL_FACE_MODE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_FUNC;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.object.Transform;
import com.snakybo.sengine.resource.mesh.Mesh;
import com.snakybo.sengine.resource.texture.CubeMap;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.window.Window;

/**
 * @author Kevin
 * @since Dec 10, 2015
 */
public final class Skybox
{
	private static final String SKYBOX_SHADER_NAME = "internal/skybox";
	
	private Transform transform;
	private CubeMap cubeMap;
	private Shader shader;
	private Mesh mesh;
	
	private boolean destroyed;
	
	public Skybox(CubeMap cubeMap)
	{
		this.transform = new Transform();
		this.cubeMap = cubeMap;
		this.shader = new Shader(SKYBOX_SHADER_NAME);
		this.mesh = new Mesh("default/cube.obj");
	}
	
	public Skybox(String front, String back, String left, String right, String top, String bottom)
	{
		this(new CubeMap(front, back, left, right, top, bottom));
	}
	
	@Override
	protected void finalize() throws Throwable
	{
		try
		{
			destroy();
		}
		finally
		{
			super.finalize();	
		}
	}
	
	public final void destroy()
	{
		if(!destroyed)
		{
			destroyed = true;			
			mesh.destroy();
		}
	}
	
	public final void render()
	{
		Window.bindAsRenderTarget();
		
		int cullFaceMode = glGetInteger(GL_CULL_FACE_MODE);
		int depthFuncMode = glGetInteger(GL_DEPTH_FUNC);
		
		glCullFace(GL_FRONT);
		glDepthFunc(GL_LEQUAL);
		
		glEnable(GL_DEPTH_CLAMP);
		
		Vector3f cameraPosition = Camera.getMainCamera().getTransform().getPosition();
		transform.setPosition(new Vector3f(cameraPosition.x, cameraPosition.y, cameraPosition.z));
		transform.setScale(5000);
		
		shader.bind();
		shader.updateUniforms(transform, null, Camera.getMainCamera());
		
		cubeMap.bind();
		mesh.draw(GL_TRIANGLES);
		
		glDisable(GL_DEPTH_CLAMP);
		
		glCullFace(cullFaceMode);
		glDepthFunc(depthFuncMode);
	}
}
