package com.snakybo.sengine.components;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.core.object.Transform;
import com.snakybo.sengine.rendering.RenderingEngine;
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
	private Matrix4f projection;
	private Transform transform;

	/** Constructor for the component
	 * @param projection The projection matrix to use for this camera
	 * @see Matrix4f */
	public Camera(Matrix4f projection)
	{
		this(projection, new Transform());
	}

	/** Constructor for the component
	 * @param projection The projection matrix to use for this camera
	 * @param transform The transformation of the camera
	 * @see Matrix4f */
	public Camera(Matrix4f projection, Transform transform)
	{
		this.projection = projection;
		this.transform = transform;

		RenderingEngine.setMainCamera(this);
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
		Matrix4f cameraRotation = getTransform().getRotation().conjugate().toRotationMatrix();
		Matrix4f cameraTranslation = new Matrix4f().initTranslation(getTransform().getPosition().mul(-1));

		return projection.mul(cameraRotation.mul(cameraTranslation));
	}

	public Transform getTransform()
	{
		return transform;
	}

	public static class CameraComponent extends Component
	{
		private Camera camera;

		public CameraComponent(Matrix4f projection)
		{
			this.camera = new Camera(projection);
		}

		@Override
		protected void onAddedToScene(RenderingEngine renderingEngine)
		{
			RenderingEngine.setMainCamera(camera);
		}

		public Matrix4f getViewProjection()
		{
			return camera.getViewProjection();
		}

		public void setProjection(Matrix4f projection)
		{
			camera.setProjection(projection);
		}

		@Override
		public void setParent(GameObject parent)
		{
			super.setParent(parent);

			camera.setTransform(getTransform());
		}
	}
}