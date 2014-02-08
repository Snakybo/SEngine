package com.snakybo.engine.core;

public class Transform {
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;
	
	public Transform() {
		position = Vector3f.ZERO;
		rotation = Vector3f.ZERO;
		scale = Vector3f.ONE;
	}
	
	/** @return The transformation */
	public Matrix4f getTransformation() {
		Matrix4f translationMatrix = new Matrix4f().initPosition(position.getX(), position.getY(), position.getZ());
		Matrix4f rotationMatrix = new Matrix4f().initRotation(rotation.getX(), rotation.getY(), rotation.getZ());
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());
		
		return translationMatrix.mul(rotationMatrix.mul(scaleMatrix));
	}
	
	public void setPosition(Vector3f position) { this.position = position; }
	public void setPosition(float x, float y, float z) { this.position = new Vector3f(x, y, z); }
	
	public void setRotation(Vector3f rotation) { this.rotation = rotation; }
	public void setRotation(float x, float y, float z) { this.rotation = new Vector3f(x, y, z); }
	
	public void setScale(Vector3f scale) { this.scale = scale; }
	public void setScale(float x, float y, float z) { this.scale = new Vector3f(x, y, z); }

	public Vector3f getPosition() { return position; }
	public Vector3f getRotation() { return rotation; }
	public Vector3f getScale() { return scale; }
}
