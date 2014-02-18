package com.snakybo.engine.renderer;

import com.snakybo.engine.core.Input;
import com.snakybo.engine.core.Input.KeyCode;
import com.snakybo.engine.core.Matrix4f;
import com.snakybo.engine.core.Vector2f;
import com.snakybo.engine.core.Vector3f;

public class Camera {
	public static final Vector3f yAxis = Vector3f.UP;
	
	private Vector3f position;
	private Vector3f forward;
	private Vector3f up;
	
	private Matrix4f projection;
	
	private boolean mouseLocked = false;
	private Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);
	
	/** Create a new camera
	 * @param fov The field of view
	 * @param aspect The aspect ratio of the camera
	 * @param zNear The near clipping plane of the camera
	 * @param zFar The far clipping plane of the camera */
	public Camera(float fov, float aspect, float zNear, float zFar) {
		this.position = Vector3f.ZERO;
		this.forward = Vector3f.FORWARD;
		this.up = Vector3f.UP;
		
		this.projection = new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
	}
	
	/** Handle Input */
	public void input(float delta) {
		float sensitivity = 0.5f;
		float movAmt = (float)(10 * delta);
		
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
		
		if(Input.getKey(KeyCode.KEY_W))
			move(getForward(), movAmt);
		if(Input.getKey(KeyCode.KEY_S))
			move(getForward(), -movAmt);
		if(Input.getKey(KeyCode.KEY_A))
			move(getLeft(), movAmt);
		if(Input.getKey(KeyCode.KEY_D))
			move(getRight(), movAmt);
		
		if(mouseLocked) {
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);
			
			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;
			
			if(rotY)
				rotateY(deltaPos.getX() * sensitivity);
			
			if(rotX)
				rotateX(-deltaPos.getY() * sensitivity);
				
			if(rotY || rotX)
				Input.setMousePosition(new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2));
		}
	}
	
	/** Move the camera in the specified direction
	 * @param direction The direction to move the camera in
	 * @param amount The amount of units to move */
	public void move(Vector3f direction, float amount) {
		position = position.add(direction.mul(amount));
	}
	
	/** Rotate the camera on the Y axis */
	public void rotateY(float angle) {
		Vector3f Haxis = yAxis.cross(forward).normalize();
		
		forward = forward.rotate(angle, yAxis).normalize();
		
		up = forward.cross(Haxis).normalize();
	}
	
	/** Rotate the camera on the X axis */
	public void rotateX(float angle) {
		Vector3f Haxis = yAxis.cross(forward).normalize();
		
		forward = forward.rotate(angle, Haxis).normalize();
		
		up = forward.cross(Haxis).normalize();
	}
	
	public void setPosition(Vector3f position) { this.position = position; }
	public void setForward(Vector3f forward) { this.forward = forward; }
	public void setUp(Vector3f up) { this.up = up; }
	
	public Vector3f getPosition() { return position; }	
	public Vector3f getForward() { return forward; }
	public Vector3f getUp() { return up; }
	
	public Vector3f getLeft() { return forward.cross(up).normalize(); }
	public Vector3f getRight() { return up.cross(forward).normalize(); }
	
	/** @return The projection matrix */
	public Matrix4f getProjection() {
		Matrix4f cameraRotation = new Matrix4f().initCamera(forward, up);
		Matrix4f cameraTranslation = new Matrix4f().initPosition(-position.getX(), -position.getY(), -position.getZ());
		
		return projection.mul(cameraRotation.mul(cameraTranslation));
	}
}