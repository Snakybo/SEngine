package test;

import com.snakybo.sengine.components.Camera.CameraComponent;
import com.snakybo.sengine.components.FreeLook;
import com.snakybo.sengine.components.FreeMove;
import com.snakybo.sengine.components.MeshRenderer;
import com.snakybo.sengine.components.lighting.DirectionalLight;
import com.snakybo.sengine.core.Game;
import com.snakybo.sengine.core.SEngine;
import com.snakybo.sengine.core.object.GameObject;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Window;
import com.snakybo.sengine.resource.Material;
import com.snakybo.sengine.resource.Primitive;
import com.snakybo.sengine.resource.Texture;
import com.snakybo.sengine.utils.Color;
import com.snakybo.sengine.utils.math.Matrix4f;
import com.snakybo.sengine.utils.math.Quaternion;
import com.snakybo.sengine.utils.math.Vector3f;

public class Test extends Game
{
	@Override
	public void init()
	{
		Matrix4f cameraProjection = new Matrix4f().initPerspective(90, (float)Window.getWidth() / (float)Window.getHeight(), 0.01f, 1000);
		GameObject camera = new GameObject();
		camera.addComponent(new FreeLook(Window.getCenter()));
		camera.addComponent(new FreeMove());
		camera.addComponent(new CameraComponent(cameraProjection, new Color(0, 0, 0)).setAsMainCamera());
		addChild(camera);
		
		DirectionalLight directionalLight = new DirectionalLight(new Color(1f, 1f, 1f), 0.1f);
		GameObject directionalLightObject = new GameObject().addComponent(directionalLight);
		directionalLightObject.getTransform().setRotation(new Quaternion(new Vector3f(2, 1, 0), (float) Math.toRadians(-65)));
		addChild(directionalLightObject);
		
		Material brickMaterial = new Material("bricks", new Texture("bricks.png"), 0.5f, 4, new Texture("bricks_normal.png"), new Texture("bricks_disp.png"), 0.03f, -0.5f);		
		addChild(new GameObject(new Vector3f(0, -1, 0), new Quaternion(), 10).addComponent(new MeshRenderer(Primitive.PLANE, brickMaterial)));
		
		Material cubeMaterial = new Material("box", new Texture("internal/default_diffuse.png"), 4, 8);
		MeshRenderer cubeMesh = new MeshRenderer(Primitive.SPHERE, cubeMaterial);
		GameObject cubeObject = new GameObject(new Vector3f(-2, 0, -2), new Quaternion());
		cubeObject.addComponent(cubeMesh);
		addChild(cubeObject);
		
		Material cubeMaterial2 = new Material("box", new Texture("internal/default_diffuse.png"), 4, 8);
		MeshRenderer cubeMesh2 = new MeshRenderer(Primitive.CUBE, cubeMaterial2);
		GameObject cubeObject2 = new GameObject(new Vector3f(2, 0, 2), new Quaternion());
		cubeObject2.addComponent(cubeMesh2);
		addChild(cubeObject2);
	};
	
	public static void main(String[] args)
	{
		Window.create(1280, 720, "Test");
		RenderingEngine.setAmbientColor(new Color(0.3f, 0.3f, 0.3f));
		
		SEngine engine = new SEngine(new Test());
		engine.start(60);
	}
}
