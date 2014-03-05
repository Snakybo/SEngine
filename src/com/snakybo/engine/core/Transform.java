package com.snakybo.engine.core;

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
		position = Vector3f.ZERO;
		rotation = Quaternion.IDENTITY;
		scale = Vector3f.ONE;
		
		parentMatrix = new Matrix4f().initIdentity();
	}
	
	public void update() {
		if(oldPosition == null) {
			oldPosition = new Vector3f().set(position).add(1.0f);
			oldRotation = new Quaternion().set(rotation).mul(0.5f);
			oldScale = new Vector3f().set(scale).add(1.0f);
		} else {
			oldPosition.set(position);
			oldRotation.set(rotation);
			oldScale.set(scale);
		}
	}
	
	public void rotate(Vector3f axis, float angle) {
		rotation = new Quaternion(axis, angle).mul(rotation).normalize();
	}
	
	public boolean hasChanged() {
		if(parent != null && parent.hasChanged())
			return true;
		
		if(!position.equals(oldPosition) || !rotation.equals(oldRotation) || !scale.equals(oldScale))
			return true;
		
		return false;
	}
	
	/** @return The transformation */
	public Matrix4f getTransformation() {
		Matrix4f translationMatrix = new Matrix4f().initPosition(position.getX(), position.getY(), position.getZ());
		Matrix4f rotationMatrix = rotation.toRotationMatrix();
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());
		
		return getParentMatrix().mul(translationMatrix.mul(rotationMatrix.mul(scaleMatrix)));
	}
	
	private Matrix4f getParentMatrix() {
		if(parent != null && parent.hasChanged())
			parentMatrix = parent.getTransformation();
		
		return parentMatrix;
	}
	
	public Vector3f getTransformedPosition() {
		return getParentMatrix().transform(position);
	}
	
	public Quaternion getTransformedRotation() {
		Quaternion parentRotation = Quaternion.IDENTITY;
		
		if(parent != null)
			parentRotation = parent.getTransformedRotation();
		
		return parentRotation.mul(rotation);
	}
	
	public void setParent(Transform parent) { this.parent = parent; }	
	public void setPosition(Vector3f position) { this.position = position; }	
	public void setRotation(Quaternion rotation) { this.rotation = rotation; }
	public void setScale(Vector3f scale) { this.scale = scale; }
	
	public Transform getParent() { return parent; }
	public Vector3f getPosition() { return position; }
	public Quaternion getRotation() { return rotation; }
	public Vector3f getScale() { return scale; }
}
