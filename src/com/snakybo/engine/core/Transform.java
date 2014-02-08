package snakybo.base.engine;

public class Transform {
	private static Camera camera;
	
	private static float zNear;
	private static float zFar;
	private static float width;
	private static float height;
	private static float fov;
	
	private Vector3f translation;
	private Vector3f rotation;
	private Vector3f scale;
	
	public Transform() {
		translation = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = new Vector3f(1, 1, 1);
	}
	
	/** @return Matrix4f: Transformation */
	public Matrix4f getTransformation() {
		Matrix4f translationMatrix = new Matrix4f().initTranslation(translation.getX(), translation.getY(), translation.getZ());
		Matrix4f rotationMatrix = new Matrix4f().initRotation(rotation.getX(), rotation.getY(), rotation.getZ());
		Matrix4f scaleMatrix = new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ());
		
		return translationMatrix.mul(rotationMatrix.mul(scaleMatrix));
	}
	
	/** @return Matrix4f: Projected transformation */
	public Matrix4f getProjectedTransformation() {
		Matrix4f transformationMatrix = getTransformation();
		Matrix4f projectionMatrix = new Matrix4f().initProjection(fov, width, height, zNear, zFar);
		Matrix4f cameraRotation = new Matrix4f().initCamera(camera.getForward(), camera.getUp());
		Matrix4f cameraTranslation = new Matrix4f().initTranslation(-camera.getPos().getX(), -camera.getPos().getY(), -camera.getPos().getZ());
		
		return projectionMatrix.mul(cameraRotation.mul(cameraTranslation.mul(transformationMatrix)));
	}
	
	/** Set projection */
	public static void setProjection(float fov, float width, float height, float zNear, float zFar) {
		Transform.fov = fov;
		Transform.width = width;
		Transform.height = height;
		Transform.zNear = zNear;
		Transform.zFar = zFar;
	}
	
	/** Set translation using a Vector3f */
	public void setTranslation(Vector3f translation) {
		this.translation = translation;
	}
	
	/** Set trasformation using floats */
	public void setTranslation(float x, float y, float z) {
		this.translation = new Vector3f(x, y, z);
	}
	
	/** Set rotation using a Vector3f */
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	/** Set rotation using floats */
	public void setRotation(float x, float y, float z) {
		this.rotation = new Vector3f(x, y, z);
	}
	
	/** Set scale using a Vector3f */
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	/** Set scale using floats */
	public void setScale(float x, float y, float z) {
		this.scale = new Vector3f(x, y, z);
	}
	
	/** Set camera */
	public static void setCamera(Camera camera) {
		Transform.camera = camera;
	}
	
	/** @return Vector3f: Object's translation */
	public Vector3f getTranslation() {
		return translation;
	}

	/** @return Vector3f: Object's rotation */
	public Vector3f getRotation() {
		return rotation;
	}

	/** @return Vector3f: Object's scale */
	public Vector3f getScale() {
		return scale;
	}

	/** @return Camera: Camera object */
	public static Camera getCamera() {
		return camera;
	}
}
