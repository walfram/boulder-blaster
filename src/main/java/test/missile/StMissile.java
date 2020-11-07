package test.missile;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;

final class StMissile extends BaseAppState {

	private final Node scene = new Node("scene");
	
	public StMissile(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Node missile = new NdMissile(app.getAssetManager());
		scene.attachChild(missile);
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
