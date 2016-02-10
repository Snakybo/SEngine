package com.snakybo.sengine.object;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.shader.Shader;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014 
 */
public class GameObject extends Object
{
	private Set<Component> components;
	private Set<Component> componentsToAdd;
	
	private Transform transform;
	
	boolean destroyed;
	
	public GameObject()
	{
		components = new HashSet<Component>();
		componentsToAdd = new HashSet<Component>();
		transform = new Transform(this);
		
		GameObjectInternal.add(this);
		
		for(Component component : components)
		{
			component.onEnable();
		}
	}
	
	final void destroy()
	{
		if(!destroyed)
		{
			destroyed = true;
			
			for(Component component : components)
			{
				component.onDisable();
				component.onDestroy();
			}
		}
	}

	final void update()
	{
		List<Component> destroyed = new ArrayList<Component>();
		
		for(Component component : components)
		{
			if(!component.startCalled)
			{
				component.startCalled = true;
				component.start();
			}
			
			component.update();
			
			if(component.destroyed)
			{
				destroyed.add(component);
			}
		}
		
		for(Component component : componentsToAdd)
		{
			components.add(component);
		}
		
		for(Component component : destroyed)
		{
			components.remove(component);
		}		
		
		componentsToAdd.clear();
	}
	
	final void render(Shader shader, Camera camera)
	{
		for(Component component : components)
		{
			component.render(shader, camera);
		}
	}
	
	final void removeComponent(Component component)
	{
		if(components.contains(component))
		{
			component.destroyed = true;
			
			component.onDisable();
			component.onDestroy();
		}
	}
	
	@SuppressWarnings("unchecked")
	public final <T extends Component> T addComponent(Component component)
	{
		componentsToAdd.add(component);
		
		component.gameObject = this;
		component.onEnable();

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