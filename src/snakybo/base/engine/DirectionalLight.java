package snakybo.base.engine;

public class DirectionalLight {
	private BaseLight base;
	private Vector3f direction;
	
	public DirectionalLight(BaseLight base, Vector3f direction) {
		this.base = base;
		this.direction = direction.normalized();
	}

	/** Set the base */
	public void setBase(BaseLight base) {
		this.base = base;
	}

	/** Set the direction */
	public void setDirection(Vector3f direction) {
		this.direction = direction.normalized();
	}

	/** @return BaseLight: color */
	public BaseLight getBase() {
		return base;
	}

	/** @return Vector3f: direction */
	public Vector3f getDirection() {
		return direction;
	}
}
