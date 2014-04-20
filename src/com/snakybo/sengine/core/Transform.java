package com.snakybo.sengine.core;

import com.snakybo.sengine.core.utils.Matrix4f;
import com.snakybo.sengine.core.utils.Quaternion;
import com.snakybo.sengine.core.utils.Vector3f;

/** Transform
 * 
 * Handles an objects transformation
 * 
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public class Transform {
	private Transform parent;
	
	private Matrix4f parentMatrix;
	
	private Vector3f position;
	private Quaternion rotation;
	private Vector3f scale;
	
	private Vector3f oldPosition;
	private Quaternion oldRotation;
	private Vector3f oldScale;
	
	public Transform() {
		position = new Vector3f(0, 0, 0);
		rotation = new Quaternion(0, 0, 0, 1);
		scale = new Vector3f(1, 1, 1);
		
		parentMatrix = new Matrix4f().initIdentity();
	}
	
	/** Update the transform */
	void update() {
		if(oldPosition != null) {
			oldPosition.set(position);
			oldRotation.set(rotation);
			oldScale.set(scale);
		} else {
			oldPosition = new Vector3f(0, 0, 0).set(position).add(1.0f);
			oldRotation = new Quaternion(0, 0, 0, 0).set(rotation).mul(0.5f);
			oldScale = new Vector3f(0, 0, 0).set(scale).add(1.0f);
		}
	}
	
	/** Translate the transform
	 * @param direction The direction to translate to */
	public void translate(Vector3f direction) {
		position = position.add(direction);
	}
	
	/** Rotate the transform
	 * @param axis The axis to rotate on
	 * @param angle The angle to rotate */
	public void rotate(Vector3f axis, float angle) {
		rotation = new Quaternion(axis, angle).mul(rotation).normalize();
	}
	
	/** Make the transform face a specified point
	 * @param point The point to look at
	 * @param up The up direction of the transform */
	public void lookAt(Vector3f point, Vector3f up) {
		rotation = getLookAtRotation(point, up);
	}
	
	/** @return The transformation matrix */
	public Matrix4f getTransformation() {
		Matrix4f translationMatrix = new Matrix4f().initTranslation(position.getX(), position.getY(), position.getZ());
		Matrix4f rotationMatrix = rotation.toRotationMatrix();
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());
		
		return getParentMatrix().mul(translationMatrix.mul(rotationMatrix.mul(scaleMatrix)));
	}
	
	/** @return The parent's transformation matrix */
	private Matrix4f getParentMatrix() {
		if(parent != null && parent.hasChanged())
			parentMatrix = parent.getTransformation();
		
		return parentMatrix;
	}
	
	/** @return The look at rotation
	 * @param point The point to look at
	 * @param up The up position of the game object */
	public Quaternion getLookAtRotation(Vector3f point, Vector3f up) {
		return new Quaternion(new Matrix4f().initRotation(point.sub(position).normalize(), up));
	}
	
	/** @return Whether or not the transformation has changed */
	private boolean hasChanged() {
		if(parent != null && parent.hasChanged())
			return true;
		
		if(!position.equals(oldPosition))
			return true;
		
		if(!rotation.equals(oldRotation))
			return true;
		
		if(!scale.equals(oldScale))
			return true;
		
		return false;
	}
	
	/** Set the parent of the transform
	 * @param parent The new parent */
	public void setParent(Transform parent) {
		this.parent = parent;
	}
	
	/** Set the position of the transform
	 * @param position The new position */
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	/** Set the rotation of the transform
	 * @param rotation The new rotation */
	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}
	
	/** Set the scale of the transform
	 * @param scale The new scale */
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	/** @return The transformated position of the transform */
	public Vector3f getPosition() {
		return getParentMatrix().transform(position);
	}
	
	/** @return The local position of the transform */
	public Vector3f getLocalPosition() {
		return position;
	}
	
	/** @return The transformated rotation of the transform */
	public Quaternion getRotation() {
		Quaternion parentRotation = new Quaternion(0, 0, 0, 1);
		
		if(parent != null)
			parentRotation = parent.getRotation();
		
		return parentRotation.mul(rotation);
	}
	
	/** @return The local rotation of the transform */
	public Quaternion getLocalRotation() {
		return rotation;
	}
	
	/** @return The local scale of transform */
	public Vector3f getLocalScale() {
		return scale;
	}
}
