package com.snakybo.sengine.core;

import com.snakybo.sengine.core.utils.Time;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Window;

public class SEngine {
	private RenderingEngine renderingEngine;
	private Window window;
	private Game game;
	
	private double frameTime;
	
	private boolean isRunning;
	
	public SEngine(Game game) {
		this.isRunning = false;
		
		this.game = game;
		
		game.setEngine(this);
	}
	
	public void start(Window window, double frameRate) {
		if(isRunning)
			return;
		
		this.window = window;
		
		frameTime = 1.0 / frameRate;
		renderingEngine = new RenderingEngine();
		
		window.bindAsRenderTarget();		
		run();
	}
	
	public void stop() {
		if(!isRunning)
			return;
		
		isRunning = false;
	}
	
	private void run() {
		isRunning = true;
		
		int frames = 0;
		long frameCounter = 0;
		
		game.init();
		
		double lastTime = Time.getTime();
		double unprocessedTime = 0;
		
		while(isRunning) {
			boolean render = false;
			
			double startTime = Time.getTime();
			double passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocessedTime += passedTime;
			frameCounter += passedTime;
			
			while(unprocessedTime > frameTime) {
				render = true;
				
				unprocessedTime -= frameTime;
				
				if(window.isCloseRequested())
					stop();
				
				game.input((float)frameTime);
				Input.update();
				
				game.update((float)frameTime);
				
				if(frameCounter >= 1.0) {
					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			if(render) {
				game.render(renderingEngine);
				window.render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		destroy();
	}
	
	private void destroy() {
		window.destroy();
	}
	
	public RenderingEngine getRenderingEngine() {
		return renderingEngine;
	}
	
	public Window getWindow() {
		return window;
	}
}
