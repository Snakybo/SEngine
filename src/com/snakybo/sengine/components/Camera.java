package com.snakybo.sengine.components;

import com.snakybo.sengine.core.Component;
import com.snakybo.sengine.core.CoreEngine;
import com.snakybo.sengine.core.utils.Matrix4f;
import com.snakybo.sengine.core.utils.Vector3f;

/** Camera component extends {@link Component}
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class Camera extends Component {
	private Matrix4f projection;
	
	/** Initialize a perspective camera
	 * @param fov The field of view
	 * @param aspect The aspect ratio
	 * @param zNear The near clipping plane
	 * @param zFar The far clipping plane
	 * @return A new perspective camera */
	public static Camera initPerspectiveCamera(float fov, float aspectRatio, float zNear, float zFar) {
		return new Camera(new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar));
	}
	
	/** Constructor for the camera
	 * @param projection The projection of the camera */
	public Camera(Matrix4f projection) {
		this.projection = projection;
	}
	
	/** Get the view projcetion of the camera represented as a matrix 4 */
	public Matrix4f getViewProjection() {
		Matrix4f cameraRotation = getTransform().getRotation().conjugate().toRotationMatrix();
		Vector3f cameraPos = getTransform().getPosition().mul(-1);
		
		Matrix4f cameraTranslation =
				new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
		
		return projection.mul(cameraRotation.mul(cameraTranslation));
	}
	
	@Override
	protected void addToEngine(CoreEngine engine) {
		engine.getRenderingEngine().addCamera(this);
	}
}
