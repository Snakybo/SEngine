package com.snakybo.sengine.components;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.utils.math.Matrix4f;
import com.snakybo.sengine.utils.math.Vector3f;

/** A basic camera class that you could use for perspective and orthographic projections
 * 
 * <p>
 * This class extends the {@link Component} class
 * </p>
 * <p>
 * The camera uses a {@link Matrix4f} as a projection matrix
 * </p>
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see Component
 * @see Matrix4f */
public class Camera extends Component {
	private Matrix4f projection;
	
	/** Initialize a camera for a perspective view
	 * @param fov The field of view of the camera
	 * @param aspect The aspect ratio of the camera (with / height)
	 * @param zNear The near clipping plane of the camera
	 * @param zFar The far clipping plane of the camera
	 * @return An initialized camera set to a perspective view */
	public static Camera initPerspectiveCamera(float fov, float aspect, float zNear, float zFar) {
		return new Camera(new Matrix4f().initPerspective(fov, aspect, zNear, zFar));
	}
	
	/** Constructor for the component
	 * @param projection The projection matrix to use for this camera
	 * @see Matrix4f */
	public Camera(Matrix4f projection) {
		this.projection = projection;
		
		RenderingEngine.setMainCamera(this);
	}
	
	/** @return A transformed version of the projection matrix, rotation and translation is applied
	 *         before returning the projection
	 * @see Matrix4f */
	public Matrix4f getViewProjection() {
		Matrix4f cameraRotation = getTransform().getWorldRotation().conjugate().toRotationMatrix();
		Vector3f cameraPos = getTransform().getWorldPosition().mul(-1);
		
		Matrix4f cameraTranslation = new Matrix4f().initTranslation(cameraPos.x, cameraPos.y, cameraPos.z);
		
		return projection.mul(cameraRotation.mul(cameraTranslation));
	}
}