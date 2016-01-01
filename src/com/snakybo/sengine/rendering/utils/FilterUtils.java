package com.snakybo.sengine.rendering.utils;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;

import com.snakybo.sengine.object.Transform;
import com.snakybo.sengine.rendering.Window;
import com.snakybo.sengine.resource.material.Material;
import com.snakybo.sengine.resource.mesh.Mesh;
import com.snakybo.sengine.resource.texture.Texture;
import com.snakybo.sengine.shader.Shader;

/**
 * @author Kevin
 * @since Dec 13, 2015
 */
public abstract class FilterUtils
{
	private static Transform transform;
	private static Material material;
	private static Texture texture;
	private static Shader shader;
	private static Mesh mesh;
	
	static
	{
		transform = new Transform();
		texture = new Texture(Window.getWidth(), Window.getHeight(), null, GL_TEXTURE_2D, GL_NEAREST, GL_RGBA, GL_RGBA, false, GL_COLOR_ATTACHMENT0);
		material = Material.createDefault(texture, 1, 8);
		shader = new Shader("internal/filter-gausBlur7x1");
		mesh = new Mesh("default/plane.obj");
	}
	
	public static Transform getTransform()
	{
		return transform;
	}
	
	public static Material getMaterial()
	{
		return material;
	}
	
	public static Texture getTexture()
	{
		return texture;
	}
	
	public static Shader getShader()
	{
		return shader;
	}
	
	public static Mesh getMesh()
	{
		return mesh;
	}
}
