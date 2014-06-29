package com.snakybo.game;

import com.snakybo.sengine.core.Component;
import com.snakybo.sengine.core.utils.Quaternion;
import com.snakybo.sengine.core.utils.Vector3f;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.rendering.Shader;

public class LookAtComponent extends Component {
	RenderingEngine renderingEngine;
	
	@Override
	public void update(float delta) {
		if(renderingEngine != null) {
			Quaternion newRotation =
				getTransform().getLookAtRotation(
					renderingEngine.getMainCamera().getTransform().getTransformedPosition(), new Vector3f(0, 1, 0)
				);
			
			getTransform().setRotation(getTransform().getRotation().nlerp(newRotation, delta * 5.0f, true));
		}
	}
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine) {
		this.renderingEngine = renderingEngine;
	}
}
