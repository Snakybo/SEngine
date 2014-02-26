package com.snakybo.engine.core;

public class Transform {
	private Vector3f position;
	private Quaternion rotation;
	private Vector3f scale;
	
	public Transform() {
		position = Vector3f.ZERO;
		rotation = Quaternion.IDENTITY;
		scale = Vector3f.ONE;
	}
	
	/** @return The transformation */
	public Matrix4f getTransformation() {
		Matrix4f translationMatrix = new Matrix4f().initPosition(position.getX(), position.getY(), position.getZ());
		Matrix4f rotationMatrix = rotation.toRotationMatrix();
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());
		
		return translationMatrix.mul(rotationMatrix.mul(scaleMatrix));
	}
	
	public void setPosition(Vector3f position) { this.position = position; }	
	public void setRotation(Quaternion rotation) { this.rotation = rotation; }
	public void setScale(Vector3f scale) { this.scale = scale; }

	public Vector3f getPosition() { return position; }
	public Quaternion getRotation() { return rotation; }
	public Vector3f getScale() { return scale; }
}
