package com.snakybo.sengine.components;

import com.snakybo.sengine.core.Input;
import com.snakybo.sengine.core.Input.KeyCode;
import com.snakybo.sengine.core.Vector3f;

/** Free move component extends {@link Component}
 * 
 * <p>
 * Allows the parent game object to move freely
 * </p>
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class FreeMove extends Component {
	private float speed;
	
	private int keyForward;
	private int keyLeft;
	private int keyBack;
	private int keyRight;
	
	/** Constructor for the free move component
	 * @param speed The speed of the component */
	public FreeMove(float speed) {
		this(speed, KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D);
	}
	
	/** Constructor for the free move component
	 * @param speed The speed of the component
	 * @param keyForward The key to move forward
	 * @param keyLeft The key to move to the left
	 * @param keyBack The key to move backwards
	 * @param keyRight The key to move to the right */
	public FreeMove(float speed, int keyForward, int keyLeft, int keyBack, int keyRight) {
		this.speed = speed;
		this.keyForward = keyForward;
		this.keyLeft = keyLeft;
		this.keyBack = keyBack;
		this.keyRight = keyRight;
	}
	
	@Override
	public void input(float delta) {
		float movAmt = speed * delta;
		
		if(Input.getKey(keyForward))
			move(getTransform().getLocalRotation().getForward(), movAmt);
		
		if(Input.getKey(keyBack))
			move(getTransform().getLocalRotation().getForward(), -movAmt);
		
		if(Input.getKey(keyLeft))
			move(getTransform().getLocalRotation().getLeft(), movAmt);
		
		if(Input.getKey(keyRight))
			move(getTransform().getLocalRotation().getRight(), movAmt);
	}
	
	/** Move the game object to the specified position
	 * @param direction The direction to move in
	 * @param amount The amount of units to move */
	private void move(Vector3f direction, float amount) {
		getTransform().setPosition(getTransform().getLocalPosition().add(direction.mul(amount)));
	}
}
