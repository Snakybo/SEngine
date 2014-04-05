package com.snakybo.game;

import com.snakybo.sengine.components.Component;
import com.snakybo.sengine.core.Quaternion;
import com.snakybo.sengine.core.Vector3f;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Shader;

public class LookAtComponent extends Component {
	RenderingEngine renderingEngine;
	
	@Override
	public void update(float delta) {
		if(renderingEngine != null) {
			Quaternion newRot =
					getTransform().getLookAtRotation(renderingEngine.getMainCamera().getTransform().getPosition(),
							new Vector3f(0, 1, 0));
			
			getTransform().setRotation(getTransform().getLocalRotation().nlerp(newRot, delta * 5.0f, true));
		}
	}
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine) {
		this.renderingEngine = renderingEngine;
	}
}
