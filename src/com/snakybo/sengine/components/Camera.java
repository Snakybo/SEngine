package com.snakybo.sengine.components;

import com.snakybo.sengine.core.Component;
import com.snakybo.sengine.core.SEngine;
import com.snakybo.sengine.core.utils.Matrix4f;
import com.snakybo.sengine.core.utils.Vector3f;

public class Camera extends Component {
	private Matrix4f projection;
	
	public static Camera initPerspectiveCamera(float fov, float aspect, float zNear, float zFar) {
		return new Camera(new Matrix4f().initPerspective(fov, aspect, zNear, zFar));
	}
	
	public Camera(Matrix4f projection) {
		this.projection = projection;
	}
	
	public Matrix4f getViewProjection() {
		Matrix4f cameraRotation = getTransform().getTransformedRotation().conjugate().toRotationMatrix();
		Vector3f cameraPos = getTransform().getTransformedPosition().mul(-1);
		
		Matrix4f cameraTranslation =
				new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
		
		return projection.mul(cameraRotation.mul(cameraTranslation));
	}
	
	@Override
	public void addToEngine(SEngine engine) {
		engine.getRenderingEngine().addCamera(this);
	}
}
