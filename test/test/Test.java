package test;

import com.snakybo.sengine.components.Camera;
import com.snakybo.sengine.components.FreeLook;
import com.snakybo.sengine.components.FreeMove;
import com.snakybo.sengine.core.SEngine;
import com.snakybo.sengine.lighting.AmbientLight;
import com.snakybo.sengine.lighting.DirectionalLight;
import com.snakybo.sengine.lighting.PointLight;
import com.snakybo.sengine.lighting.SpotLight;
import com.snakybo.sengine.lighting.utils.Attenuation;
import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Quaternion;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.object.GameObject;
import com.snakybo.sengine.object.prefab.Cube;
import com.snakybo.sengine.object.prefab.Plane;
import com.snakybo.sengine.object.prefab.Sphere;
import com.snakybo.sengine.resource.material.Material;
import com.snakybo.sengine.resource.texture.Texture;
import com.snakybo.sengine.skybox.Skybox;
import com.snakybo.sengine.utils.Color;
import com.snakybo.sengine.window.Window;

public class Test extends SEngine
{
	@Override
	protected void initialize()
	{
		Window.setSamples(4);
		Window.createWindowed("Test", 1280, 720);
		
		AmbientLight.setAmbientColor(new Color(0.2f, 0.2f, 0.2f));
		AmbientLight.setSkybox(new Skybox("skybox/sp3front.jpg", "skybox/sp3back.jpg", "skybox/sp3left.jpg", "skybox/sp3right.jpg", "skybox/sp3top.jpg", "skybox/sp3bot.jpg"));
	}
	
	@Override
	public void onCreate()
	{
		Material brickMaterial = Material.createDefault(new Texture("bricks.png"), 0.5f, 4, new Texture("bricks_normal.png"), new Texture("bricks_disp.png"), 0.03f, -0.5f);
		Material brick2Material = Material.createDefault(new Texture("bricks2.jpg"), 1, 8, new Texture("bricks2_normal.png"), new Texture("bricks2_disp.jpg"), 0.04f, -1f);
		
		GameObject directionalLight = new GameObject(new Vector3f(), new Quaternion(new Vector3f(1, 0, 0), Math.toRadians(-45)));
		directionalLight.addComponent(new DirectionalLight(new Color(1f, 1f, 1f), 0.4f, 10));
		
		GameObject spotLight = new GameObject(new Vector3f(-4, 1, 2));
		spotLight.getTransform().rotate(new Vector3f(0, 1, 0), Math.toRadians(90));
		spotLight.getTransform().rotate(new Vector3f(1, 0, 0), Math.toRadians(-60));
		spotLight.addComponent(new SpotLight(new Color(0, 1, 1), 0.8f, new Attenuation(0, 0, 0.02f), (float)Math.toRadians(91.1f), 7, 1, 0.5f));
		
		GameObject pointLight = new GameObject(new Vector3f(12, 1, 12));
		pointLight.addComponent(new PointLight(new Color(1, 1, 0), 0.2f, new Attenuation(0, 0, 0.05f)));
		
		Matrix4f projection = Matrix4f.perspective(90, (float)Window.getWidth() / (float)Window.getHeight(), 0.01f, 1000);
		GameObject camera = new GameObject();
		Camera cam = camera.addComponent(new Camera(projection));
		camera.addComponent(new FreeLook());
		camera.addComponent(new FreeMove());
		Camera.setMainCamera(cam);
		
		new Plane(new Vector3f(0, -1, 0), new Quaternion(), new Vector3f(20), brickMaterial);
		new Plane(new Vector3f(-8, 2, 8), new Quaternion(new Vector3f(0, 1, 0), Math.toRadians(45f)), new Vector3f(5), brick2Material);
		new Sphere(new Vector3f(-2, 0, -2), new Quaternion());
		new Cube(new Vector3f(2, 0, 2), new Quaternion(new Vector3f(0, 1, 0), Math.toRadians(30f)));
	}
	
	public static void main(String[] args)
	{
		new Test();
	}
}
