package com.snakybo.sengine.components;

import com.snakybo.sengine.core.Time;
import com.snakybo.sengine.core.input.Input;
import com.snakybo.sengine.core.input.Input.KeyCode;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.object.Component;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 */
public class FreeMove extends Component
{
	private float speed;

	private KeyCode keyForward;
	private KeyCode keyBack;
	private KeyCode keyLeft;
	private KeyCode keyRight;
	
	public FreeMove()
	{
		this(10);
	}
	
	public FreeMove(float speed)
	{
		this(speed, KeyCode.KEY_W);
	}
	
	public FreeMove(float speed, KeyCode keyForward)
	{
		this(speed, keyForward, KeyCode.KEY_S);
	}
	
	public FreeMove(float speed, KeyCode keyForward, KeyCode keyBackward)
	{
		this(speed, keyForward, keyBackward, KeyCode.KEY_A);
	}
	
	public FreeMove(float speed, KeyCode keyForward, KeyCode keyBackward, KeyCode keyLeft)
	{
		this(speed, keyForward, keyBackward, keyLeft, KeyCode.KEY_D);
	}
	
	public FreeMove(float speed, KeyCode keyForward, KeyCode keyBackward, KeyCode keyLeft, KeyCode keyRight)
	{
		this.speed = speed;

		this.keyForward = keyForward;
		this.keyBack = keyBackward;
		this.keyLeft = keyLeft;
		this.keyRight = keyRight;
	}
	
	@Override
	protected void update()
	{
		float moveAmount = speed * Time.getDeltaTime();
		
		if(Input.getKey(keyForward))
		{
			move(getTransform().getLocalRotation().getForward(), moveAmount);
		}
		else if(Input.getKey(keyBack))
		{
			move(getTransform().getLocalRotation().getForward(), -moveAmount);
		}
		
		if(Input.getKey(keyLeft))
		{
			move(getTransform().getLocalRotation().getLeft(), moveAmount);
		}
		else if(Input.getKey(keyRight))
		{
			move(getTransform().getLocalRotation().getRight(), moveAmount);
		}
	}
	
	private final void move(Vector3f direction, float amount)
	{
		getTransform().translate(direction.mul(amount));
	}
	
	public final void setSpeed(float speed)
	{
		this.speed = speed;
	}
	
	public final void setForwardKey(KeyCode key)
	{
		this.keyForward = key;
	}
	
	public final void setLeftKey(KeyCode key)
	{
		this.keyLeft = key;
	}
	
	public final void setBackKey(KeyCode key)
	{
		this.keyBack = key;
	}
	
	public final void setRightKey(KeyCode key)
	{
		this.keyRight = key;
	}
}
