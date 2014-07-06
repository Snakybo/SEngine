package com.snakybo.sengine.components;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.rendering.Window;
import com.snakybo.sengine.utils.Input;
import com.snakybo.sengine.utils.Input.KeyCode;
import com.snakybo.sengine.utils.math.Vector2f;
import com.snakybo.sengine.utils.math.Vector3f;

/** This class extends the {@link Component} class
 * 
 * <p>
 * Allows the parent game object to rotate freely using the mouse
 * </p>
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see Component */
public class FreeLook extends Component {
	private static final Vector3f yAxis = new Vector3f(0, 1, 0);
	
	private boolean mouseLocked = false;
	private float sensitivity;
	private int unlockMouseKey;
	
	/** Constructor for the component
	 * 
	 * <p>
	 * This constructor will call {@link #FreeLook(float, int)} with the Escape key as unlock button
	 * </p>
	 * 
	 * @param sensitivity The sensitivity of the mouse
	 * @see #FreeLook(float, int) */
	public FreeLook(float sensitivity) {
		this(sensitivity, KeyCode.KEY_ESCAPE);
	}
	
	/** Constructor for the component
	 * @param sensitivity The sensitivity of the mouse
	 * @param unlockMouseKey The key code to unlock the mouse */
	public FreeLook(float sensitivity, int unlockMouseKey) {
		this.sensitivity = sensitivity;
		this.unlockMouseKey = unlockMouseKey;
	}
	
	@Override
	protected void input(double delta) {
		Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);
		
		if(Input.getKey(unlockMouseKey)) {
			Input.setCursor(true);
			mouseLocked = false;
		}
		
		if(Input.getMouseDown(0)) {
			Input.setMousePosition(centerPosition);
			Input.setCursor(false);
			mouseLocked = true;
		}
		
		if(mouseLocked) {
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);
			
			boolean rotY = deltaPos.x != 0;
			boolean rotX = deltaPos.y != 0;
			
			if(rotY)
				getTransform().rotate(yAxis, (float)Math.toRadians(deltaPos.x * sensitivity));
			if(rotX)
				getTransform().rotate(getTransform().getRotation().getRight(),
						(float)Math.toRadians(-deltaPos.y * sensitivity));
			
			if(rotY || rotX)
				Input.setMousePosition(centerPosition);
		}
	}
}
