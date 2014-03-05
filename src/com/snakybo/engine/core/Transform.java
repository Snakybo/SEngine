package com.snakybo.engine.core;

/** @author Kevin Krol */
public class Transform {
	private Transform parent;
	private Matrix4f parentMatrix;
	
	private Vector3f position;
	private Quaternion rotation;
	private Vector3f scale;
	
	private Vector3f oldPosition;
	private Quaternion oldRotation;
	private Vector3f oldScale;
	
	/** Initialize the transformation */
	public Transform() {
		position = Vector3f.ZERO;
		rotation = Quaternion.IDENTITY;
		scale = Vector3f.ONE;
		
		parentMatrix = new Matrix4f().initIdentity();
	}
	
	/** Update the transformation */
	public void update() {
		if(oldPosition == null) {
			oldPosition = new Vector3f(position).add(1.0f);
			oldRotation = new Quaternion(rotation).scale(0.5f);
			oldScale = new Vector3f(scale).add(1.0f);
		} else {
			oldPosition.set(position);
			oldRotation.set(rotation);
			oldScale.set(scale);
		}
	}
	
	/** Rotate the transformation
	 * @param axis The axis to rotate on
	 * @param angle The angle to rotate by */
	public void rotate(Vector3f axis, float angle) {
		rotation = new Quaternion(axis, angle).mul(rotation).normalize();
	}
	
	/** Check whether or not the transformation has changed
	 * @return True if the transformation has changed, false if not */
	public boolean hasChanged() {
		if(parent != null && parent.hasChanged())
			return true;
		
		if(!position.equals(oldPosition) || !rotation.equals(oldRotation)
				|| !scale.equals(oldScale))
			return true;
		
		return false;
	}
	
	/** Set the parent of the transformation
	 * @param parent The parent of the transformation */
	public void setParent(Transform parent) {
		this.parent = parent;
	}
	
	/** Set the position of the transformation
	 * @param position The position of the transformation */
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	/** Set the rotation of the transformation
	 * @param rotation The rotation of the transformation */
	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}
	
	/** Set the scale of the transformation
	 * @param scale The scale of the transformation */
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	/** @return The transformation matrix of the parent */
	private Matrix4f getParentMatrix() {
		if(parent != null && parent.hasChanged())
			parentMatrix = parent.getTransformation();
		
		return parentMatrix;
	}
	
	/** @return The parent of the transformation */
	public Transform getParent() {
		return parent;
	}
	
	/** @return The transformed position, taking parenting into account */
	public Vector3f getTransformedPosition() {
		return getParentMatrix().transform(position);
	}
	
	/** @return The untransformed position of the transformation */
	public Vector3f getPosition() {
		return position;
	}
	
	/** @return The transformed rotation, taking parenting into account */
	public Quaternion getTransformedRotation() {
		Quaternion parentRotation = Quaternion.IDENTITY;
		
		if(parent != null)
			parentRotation = parent.getTransformedRotation();
		
		return parentRotation.mul(rotation);
	}
	
	/** @return The untransformed rotation of the transformation */
	public Quaternion getRotation() {
		return rotation;
	}

	/** @return The scale of the transformation */
	public Vector3f getScale() {
		return scale;
	}
	
	/** @return The transformation */
	public Matrix4f getTransformation() {
		Matrix4f translationMatrix =
				new Matrix4f().initPosition(position);
		Matrix4f rotationMatrix = rotation.toRotationMatrix();
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale);
		
		return getParentMatrix().scale(translationMatrix.scale(rotationMatrix.scale(scaleMatrix)));
	}
}
