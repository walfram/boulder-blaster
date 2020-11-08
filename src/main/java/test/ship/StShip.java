package test.ship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;

import test.cmn.ScenePick;

final class StShip extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StShip.class);

	private static final FunctionId F_CLICK = new FunctionId("click");

	private final Node scene = new Node("scene");

	private Node ship;

	public StShip(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		ship = new NdShip(app.getAssetManager());
		scene.attachChild(ship);

		// BoundsVisualizer boundsVisualizer = new BoundsVisualizer(app.getAssetManager());
		// scene.addControl(boundsVisualizer);
		// boundsVisualizer.setSubject(ship);
		// boundsVisualizer.setEnabled(true);

		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();
		inputMapper.map(F_CLICK, Button.MOUSE_BUTTON2);
		inputMapper.addStateListener(new ScenePick(scene, app), F_CLICK);
	}

	void toggleEngines() {
		logger.debug("toggleEngines called");
		ship.getControl(CtShipEngines.class).toggleEnabled();
	}

	void fireGuns() {
		logger.debug("fireGuns called");
		ship.getControl(CtShipWeapons.class).toggleEnabled();
	}

	void fireMissile() {
		logger.debug("fireMissile called");
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
