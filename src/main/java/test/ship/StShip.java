package test.ship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;

import test.cmn.ScenePick;
import test.missile.CtMissileEngine;
import test.missile.NdMissile;

final class StShip extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StShip.class);

	private static final FunctionId F_CLICK = new FunctionId("click");

	private final Node scene = new Node("scene");

	private Node ship;

	private ChaseCamera chaseCamera;

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
		
		chaseCamera = new ChaseCamera(app.getCamera(), app.getInputManager());
		chaseCamera.setInvertVerticalAxis(true);
		chaseCamera.setUpVector(Vector3f.UNIT_Y);
		chaseCamera.setDefaultDistance(50f);
		chaseCamera.setMaxDistance(150);
		chaseCamera.setMinVerticalRotation(-FastMath.HALF_PI);
		ship.addControl(chaseCamera);
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
		// ship.getControl(CtShipMissiles.class).
		Node missile = new NdMissile(getApplication().getAssetManager());
		
		Vector3f translation = ship.getLocalTranslation().add(new Vector3f(0, -1, 0));
		missile.setLocalTranslation(translation);
		missile.setLocalRotation(ship.getLocalRotation());
		
		scene.attachChild(missile);
		
		// TODO use one method for engine start and launch, or delay engine start
		missile.getControl(CtMissileEngine.class).setEnabled(true);
		missile.getControl(CtMissileEngine.class).launch();
		
		missile.addControl(chaseCamera);
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
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
