package com.snakybo.sengine.components;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.utils.Input;
import com.snakybo.sengine.utils.Input.KeyCode;
import com.snakybo.sengine.utils.math.Vector3f;

/** This class extends the {@link Component} class
 * 
 * <p>
 * Allows the parent game object to move around freely
 * </p>
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014
 * @see Component */
public class FreeMove extends Component {
	private float speed;
	
	private int keyForward;
	private int keyBackward;
	private int keyLeft;
	private int keyRight;
	
	/** Constructor for the component
	 * 
	 * <p>
	 * This constructor will call {@link #FreeMove(float)} with {@code 10.0} as {@code speed}
	 * </p>
	 * 
	 * @param speed The speed of the game object
	 * @see #FreeMove(float) */
	public FreeMove() {
		this(10.0f);
	}
	
	/** Constructor for the component
	 * 
	 * <p>
	 * This constructor will call {@link #FreeMove(float, int)} with {@code W} as {@code keyForward}
	 * </p>
	 * 
	 * @param speed The speed of the game object
	 * @see #FreeMove(float, int) */
	public FreeMove(float speed) {
		this(speed, KeyCode.KEY_W);
	}
	
	/** Constructor for the component
	 * 
	 * <p>
	 * This constructor will call {@link #FreeMove(float, int, int)} with {@code S} as {@code keyBackward}
	 * </p>
	 * 
	 * @param speed The speed of the game object
	 * @param keyForward The key to use to go forward
	 * @see #FreeMove(float, int, int) */
	public FreeMove(float speed, int keyForward) {
		this(speed, keyForward, KeyCode.KEY_S);
	}
	
	/** Constructor for the component
	 * 
	 * <p>
	 * This constructor will call {@link #FreeMove(float, int, int, int)} with {@code A} as {@code keyLeft}
	 * </p>
	 * 
	 * @param speed The speed of the game object
	 * @param keyForward The key to use to go forward
	 * @param keyBackward The key to use to go backward
	 * @see #FreeMove(float, int, int, int) */
	public FreeMove(float speed, int keyForward, int keyBackward) {
		this(speed, keyForward, keyBackward, KeyCode.KEY_A);
	}
	
	/** Constructor for the component
	 * 
	 * <p>
	 * This constructor will call {@link #FreeMove(float, int, int, int, int)} with {@code D} as {@code keyRight}
	 * </p>
	 * 
	 * @param speed The speed of the game object
	 * @param keyForward The key to use to go forward
	 * @param keyBackward The key to use to go backward
	 * @param keyLeft The key to use to go to the left
	 * @see #FreeMove(float, int, int, int, int) */
	public FreeMove(float speed, int keyForward, int keyBackward, int keyLeft) {
		this(speed, keyForward, keyBackward, keyLeft, KeyCode.KEY_D);
	}
	
	/** Constructor for the component
	 * @param speed The speed of the game object
	 * @param keyForward The key to use to go forward
	 * @param keyBackward The key to use to go backward
	 * @param keyLeft The key to use to go to the left
	 * @param keyRight The key to use to go to the right */
	public FreeMove(float speed, int keyForward, int keyBackward, int keyLeft, int keyRight) {
		this.speed = speed;
		
		this.keyForward = keyForward;
		this.keyBackward = keyBackward;
		this.keyLeft = keyLeft;
		this.keyRight = keyRight;
	}
	
	@Override
	protected void input(double delta) {
		float moveAmount = speed * (float)delta;
		
		if(Input.getKey(keyForward))
			move(getTransform().getRotation().getForward(), moveAmount);
		
		if(Input.getKey(keyBackward))
			move(getTransform().getRotation().getForward(), -moveAmount);
		
		if(Input.getKey(keyLeft))
			move(getTransform().getRotation().getLeft(), moveAmount);
		
		if(Input.getKey(keyRight))
			move(getTransform().getRotation().getRight(), moveAmount);
	}
	
	/** Move the game object in the specified direction by the specified amount
	 * @param direction The direction to move in
	 * @param amount The amount of units to move */
	private void move(Vector3f direction, float amount) {
		getTransform().setPosition(getTransform().getPosition().add(direction.mul(amount)));
	}
}
