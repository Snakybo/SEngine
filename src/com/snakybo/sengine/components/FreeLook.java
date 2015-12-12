package com.snakybo.sengine.components;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.utils.Input;
import com.snakybo.sengine.utils.Input.KeyCode;

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

	private Vector2f windowCenter;

	private float sensitivity;

	private int unlockMouseKey;

	private boolean mouseLocked;

	/** Constructor for the component
	 * <p>
	 * This constructor will call {@link #FreeLook(Vector2f, float)} with
	 * {@code 0.5} as {@code sensitivity}
	 * </p>
	 * @param windowCenter The center position of the window, the mouse will
	 *        stay locked in this position
	 * @see #FreeLook(Vector2f, float) */
	public FreeLook(Vector2f windowCenter)
	{
		this(windowCenter, 0.5f);
	}

	/** Constructor for the component
	 * <p>
	 * This constructor will call {@link #FreeLook(Vector2f, float, int)} with
	 * {@code KeyCode.KEY_ESCAPE}
	 * </p>
	 * @param windowCenter The center position of the window, the mouse will
	 *        stay locked in this position
	 * @param sensitivity The sensitivity of the mouse
	 * @see #FreeLook(Vector2f, float, int) */
	public FreeLook(Vector2f windowCenter, float sensitivity)
	{
		this(windowCenter, sensitivity, KeyCode.KEY_ESCAPE);
	}

	/** Constructor for the component
	 * @param windowCenter The center position of the window, the mouse will
	 *        stay locked in this position
	 * @param sensitivity The sensitivity of the mouse
	 * @param unlockMouseKey The key code to unlock the mouse */
	public FreeLook(Vector2f windowCenter, float sensitivity, int unlockMouseKey)
	{
		this.windowCenter = windowCenter;
		this.sensitivity = sensitivity;
		this.unlockMouseKey = unlockMouseKey;

		mouseLocked = false;
	}

	@Override
	protected void update(float delta)
	{
		if(Input.getKey(unlockMouseKey))
		{
			Input.setCursor(true);
			mouseLocked = false;
		}

		if(mouseLocked)
		{
			Vector2f deltaPos = Input.getMousePosition().sub(windowCenter);

			boolean rotY = deltaPos.x != 0;
			boolean rotX = deltaPos.y != 0;

			if(rotY)
				rotate(yAxis, -deltaPos.x);
			if(rotX)
				rotate(getTransform().getLocalRotation().getRight(), deltaPos.y);

			if(rotY || rotX)
				Input.setMousePosition(windowCenter);
		}

		if(Input.getMouseDown(0))
		{
			Input.setMousePosition(windowCenter);
			Input.setCursor(false);
			mouseLocked = true;
		}
	}

	/** Rotate the camera, this method is a convenience method
	 * @param axis The axis to rotate the camera on
	 * @param amount The amount of degrees to rotate the camera */
	private void rotate(Vector3f axis, float amount)
	{
		getTransform().rotate(axis, (float)Math.toRadians(-amount * sensitivity));
	}
}
