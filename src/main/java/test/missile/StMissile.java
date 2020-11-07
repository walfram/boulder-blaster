package test.missile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;

final class StMissile extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StMissile.class);

	private final Node scene = new Node("scene");

	public StMissile(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Node missile = new NdMissile(app.getAssetManager());
		scene.attachChild(missile);
		logger.debug("missile bound = {}", missile.getWorldBound());
		// missile.scale(2f);

		// missile.addControl(new SimpleControl() {
		// @Override
		// protected void controlUpdate(float updateInterval) {
		// super.controlUpdate(updateInterval);
		// spatial.rotate(0, 0, FastMath.DEG_TO_RAD * 30f * updateInterval);
		// }
		// });
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
