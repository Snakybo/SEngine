package com.snakybo.sengine.components;

import com.snakybo.sengine.core.CoreEngine;
import com.snakybo.sengine.core.utils.Matrix4f;
import com.snakybo.sengine.core.utils.Vector3f;

/** Camera component extends {@link Component}
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class Camera extends Component {
	private Matrix4f projection;
	
	/** Constructor for the camera
	 * @param fov The field of view
	 * @param aspect The aspect ratio
	 * @param zNear The near clipping plane
	 * @param zFar The far clipping plane */
	public Camera(float fov, float aspect, float zNear, float zFar) {
		this.projection = new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
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
	public void addToEngine(CoreEngine engine) {
		engine.getRenderingEngine().addCamera(this);
	}
}
