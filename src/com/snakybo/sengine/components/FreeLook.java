package com.snakybo.sengine.components;

import com.snakybo.sengine.core.Input;
import com.snakybo.sengine.core.Input.KeyCode;
import com.snakybo.sengine.core.Input.MouseButton;
import com.snakybo.sengine.core.Vector2f;
import com.snakybo.sengine.core.Vector3f;
import com.snakybo.sengine.rendering.Window;

/** Free look component extends {@link Component}
 * 
 * <p>
 * Allows the parent game object to rotate freely using the mouse
 * </p>
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class FreeLook extends Component {
	private static final Vector3f yAxis = new Vector3f(0, 1, 0);
	
	private boolean mouseLocked = false;
	
	private float sensitivity;
	
	private int unlockMouseKey;
	
	/** Constructor for the free look component
	 * @param sensitivity The mouse sensitivity */
	public FreeLook(float sensitivity) {
		this(sensitivity, KeyCode.ESCAPE);
	}
	
	/** Constructor for the free look component
	 * @param sensitivity The mouse sensitivity
	 * @param unlockMouseKey The key the unlocks the mouse cursor */
	public FreeLook(float sensitivity, int unlockMouseKey) {
		this.sensitivity = sensitivity;
		this.unlockMouseKey = unlockMouseKey;
	}
	
	@Override
	public void input(float delta) {
		Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);
		
		if(Input.getKey(unlockMouseKey)) {
			Input.setCursor(true);
			mouseLocked = false;
		}
		
		if(Input.getMouseDown(MouseButton.LEFT)) {
			Input.setMousePosition(centerPosition);
			Input.setCursor(false);
			mouseLocked = true;
		}
		
		if(mouseLocked) {
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);
			
			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;
			
			if(rotY)
				getTransform().rotate(yAxis, (float)Math.toRadians(deltaPos.getX() * sensitivity));
			
			if(rotX)
				getTransform().rotate(getTransform().getLocalRotation().getRight(),
						(float)Math.toRadians(-deltaPos.getY() * sensitivity));
			
			if(rotY || rotX)
				Input.setMousePosition(centerPosition);
		}
	}
}
