package com.snakybo.sengine.components;

import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.object.Component;
import com.snakybo.sengine.rendering.Camera;
import com.snakybo.sengine.utils.Color;

/**
 * @author Kevin
 * @since Jan 3, 2016
 */
public class CameraComponent extends Component
{
	private Camera camera;
		
	public CameraComponent(Matrix4f projection)
	{
		this(projection, new Color());
	}
	
	public CameraComponent(Matrix4f projection, Color clearColor)
	{
		camera = new Camera(projection, clearColor);
	}
	
	public CameraComponent(Matrix4f projection, Color clearColor, boolean mainCamera)
	{
		camera = new Camera(projection, clearColor);
		
		if(mainCamera)
		{
			Camera.setMainCamera(camera);
		}
	}
	
	@Override
	protected void onAddedToScene()
	{		
		camera.setTransform(getTransform());
	}
	
	public final void setProjection(Matrix4f projection)
	{
		camera.setProjection(projection);
	}
	
	public final Matrix4f getViewProjection()
	{
		return camera.getViewProjection();
	}
	
	public final Color getClearColor()
	{
		return camera.getClearColor();
	}
}