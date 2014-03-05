package com.snakybo.engine.components;

import com.snakybo.engine.core.Input;
import com.snakybo.engine.core.Input.KeyCode;
import com.snakybo.engine.core.Matrix4f;
import com.snakybo.engine.core.Vector2f;
import com.snakybo.engine.core.Vector3f;
import com.snakybo.engine.renderer.Renderer;
import com.snakybo.engine.renderer.Window;

/** @author Kevin Krol */
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
	
	/** Handle Input camera input
	 * @param delta The delta time */
	@Override
	public void input(float delta) {
		float sensitivity = 0.5f;
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
		
		if(Input.getKey(KeyCode.W)) {
			move(getTransform().getRotation().front(), moveAmount);
		} else if(Input.getKey(KeyCode.S)) {
			move(getTransform().getRotation().front(), -moveAmount);
		}
		
		if(Input.getKey(KeyCode.A)) {
			move(getTransform().getRotation().left(), moveAmount);
		} else if(Input.getKey(KeyCode.D)) {
			move(getTransform().getRotation().right(), moveAmount);
		}
		
		if(mouseLocked) {
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);
			
			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;
			
			if(rotY)
				getTransform().rotate(Vector3f.UP,
						(float)Math.toRadians(deltaPos.getX() * sensitivity));
			
			if(rotX)
				getTransform().rotate(getTransform().getRotation().right(),
						(float)Math.toRadians(-deltaPos.getY() * sensitivity));
			
			if(rotY || rotX)
				Input.setMousePosition(new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2));
		}
	}
	
	/** Move the camera in the specified direction
	 * @param direction The direction to move the camera in
	 * @param amount The amount of units to move */
	public void move(Vector3f direction, float amount) {
		getTransform().setPosition(getTransform().getPosition().add(direction.scale(amount)));
	}
	
	/** @return The projection matrix */
	public Matrix4f getProjection() {
		Matrix4f cameraRotation =
				getTransform().getTransformedRotation().conjugate().toRotationMatrix();
		Vector3f cameraPosition = getTransform().getTransformedPosition().scale(-1);
		
		Matrix4f cameraTranslation = new Matrix4f().initPosition(cameraPosition);
		
		return projection.scale(cameraRotation.scale(cameraTranslation));
	}
}