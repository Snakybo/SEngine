package com.snakybo.sengine.object;

import java.util.ArrayList;
import java.util.List;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.math.Quaternion;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.shader.Shader;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014 
 */
public class GameObject extends Object
{
	private List<Component> components;
	private List<Component> componentsToAdd;
	private List<Component> componentsToRemove;
	
	private Transform transform;
	
	private boolean destroyed;
	
	public GameObject()
	{
		this(new Vector3f());
	}
	
	public GameObject(Vector3f position)
	{
		this(position, new Quaternion());
	}
	
	public GameObject(Vector3f position, Quaternion rotation)
	{
		this(position, rotation, new Vector3f(1, 1, 1));
	}
	
	public GameObject(Vector3f position, Quaternion rotation, Vector3f scale)
	{
		components = new ArrayList<Component>();
		componentsToAdd = new ArrayList<Component>();
		componentsToRemove = new ArrayList<Component>();
		
		transform = new Transform(position, rotation, scale);
		transform.setGameObject(this);
		
		GameObjectInternal.add(this);
	}
	
	@Override
	protected final void finalize() throws Throwable
	{
		try
		{
			destroy();
		}
		finally
		{
			super.finalize();
		}
	}
	
	final void destroy()
	{
		if(!destroyed)
		{
			destroyed = true;
			
			for(Component component : components)
			{
				component.onDestroy();
			}
		}
	}

	final void update()
	{
		List<Component> componentsToAddCache = new ArrayList<Component>(componentsToAdd);
		List<Component> componentsToRemoveCache = new ArrayList<Component>(componentsToRemove);
		
		componentsToAdd.clear();
		componentsToRemove.clear();
		
		for(Component component : componentsToAddCache)
		{
			component.gameObject = this;
			component.onAddedToScene();
			components.add(component);
		}
		
		for(Component component : componentsToRemoveCache)
		{
			component.onRemovedFromScene();
			component.onDestroy();
			component.gameObject = null;
			components.remove(component);
		}
		
		for(Component component : components)
		{
			component.update();
		}
	}
	
	final void render(Shader shader, Camera camera)
	{
		for(Component component : components)
		{
			component.render(shader, camera);
		}
	}
	
	final void addToScene()
	{
		for(Component component : components)
		{
			component.onAddedToScene();
		}
	}
	
	final void removeFromScene()
	{
		for(Component component : components)
		{
			component.onRemovedFromScene();
		}
	}
	
	final void removeComponent(Component component)
	{
		if(components.contains(component) && !componentsToRemove.contains(component))
		{
			componentsToRemove.add(component);
		}
	}
	
	@SuppressWarnings("unchecked")
	public final <T extends Component> T addComponent(Component component)
	{
		componentsToAdd.add(component);

		return (T)component.getClass().cast(component);
	}
	
	public final <T extends Component> Iterable<T> getComponents(Class<T> type)
	{
		List<T> result = new ArrayList<T>();
		
		for(Component component : components)
		{
			if(component.getClass() == type)
			{
				result.add(type.cast(component));
			}
		}
		
		return result;
	}
	
	public final <T extends Component> T getComponent(Class<T> type)
	{
		for(Component component : components)
		{
			if(component.getClass() == type)
			{
				return type.cast(component);
			}
		}
		
		return null;
	}
	
	public final Transform getTransform()
	{
		return transform;
	}
}