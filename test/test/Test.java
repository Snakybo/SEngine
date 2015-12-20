package test;

import com.snakybo.sengine.components.Camera.CameraComponent;
import com.snakybo.sengine.components.FreeLook;
import com.snakybo.sengine.components.FreeMove;
import com.snakybo.sengine.components.MeshRenderer;
import com.snakybo.sengine.core.Game;
import com.snakybo.sengine.core.Input;
import com.snakybo.sengine.core.SEngine;
import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.lighting.AmbientLight;
import com.snakybo.sengine.lighting.DirectionalLight;
import com.snakybo.sengine.lighting.PointLight;
import com.snakybo.sengine.lighting.SpotLight;
import com.snakybo.sengine.lighting.utils.Attenuation;
import com.snakybo.sengine.math.Matrix4f;
import com.snakybo.sengine.math.Quaternion;
import com.snakybo.sengine.math.Vector3f;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.glfw.GLFWWindow;
import com.snakybo.sengine.resource.material.Material;
import com.snakybo.sengine.resource.mesh.Primitive;
import com.snakybo.sengine.resource.texture.Texture;
import com.snakybo.sengine.skybox.Skybox;
import com.snakybo.sengine.utils.Color;

public class Test extends Game
{
	@Override
	public void create()
	{
		Matrix4f cameraProjection = Matrix4f.perspective(90, (float)GLFWWindow.getWidth() / (float)GLFWWindow.getHeight(), 0.01f, 1000);
		GameObject camera = new GameObject();
		camera.addComponent(new FreeLook());
		camera.addComponent(new FreeMove());
		camera.addComponent(new CameraComponent(cameraProjection, new Color(0, 0, 0)).setAsMainCamera());
		addChild(camera);
		
		DirectionalLight directionalLight = new DirectionalLight(new Color(1f, 1f, 1f), 0.4f, 10);
		GameObject directionalLightObject = new GameObject().addComponent(directionalLight);
		directionalLightObject.getTransform().setRotation(new Quaternion(new Vector3f(1, 0, 0), Math.toRadians(-45)));
		addChild(directionalLightObject);
		
		SpotLight spotLight = new SpotLight(new Color(0, 1, 1), 0.8f, new Attenuation(0, 0, 0.02f), (float)Math.toRadians(91.1f), 7, 1, 0.5f);
		GameObject spotLightObject = new GameObject().addComponent(spotLight);
		spotLightObject.getTransform().rotate(new Vector3f(0, 1, 0), Math.toRadians(90));
		spotLightObject.getTransform().rotate(new Vector3f(1, 0, 0), Math.toRadians(-60));
		spotLightObject.getTransform().setPosition(new Vector3f(-4, 1, 2));
		addChild(spotLightObject);
		
		PointLight pointLight = new PointLight(new Color(1, 1, 0), 0.2f, new Attenuation(0, 0, 0.05f));
		GameObject pointLightObject = new GameObject().addComponent(pointLight);
		pointLightObject.getTransform().setPosition(new Vector3f(12, 1, 12));
		addChild(pointLightObject);
		
		Material brickMaterial = new Material(new Texture("bricks.png"), 0.5f, 4, new Texture("bricks_normal.png"), new Texture("bricks_disp.png"), 0.03f, -0.5f);		
		addChild(new GameObject(new Vector3f(0, -1, 0), new Quaternion(), new Vector3f(20)).addComponent(new MeshRenderer(Primitive.PLANE, brickMaterial)));
		
		Material brick2Material = new Material(new Texture("bricks2.jpg"), 1, 8, new Texture("bricks2_normal.png"), new Texture("bricks2_disp.jpg"), 0.04f, -1f);
		addChild(new GameObject(new Vector3f(-8, 2, 8), new Quaternion(new Vector3f(0, 1, 0), Math.toRadians(45f)), new Vector3f(5)).addComponent(new MeshRenderer(Primitive.PLANE, brick2Material)));
		
		Material diffuseMaterial = new Material(new Texture("default/diffuse.png"), 4, 8);
		GameObject sphereObject = new GameObject(new Vector3f(-2, 0, -2), new Quaternion());
		sphereObject.addComponent(new MeshRenderer(Primitive.SPHERE, diffuseMaterial));
		addChild(sphereObject);
		
		GameObject cubeObject = new GameObject(new Vector3f(2, 0, 2), new Quaternion(new Vector3f(0, 1, 0), Math.toRadians(30f)));
		cubeObject.addComponent(new MeshRenderer(Primitive.CUBE, diffuseMaterial));
		addChild(cubeObject);
		
		Skybox skyBox = new Skybox("skybox/sp3front.jpg", "skybox/sp3back.jpg", "skybox/sp3left.jpg", "skybox/sp3right.jpg", "skybox/sp3top.jpg", "skybox/sp3bot.jpg");
		RenderingEngine.setSkybox(skyBox);
	}
	
	public static void main(String[] args)
	{
		GLFWWindow.createWindowed("Test", 1280, 720);
		AmbientLight.setAmbientColor(new Color(0.2f, 0.2f, 0.2f));		
		SEngine.start(new Test());
	}
}
