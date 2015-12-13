package com.snakybo.sengine.core.object;

import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Quaternion;
import com.snakybo.sengine.math.Vector3f;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014
 */
public final class Transform
{	
	private GameObject gameObject;
	
	private Transform parent;
	private Matrix4f parentMatrix;
	
	private Vector3f position;
	private Quaternion rotation;
	private Vector3f scale;
	
	private boolean changed;
	
	public Transform()
	{
		this(new Vector3f());
	}
	
	public Transform(Vector3f position)
	{
		this(position, new Quaternion());
	}
	
	public Transform(Vector3f position, Quaternion rotation)
	{
		this(position, rotation, new Vector3f(1, 1, 1));
	}
	
	public Transform(Vector3f position, Quaternion rotation, Vector3f scale)
	{
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		
		parentMatrix = Matrix4f.identity();
	}
	
	final void setGameObject(GameObject object)
	{
		gameObject = object;
	}
	
	final void update()
	{
		changed = false;
	}
	
	public final void translate(Vector3f direction)
	{
		setPosition(position.add(direction));
	}
	
	public final void rotate(Vector3f axis, double angle)
	{
		rotate(axis, (float)angle);
	}

	public final void rotate(Vector3f axis, float angle)
	{
		setRotation(new Quaternion(axis, angle).mul(rotation).normalized());
	}

	public final void lookAt(Vector3f point, Vector3f up)
	{
		setRotation(getLookAtRotation(point, up));
	}

	public final boolean hasChanged()
	{
		if(parent != null && parent.hasChanged())
		{
			return true;
		}

		return changed;
	}

	public final void setParent(Transform parent)
	{
		this.parent = parent;
	}

	public final void setPosition(Vector3f position)
	{
		this.position = position;
		
		if(!changed)
		{
			if(!position.equals(new Vector3f()))
			{
				changed = true;
			}
		}
	}

	public final void setRotation(Quaternion rotation)
	{
		this.rotation = rotation;
		
		if(!changed)
		{
			if(!rotation.equals(new Quaternion()))
			{
				changed = true;
			}
		}
	}
	
	public final void setScale(float scale)
	{
		setScale(new Vector3f(scale, scale, scale));
	}

	public final void setScale(Vector3f scale)
	{
		this.scale = scale;
		
		if(!changed)
		{
			if(!scale.equals(new Vector3f()))
			{
				changed = true;
			}
		}
	}

	public final Matrix4f getTransformation()
	{
		Matrix4f translationMatrix = Matrix4f.createTranslationMatrix(position);
		Matrix4f rotationMatrix = Matrix4f.createRotationMatrix(rotation);
		Matrix4f scaleMatrix = Matrix4f.createScaleMatrix(scale);

		return getParentMatrix().mul(translationMatrix.mul(rotationMatrix.mul(scaleMatrix)));
	}

	public final Transform getParent()
	{
		return parent;
	}
	
	public final GameObject getGameObject()
	{
		return gameObject;
	}

	public final Vector3f getPosition()
	{
		return getParentMatrix().transform(position);
	}

	public final Quaternion getRotation()
	{
		Quaternion parentRotation = new Quaternion(0, 0, 0, 1);

		if(parent != null)
		{
			parentRotation = parent.getRotation();
		}

		return parentRotation.mul(rotation);
	}

	public final Vector3f getLocalPosition()
	{
		return position;
	}

	public final Quaternion getLocalRotation()
	{
		return rotation;
	}
	
	public final Vector3f getLocalScale()
	{
		return scale;
	}
	
	private final Matrix4f getParentMatrix()
	{
		if(parent != null && parent.hasChanged())
		{
			parentMatrix = parent.getTransformation();
		}
	
		return parentMatrix;
	}

	private final Quaternion getLookAtRotation(Vector3f point, Vector3f up)
	{
		return new Quaternion(Matrix4f.createRotationMatrix(point.sub(position).normalized(), up));
	}
}
