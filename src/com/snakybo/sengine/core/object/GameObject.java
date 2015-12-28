package com.snakybo.sengine.core.object;

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
public class GameObject
{
	private ArrayList<GameObject> children;
	private ArrayList<GameObject> childrenToRemove;
	private ArrayList<GameObject> childrenToDestroy;
	
	private ArrayList<Component> components;
	private ArrayList<Component> componentsToRemove;
	
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
		children = new ArrayList<GameObject>();
		childrenToRemove = new ArrayList<GameObject>();
		childrenToDestroy = new ArrayList<GameObject>();
		
		components = new ArrayList<Component>();
		componentsToRemove = new ArrayList<Component>();
		
		transform = new Transform(position, rotation, scale);
		transform.setGameObject(this);
	}
	
	@Override
	protected void finalize() throws Throwable
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
	
	public final void destroy()
	{
		if(!destroyed)
		{
			destroyed = true;
			
			if(transform.getParent() != null)
			{
				transform.getParent().getGameObject().removeChildAndDestroy(this);
			}
			
			for(Component component : components)
			{
				component.onDestroy();
			}
			
			for(GameObject child : children)
			{
				child.destroy();
			}
		}
	}

	public final void update()
	{
		updateInternal();
		
		for(Component component : components)
		{
			component.update();
		}
		
		for(GameObject child : children)
		{
			child.update();
		}
	}
	
	public final void render(Shader shader, Camera camera)
	{
		for(Component component : components)
		{
			component.render(shader, camera);
		}
		
		for(GameObject child : children)
		{
			child.render(shader, camera);
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
	
	public final Component addComponent(Component component)
	{
		components.add(component);
		component.setParent(this);

		return component;
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
	
	private final void removeChildAndDestroy(GameObject child)
	{
		if(children.contains(child) && !childrenToDestroy.contains(child))
		{
			for(Component component : child.components)
			{
				component.onRemovedFromScene();
			}
			
			child.transform.setParent(null);
			childrenToDestroy.add(child);
		}
	}
	
	private final void updateInternal()
	{
		transform.update();
		
		for(GameObject child : childrenToDestroy)
		{
			children.remove(child);
		}
			
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
		childrenToDestroy.clear();
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
