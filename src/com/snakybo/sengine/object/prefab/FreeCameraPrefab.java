package com.snakybo.sengine.object.prefab;

import com.snakybo.sengine.components.CameraComponent;
import com.snakybo.sengine.components.FreeLook;
import com.snakybo.sengine.components.FreeMove;
import com.snakybo.sengine.core.Input.KeyCode;
import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Quaternion;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.utils.Color;
import com.snakybo.sengine.window.Window;

/**
 * @author Kevin
 * @since Jan 3, 2016
 */
public class FreeCameraPrefab extends Prefab
{
	private CameraComponent camera;
	private FreeLook freeLook;
	private FreeMove freeMove;
	
	public FreeCameraPrefab()
	{
		this(new Vector3f());
	}
	
	public FreeCameraPrefab(Vector3f position)
	{
		this(position, new Quaternion());
	}
	
	public FreeCameraPrefab(Vector3f position, Quaternion rotation)
	{
		this(position, rotation, new Vector3f(1, 1, 1));
	}
	
	public FreeCameraPrefab(Vector3f position, Quaternion rotation, Vector3f scale)
	{
		super(position, rotation, scale);
		
		Matrix4f projection = Matrix4f.perspective(90, (float)Window.getWidth() / (float)Window.getHeight(), 0.01f, 1000);
		camera = addComponent(new CameraComponent(projection, new Color(), true));
		freeLook = addComponent(new FreeLook());
		freeMove = addComponent(new FreeMove());
	}
	
	public final void setProjection(Matrix4f projection)
	{
		camera.setProjection(projection);
	}
	
	public final void setLookSensitivity(float sensitivity)
	{
		freeLook.setSensitivity(sensitivity);
	}
	
	public final void setMoveSpeed(float speed)
	{
		freeMove.setSpeed(speed);
	}
	
	public final void setMoveKeyForward(KeyCode key)
	{
		freeMove.setForwardKey(key);
	}
	
	public final void setMoveKeyLeft(KeyCode key)
	{
		freeMove.setLeftKey(key);
	}
	
	public final void setMoveKeyBack(KeyCode key)
	{
		freeMove.setBackKey(key);
	}
	
	public final void setMoveKeyRight(KeyCode key)
	{
		freeMove.setRightKey(key);
	}
}
