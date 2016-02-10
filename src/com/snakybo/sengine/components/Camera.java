package com.snakybo.sengine.components;

import java.util.HashSet;
import java.util.Set;

import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.object.Component;
import com.snakybo.sengine.object.Transform;
import com.snakybo.sengine.utils.Color;

/**
 * @author Kevin
 * @since Jan 3, 2016
 */
public class Camera extends Component
{
	private static Set<Camera> cameras = new HashSet<Camera>();
	
	private static Camera mainCamera;
	private static Camera currentCamera;
	
	private Matrix4f projection;
	private Color clearColor;
		
	public Camera(Matrix4f projection)
	{
		this(projection, new Color());
	}
	
	public Camera(Matrix4f projection, Color clearColor)
	{
		this.projection = projection;
		this.clearColor = clearColor;
	}
	
	@Override
	protected void onEnable()
	{	
		cameras.add(this);
	}
	
	@Override
	protected void onDisable()
	{
		mainCamera = mainCamera == this ? null : mainCamera;
		cameras.remove(this);
	}
	
	public void render()
	{
		currentCamera = this;
	}
	
	public final void setProjection(Matrix4f projection)
	{
		this.projection = projection;
	}
	
	public final void setClearColor(Color clearColor)
	{
		this.clearColor = clearColor;
	}
	
	public final Matrix4f getViewProjection()
	{
		Transform t = getTransform();
		
		Matrix4f cameraRotation = Matrix4f.createRotationMatrix(t.getRotation().conjugate());
		Matrix4f cameraTranslation = Matrix4f.createTranslationMatrix(t.getPosition().mul(-1));

		return projection.mul(cameraRotation.mul(cameraTranslation));
	}
	
	public final Color getClearColor()
	{
		return clearColor;
	}
	
	public static void setMainCamera(Camera camera)
	{
		mainCamera = camera;
	}
	
	public static Iterable<Camera> getCameras()
	{
		return cameras;
	}
	
	public static Camera getMainCamera()
	{
		return mainCamera;
	}
	
	public static Camera getCurrentCamera()
	{
		return currentCamera;
	}
}