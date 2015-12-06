package com.snakybo.sengine.core;

import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.rendering.RenderingEngine;

/** Base game class, your has to extend this class in order to work with the
 * engine
 * @author Kevin Krol
 * @since Apr 4, 2014 */
public abstract class Game
{
	private GameObject root;

	/** Main initialization method for your game.
	 * <p>
	 * You should start everything here
	 * </p>
	*/
	public void init()
	{
	}

	/** Internal initialization method for the game, used by the engine */
	final void internalInit(RenderingEngine renderingEngine)
	{
		root = new GameObject();
		root.addToRenderingEngine(renderingEngine);
	}

	/** Entry point for input handling
	 * @param delta The delta time */
	final void input(double delta)
	{
		root.inputAll(delta);
	}

	/** Entry point for updating
	 * @param delta The delta time */
	final void update(double delta)
	{
		root.updateAll(delta);
	}

	/** Entry point for rendering */
	final void render(RenderingEngine renderingEngine)
	{
		renderingEngine.render(root);
	}

	/** Add a game object to the scene
	 * @param object The game object to add */
	public final void addChild(GameObject object)
	{
		root.addChild(object);
	}
}
