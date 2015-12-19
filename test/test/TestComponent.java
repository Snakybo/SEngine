package test;

import com.snakybo.sengine.core.object.Component;

/**
 * @author Kevin
 *
 */
public class TestComponent extends Component
{
	@Override
	protected void update(float delta)
	{
		getParent().destroy();
	}
}
