package com.snakybo.sengine.components;

import com.snakybo.sengine.core.input.Input;
import com.snakybo.sengine.math.Vector2f;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.object.Component;
import com.snakybo.sengine.window.Window;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 */
public class FreeLook extends Component
{	
	private float sensitivity;
	
	public FreeLook()
	{
		this(0.5f);
	}
	
	public FreeLook(float sensitivity)
	{
		this.sensitivity = sensitivity;
	}
	
	@Override
	protected void onAddedToScene()
	{
		Input.setMousePosition(Window.getCenter());
		Input.setCursor(true);
	}
	
	@Override
	protected void onRemovedFromScene()
	{
		Input.setCursor(false);
	}

	@Override
	protected void update()
	{
		Vector2f deltaPos = Input.getMousePosition().sub(Window.getCenter());
		
		if(deltaPos.x != 0)
		{
			rotate(new Vector3f(0, 1, 0), -deltaPos.x);
		}
	
		if(deltaPos.y != 0)
		{
			rotate(getTransform().getLocalRotation().getRight(), deltaPos.y);
		}

		Input.setMousePosition(Window.getCenter());
	}
	
	private final void rotate(Vector3f axis, float amount)
	{
		getTransform().rotate(axis, Math.toRadians(-amount * sensitivity));
	}
	
	public final void setSensitivity(float sensitivity)
	{
		this.sensitivity = sensitivity;
	}
	
	public final float getSensitivity()
	{
		return sensitivity;
	}
}