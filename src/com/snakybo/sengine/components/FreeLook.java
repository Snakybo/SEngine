package com.snakybo.sengine.components;

import com.snakybo.sengine.core.Input;
import com.snakybo.sengine.core.Input.KeyCode;
import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.rendering.glfw.GLFWWindow;

/** This class extends the {@link Component} class
 * <p>
 * Allows the parent game object to rotate freely using the mouse
 * </p>
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see Component */
public class FreeLook extends Component
{
	private static final Vector3f yAxis = new Vector3f(0, 1, 0);
	
	private float sensitivity;
	
	private int unlockMouseKey;

	private boolean mouseLocked;
	
	public FreeLook()
	{
		this(0.5f);
	}
	
	public FreeLook(float sensitivity)
	{
		this(sensitivity, KeyCode.KEY_ESCAPE);
	}
	
	public FreeLook(float sensitivity, int unlockMouseKey)
	{
		this.sensitivity = sensitivity;
		this.unlockMouseKey = unlockMouseKey;

		mouseLocked = false;
	}

	@Override
	protected void update()
	{
		Vector2f center = GLFWWindow.getCenter();
		
		if(Input.getKey(unlockMouseKey))
		{
			Input.setCursor(true);
			mouseLocked = false;
		}

		if(mouseLocked)
		{
			Vector2f deltaPos = Input.getMousePosition().sub(center);

			boolean rotY = deltaPos.x != 0;
			boolean rotX = deltaPos.y != 0;

			if(rotY)
			{
				rotate(yAxis, -deltaPos.x);
			}
		
			if(rotX)
			{
				rotate(getTransform().getLocalRotation().getRight(), deltaPos.y);
			}

			if(rotY || rotX)
			{
				Input.setMousePosition(center);
			}
		}

		if(Input.getMouseDown(0))
		{
			Input.setMousePosition(center);
			Input.setCursor(false);
			mouseLocked = true;
		}
	}

	/** Rotate the camera, this method is a convenience method
	 * @param axis The axis to rotate the camera on
	 * @param amount The amount of degrees to rotate the camera */
	private void rotate(Vector3f axis, float amount)
	{
		getTransform().rotate(axis, Math.toRadians(-amount * sensitivity));
	}
}
