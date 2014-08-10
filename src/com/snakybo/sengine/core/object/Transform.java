package com.snakybo.sengine.core.object;

import com.snakybo.sengine.utils.math.Matrix4f;
import com.snakybo.sengine.utils.math.Quaternion;
import com.snakybo.sengine.utils.math.Vector3f;

/** The transform class, every game object has a transform by default
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class Transform {
	private Transform parent;
	private Matrix4f parentMatrix;
	
	private Vector3f position;
	private Quaternion rotation;
	private float scale;
	
	private Vector3f oldPosition;
	private Quaternion oldRotation;
	private float oldScale;
	
	/** Constructor for the transform This constructor will call {@link #Transform(Vector3f)}
	 * @see #Transform(Vector3f) */
	public Transform() {
		this(new Vector3f());
	}
	
	/** Constructor for the transform This constructor will call
	 * {@link #Transform(Vector3f, Quaternion)}
	 * @param position The position of the transform
	 * @see #Transform(Vector3f, Quaternion) */
	public Transform(Vector3f position) {
		this(position, new Quaternion());
	}
	
	/** Constructor for the transform This constructor will call
	 * {@link #Transform(Vector3f, Quaternion, float)}
	 * @param position The position of the transform
	 * @param rotation The rotation of the transform
	 * @see #Transform(Vector3f, Quaternion, float) */
	public Transform(Vector3f position, Quaternion rotation) {
		this(position, rotation, 1.0f);
	}
	
	/** Constructor for the transform
	 * @param position The position of the transform
	 * @param rotation The rotation of the transform
	 * @param scale The scale of the transform */
	public Transform(Vector3f position, Quaternion rotation, float scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		
		parentMatrix = new Matrix4f().initIdentity();
	}
	
	/** Update the transform, sets the old transform values to the current values */
	public final void update() {
		if(oldPosition != null) {
			oldPosition.set(position);
			oldRotation.set(rotation);
			oldScale = scale;
		} else {
			oldPosition = new Vector3f(position).add(1.0f);
			oldRotation = new Quaternion(rotation).mul(0.5f);
			oldScale = scale + 1.0f;
		}
	}
	
	public void rotate(Vector3f axis, float angle) {
		rotation = new Quaternion(axis, angle).mul(rotation).normalized();
	}
	
	public void lookAt(Vector3f point, Vector3f up) {
		rotation = getLookAtRotation(point, up);
	}
	
	public Quaternion getLookAtRotation(Vector3f point, Vector3f up) {
		return new Quaternion(new Matrix4f().initRotation(point.sub(position).normalized(), up));
	}
	
	public boolean hasChanged() {
		if(parent != null && parent.hasChanged())
			return true;
		
		if(!position.equals(oldPosition))
			return true;
		
		if(!rotation.equals(oldRotation))
			return true;
		
		if(scale != oldScale)
			return true;
		
		return false;
	}
	
	public void setParent(Transform parent) {
		this.parent = parent;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public Matrix4f getTransformation() {
		Matrix4f translationMatrix = new Matrix4f().initTranslation(position.x, position.y, position.z);
		Matrix4f rotationMatrix = rotation.toRotationMatrix();
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale, scale, scale);
		
		return getParentMatrix().mul(translationMatrix.mul(rotationMatrix.mul(scaleMatrix)));
	}
	
	private Matrix4f getParentMatrix() {
		if(parent != null && parent.hasChanged())
			parentMatrix = parent.getTransformation();
		
		return parentMatrix;
	}
	
	public Vector3f getWorldPosition() {
		return getParentMatrix().transform(position);
	}
	
	public Quaternion getWorldRotation() {
		Quaternion parentRotation = new Quaternion(0, 0, 0, 1);
		
		if(parent != null)
			parentRotation = parent.getWorldRotation();
		
		return parentRotation.mul(rotation);
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public Quaternion getRotation() {
		return rotation;
	}
	
	public float getScale() {
		return scale;
	}
}
