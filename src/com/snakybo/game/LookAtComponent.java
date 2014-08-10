package com.snakybo.game;

import com.snakybo.sengine.core.object.Component;
import com.snakybo.sengine.rendering.RenderingEngine;
import com.snakybo.sengine.utils.math.Quaternion;
import com.snakybo.sengine.utils.math.Vector3f;

public class LookAtComponent extends Component {
	@Override
	public void update(double delta) {
		Quaternion newRotation =
				getTransform().getLookAtRotation(
						RenderingEngine.getMainCamera().getTransform().getPosition(), new Vector3f(0, 1, 0));
		
		getTransform().setRotation(getTransform().getLocalRotation().nlerp(newRotation, (float)delta * 5.0f, true));
	}
}
