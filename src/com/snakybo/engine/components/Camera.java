package com.snakybo.engine.components;

import com.snakybo.engine.core.Input;
import com.snakybo.engine.core.Input.KeyCode;
import com.snakybo.engine.core.Matrix4f;
import com.snakybo.engine.core.Quaternion;
import com.snakybo.engine.core.Vector2f;
import com.snakybo.engine.core.Vector3f;
import com.snakybo.engine.renderer.Renderer;
import com.snakybo.engine.renderer.Window;

public class Camera extends Component {
	private Matrix4f projection;
	
	private boolean mouseLocked = false;
	private Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);
	
	/** Create a new camera
	 * @param fov The field of view
	 * @param aspect The aspect ratio of the camera
	 * @param zNear The near clipping plane of the camera
	 * @param zFar The far clipping plane of the camera */
	public Camera(float fov, float aspect, float zNear, float zFar) {
		this.projection = new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
	}
	
	@Override
	public void addToRenderer(Renderer renderer) {
		renderer.addCamera(this);
	}
	
	/** Handle Input */
	@Override
	public void input(float delta) {
		float sensitivity = -0.5f;
		float moveAmount = (float)(10 * delta);
		
		if(Input.getMouseDown(0)) {
			if(!mouseLocked) {
				Input.setMousePosition(centerPosition);
				Input.setCursor(false);
				mouseLocked = true;
			} else {
				Input.setCursor(true);
				mouseLocked = false;
			}
		}
		
		if(Input.getKey(KeyCode.KEY_W)) {
			getTransform().setPosition(getTransform().getPosition().add(getTransform().getRotation().forward().mul(moveAmount)));
		} else if(Input.getKey(KeyCode.KEY_S)) {
			getTransform().setPosition(getTransform().getPosition().add(getTransform().getRotation().back().mul(moveAmount)));
		}
		
		if(Input.getKey(KeyCode.KEY_A)) {
			getTransform().setPosition(getTransform().getPosition().add(getTransform().getRotation().left().mul(moveAmount)));
		} else if(Input.getKey(KeyCode.KEY_D)) {
			getTransform().setPosition(getTransform().getPosition().add(getTransform().getRotation().right().mul(moveAmount)));
		}
		
		if(mouseLocked) {
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);
			
			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;
			
			if(rotY)
				getTransform().setRotation(getTransform().getRotation().mul(new Quaternion().initRotation(Vector3f.UP, (float)Math.toRadians(deltaPos.getX() * sensitivity))).normalize());
			
			if(rotX)
				getTransform().setRotation(getTransform().getRotation().mul(new Quaternion().initRotation(getTransform().getRotation().right(), (float)Math.toRadians(-deltaPos.getY() * sensitivity))).normalize());
				
			if(rotY || rotX)
				Input.setMousePosition(new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2));
		}
	}
	
	/** Move the camera in the specified direction
	 * @param direction The direction to move the camera in
	 * @param amount The amount of units to move */
	public void move(Vector3f direction, float amount) {
		getTransform().setPosition(getTransform().getPosition().add(direction.mul(amount)));
	}
	
	/** @return The projection matrix */
	public Matrix4f getProjection() {
		Matrix4f cameraRotation = getTransform().getRotation().toRotationMatrix();
		Matrix4f cameraTranslation = new Matrix4f().initPosition(-getTransform().getPosition().getX(), -getTransform().getPosition().getY(), -getTransform().getPosition().getZ());
		
		return projection.mul(cameraRotation.mul(cameraTranslation));
	}
}