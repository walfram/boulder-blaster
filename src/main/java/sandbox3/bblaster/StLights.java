package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;

public final class StLights extends BaseAppState {

	private final DirectionalLight light = new DirectionalLight();

	public StLights(Node rootNode) {
		rootNode.addLight(light);
	}

	@Override
	protected void initialize(Application app) {
		light.setColor(ColorRGBA.White);
		light.setDirection(app.getCamera().getDirection());
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		light.setDirection(getApplication().getCamera().getDirection());
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

}
