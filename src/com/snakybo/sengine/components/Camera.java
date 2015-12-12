package com.snakybo.sengine.components;

import java.util.ArrayList;
import java.util.List;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.core.object.Transform;
import com.snakybo.sengine.utils.Color;
import com.snakybo.sengine.utils.math.Matrix4f;

/** A basic camera class that you could use for perspective and orthographic
 * projections
 * <p>
 * The camera uses a {@link Matrix4f} as a projection matrix
 * </p>
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see Component
 * @see Matrix4f */
public class Camera
{
	private static List<Camera> cameras = new ArrayList<Camera>();
	
	private Matrix4f projection;
	private Transform transform;
	private Color clearColor;

	/** Constructor for the component
	 * @param projection The projection matrix to use for this camera
	 * @see Matrix4f */
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
	
	public Camera setAsMainCamera()
	{
		setMainCamera(this);
		return this;
	}
	
	public void setProjection(Matrix4f projection)
	{
		this.projection = projection;
	}
	
	public void setTransform(Transform transform)
	{
		this.transform = transform;
	}
	
	/** @return A transformed version of the projection matrix, rotation and
	 *         translation is applied before returning the projection
	 * @see Matrix4f */
	public Matrix4f getViewProjection()
	{
		Matrix4f cameraRotation = transform.getRotation().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = new Matrix4f().initTranslation(transform.getPosition().mul(-1));

		return projection.mul(cameraRotation.mul(cameraTranslation));
	}
	
	public Transform getTransform()
	{
		return transform;
	}
	
	public Color getClearColor()
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
		
		public CameraComponent setAsMainCamera()
		{
			camera.setAsMainCamera();
			return this;
		}
		
		public void setProjection(Matrix4f projection)
		{
			camera.setProjection(projection);
		}
		
		public Matrix4f getViewProjection()
		{
			return camera.getViewProjection();
		}
		
		public Color getClearColor()
		{
			return camera.getClearColor();
		}
	}
}