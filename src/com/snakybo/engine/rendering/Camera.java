package snakybo.base.engine;

public class Camera {
	public static final Vector3f yAxis = new Vector3f(0,1,0);
	
	private Vector3f pos;
	private Vector3f forward;
	private Vector3f up;
	
	private boolean mouseLocked = false;
	private Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);
	
	public Camera() {
		this(new Vector3f(0,0,0), new Vector3f(0,0,1), new Vector3f(0,1,0));
	}
	
	public Camera(Vector3f pos, Vector3f forward, Vector3f up) {
		this.pos = pos;
		this.forward = forward.normalized();
		this.up = up.normalized();
	}
	
	/** Handle Input */
	public void input() {
		float sensitivity = 0.5f;
		float movAmt = (float)(10 * Time.getDelta());
		
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
		
		if(Input.getKey(Input.KEY_W))
			move(getForward(), movAmt);
		if(Input.getKey(Input.KEY_S))
			move(getForward(), -movAmt);
		if(Input.getKey(Input.KEY_A))
			move(getLeft(), movAmt);
		if(Input.getKey(Input.KEY_D))
			move(getRight(), movAmt);
		
		if(mouseLocked) {
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);
			
			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;
			
			if(rotY)
				rotateY(deltaPos.getX() * sensitivity);
			if(rotX)
				rotateX(-deltaPos.getY() * sensitivity);
				
			if(rotY || rotX)
				Input.setMousePosition(new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2));
		}
		
//		if(Input.getKey(Input.KEY_UP))
//			rotateX(-rotAmt);
//		if(Input.getKey(Input.KEY_DOWN))
//			rotateX(rotAmt);
//		if(Input.getKey(Input.KEY_LEFT))
//			rotateY(-rotAmt);
//		if(Input.getKey(Input.KEY_RIGHT))
//			rotateY(rotAmt);
	}
	
	/** Move camera */
	public void move(Vector3f dir, float amt) {
		pos = pos.add(dir.mul(amt));
	}
	
	/** Rotate Y axis */
	public void rotateY(float angle) {
		Vector3f Haxis = yAxis.cross(forward).normalized();
		
		forward = forward.rotate(angle, yAxis).normalized();
		
		up = forward.cross(Haxis).normalized();
	}
	
	/** Rotate X axis */
	public void rotateX(float angle) {
		Vector3f Haxis = yAxis.cross(forward).normalized();
		
		forward = forward.rotate(angle, Haxis).normalized();
		
		up = forward.cross(Haxis).normalized();
	}
	
	/** Set position */
	public void setPos(Vector3f pos) {
		this.pos = pos;
	}
	
	/** Set forward */
	public void setForward(Vector3f forward) {
		this.forward = forward;
	}
	
	/** Set up */
	public void setUp(Vector3f up) {
		this.up = up;
	}
	
	/** @return Vector3f: Pos */
	public Vector3f getPos() {
		return pos;
	}
	
	/** @return Vector3f: Forward */
	public Vector3f getForward() {
		return forward;
	}
	
	/** @return Vector3f: Up */
	public Vector3f getUp() {
		return up;
	}
	
	/** @return Vector3f: Left */
	public Vector3f getLeft() {
		return forward.cross(up).normalized();
	}
	
	/** @return Vector3f: Right */
	public Vector3f getRight() {
		return up.cross(forward).normalized();
	}
}