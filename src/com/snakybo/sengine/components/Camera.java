package com.snakybo.sengine.components;

import java.util.ArrayList;
import java.util.List;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.core.object.Transform;
import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.utils.Color;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 */
public class Camera
{
	private static List<Camera> cameras = new ArrayList<Camera>();
	
	private Matrix4f projection;
	private Transform transform;
	private Color clearColor;
	
	public Camera(Matrix4f projection)
	{
		this(projection, new Transform());
	}
	
	public Camera(Matrix4f projection, Transform transform)
	{
		this(projection, transform, new Color(0, 0, 0));
	}
	
	public Camera(Matrix4f projection, Color clearColor)
	{
		this(projection, new Transform(), clearColor);
	}
	
	public Camera(Matrix4f projection, Transform transform, Color clearColor)
	{
		this.projection = projection;
		this.transform = transform;
		this.clearColor = clearColor;
		
		cameras.add(this);
	}
	
	public final Camera setAsMainCamera()
	{
		setMainCamera(this);
		return this;
	}
	
	public final void setProjection(Matrix4f projection)
	{
		this.projection = projection;
	}
	
	public final void setTransform(Transform transform)
	{
		this.transform = transform;
	}
	
	/** @return A transformed version of the projection matrix, rotation and
	 *         translation is applied before returning the projection
	 * @see Matrix4f */
	public final Matrix4f getViewProjection()
	{
		Matrix4f cameraRotation = Matrix4f.createRotationMatrix(transform.getRotation().conjugate());
		Matrix4f cameraTranslation = Matrix4f.createTranslationMatrix(transform.getPosition().mul(-1));

		return projection.mul(cameraRotation.mul(cameraTranslation));
	}
	
	public final Transform getTransform()
	{
		return transform;
	}
	
	public final Color getClearColor()
	{
		return clearColor;
	}
	
	public static void setMainCamera(Camera camera)
	{
		Camera old = cameras.set(0, camera);
		
		if(old != null)
		{
			cameras.add(old);
		}
	}
	
	public static Camera getMainCamera()
	{
		if(cameras.size() > 0)
		{
			return cameras.get(0);
		}
		
		return null;
	}
	
	public static Iterable<Camera> getCameras()
	{
		return cameras;
	}

	public static class CameraComponent extends Component
	{
		private Camera camera;
		
		public CameraComponent(Matrix4f projection)
		{
			this(projection, new Color(0, 0, 0));
		}
		
		public CameraComponent(Matrix4f projection, Color clearColor)
		{
			camera = new Camera(projection, clearColor);
		}
		
		@Override
		protected void onAddedToScene()
		{		
			camera.setTransform(getTransform());
		}
		
		public final CameraComponent setAsMainCamera()
		{
			camera.setAsMainCamera();
			return this;
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
		
		public final Camera getCamera()
		{
			return camera;
		}
	}
}