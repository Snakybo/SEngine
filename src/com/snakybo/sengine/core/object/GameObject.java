package com.snakybo.sengine.core.object;

import java.util.ArrayList;
import java.util.List;

import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.shader.Shader;
import com.snakybo.sengine.utils.math.Quaternion;
import com.snakybo.sengine.utils.math.Vector3f;

/**
 * @author Kevin Krol
 * @since Apr 4, 2014 
 */
public final class GameObject
{
	private ArrayList<GameObject> children;
	private ArrayList<GameObject> childrenToRemove;
	
	private ArrayList<Component> components;
	private ArrayList<Component> componentsToRemove;
	
	private Transform transform;
	
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
		children = new ArrayList<GameObject>();
		childrenToRemove = new ArrayList<GameObject>();
		
		components = new ArrayList<Component>();
		componentsToRemove = new ArrayList<Component>();
		
		transform = new Transform(position, rotation, scale);
		transform.setGameObject(this);
	}
	
	public final void update(float delta)
	{
		updateInternal();
		
		for(Component component : components)
		{
			component.update(delta);
		}
		
		for(GameObject child : children)
		{
			child.update(delta);
		}
	}
	
	public final void render(RenderingEngine renderingEngine, Shader shader)
	{
		for(Component component : components)
		{
			component.render(renderingEngine, shader);
		}
		
		for(GameObject child : children)
		{
			child.render(renderingEngine, shader);
		}
	}
	
	public final void addChild(GameObject child)
	{
		children.add(child);

		child.transform.setParent(transform);

		for(Component component : child.components)
		{
			component.onAddedToScene();
		}
	}
	
	public final void removeAllChildren()
	{
		for(GameObject child : children)
		{
			removeChild(child);
		}
	}
	
	public final void removeChild(GameObject child)
	{
		if(children.contains(child) && !childrenToRemove.contains(child))
		{
			childrenToRemove.add(child);
		}
	}
	
	public final GameObject addComponent(Component component)
	{
		components.add(component);
		component.setParent(this);

		return this;
	}
	
	public final <T extends Component> void removeComponents(Class<T> type)
	{
		for(Component component : components)
		{
			if(component.getClass() == type)
			{				
				removeComponent(component);
			}
		}
	}
	
	public final <T extends Component> void removeComponent(Class<T> type)
	{
		for(Component component : components)
		{
			if(component.getClass() == type)
			{				
				removeComponent(component);
				break;
			}
		}
	}
	
	public final void removeComponents(Component... components)
	{
		for(Component component : components)
		{
			removeComponent(component);
		}
	}
	
	public final void removeComponent(Component component)
	{
		if(components.contains(component) && !componentsToRemove.contains(component))
		{
			componentsToRemove.add(component);
		}
	}
	
	private final void updateInternal()
	{
		transform.update();
		
		for(GameObject child : childrenToRemove)
		{
			for(Component component : child.components)
			{
				component.onRemovedFromScene();
			}
			
			child.transform.setParent(null);
			children.remove(child);
		}
		
		for(Component component : componentsToRemove)
		{
			component.onRemovedFromScene();
			component.setParent(null);
			components.remove(component);
		}
		
		childrenToRemove.clear();
		componentsToRemove.clear();
	}
	
	public final Iterable<GameObject> getChildren()
	{
		ArrayList<GameObject> result = new ArrayList<GameObject>();

		for(GameObject child : children)
		{
			Iterable<GameObject> childChildren = child.getChildren();
			
			for(GameObject childChild : childChildren)
			{
				result.add(childChild);
			}
		}

		result.add(this);
		return result;
	}
	
	public final GameObject getChild(int index)
	{
		if(index >= children.size())
		{
			return null;
		}
		
		return children.get(index);
	}
	
	public <T extends Component> Iterable<T> getComponentsInChildren(Class<T> type)
	{
		List<T> result = new ArrayList<T>();
		
		for(GameObject child : children)
		{
			Iterable<T> childResults = child.getComponentsInChildren(type);
			
			for(T component : childResults)
			{
				result.add(component);
			}
		}
		
		Iterable<T> ownResults = getComponentsInChildren(type);		
		for(T component : ownResults)
		{
			result.add(component);
		}
		
		return result;
	}
	
	public <T extends Component> Iterable<T> getComponents(Class<T> type)
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
	
	public <T extends Component> T getComponent(Class<T> type)
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
	
	public final int getNumChildren()
	{
		return children.size();
	}
	
	public final Transform getTransform()
	{
		return transform;
	}
}
