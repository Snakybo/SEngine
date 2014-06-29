package com.snakybo.sengine.components;

import com.snakybo.sengine.core.Component;
import com.snakybo.sengine.core.Input;
import com.snakybo.sengine.core.utils.Vector3f;

public class FreeMove extends Component {
	private float speed;
	
	private int keyForward;
	private int keyBackward;
	private int keyLeft;
	private int keyRight;
	
	public FreeMove(float speed) {
		this(speed, Input.KEY_W, Input.KEY_S, Input.KEY_A, Input.KEY_D);
	}
	
	public FreeMove(float speed, int forwardKey, int backKey, int leftKey, int rightKey) {
		this.speed = speed;
		this.keyForward = forwardKey;
		this.keyBackward = backKey;
		this.keyLeft = leftKey;
		this.keyRight = rightKey;
	}
	
	@Override
	public void input(float delta) {
		float movAmt = speed * delta;
		
		if(Input.getKey(keyForward))
			move(getTransform().getRotation().getForward(), movAmt);
		if(Input.getKey(keyBackward))
			move(getTransform().getRotation().getForward(), -movAmt);
		if(Input.getKey(keyLeft))
			move(getTransform().getRotation().getLeft(), movAmt);
		if(Input.getKey(keyRight))
			move(getTransform().getRotation().getRight(), movAmt);
	}
	
	private void move(Vector3f dir, float amt) {
		getTransform().setPosition(getTransform().getPosition().add(dir.mul(amt)));
	}
}
