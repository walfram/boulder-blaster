package test.ship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import sandbox3.bblaster.missiles.CtMissileEngine;
import sandbox3.bblaster.missiles.CtMissileGuidance;
import sandbox3.bblaster.missiles.CtMissileTrail;
import sandbox3.bblaster.missiles.NdMissile;
import sandbox3.bblaster.ships.CtShipBlasters;
import sandbox3.bblaster.ships.CtShipEmissions;
import sandbox3.bblaster.ships.CtShipEngine;
import sandbox3.bblaster.ships.NdSpeederD;

final class StShip extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StShip.class);

	private final Node scene = new Node("scene");

	private Node ship;

	private ChaseCamera chaseCamera;

	private Spatial target;

	public StShip(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		ship = new NdSpeederD(app.getAssetManager());
		scene.attachChild(ship);

		ship.setLocalTranslation(5, 10, 15);
		// ship.lookAt(new Vector3f(50, 100, 150f), Vector3f.UNIT_Y);

		// BoundsVisualizer boundsVisualizer = new BoundsVisualizer(app.getAssetManager());
		// scene.addControl(boundsVisualizer);
		// boundsVisualizer.setSubject(ship);
		// boundsVisualizer.setEnabled(true);

		chaseCamera = new ChaseCamera(app.getCamera(), app.getInputManager());
		chaseCamera.setInvertVerticalAxis(true);
		chaseCamera.setUpVector(Vector3f.UNIT_Y);
		chaseCamera.setDefaultDistance(100f);
		chaseCamera.setMaxDistance(250);
		chaseCamera.setMinVerticalRotation(-FastMath.HALF_PI);
		// ship.addControl(chaseCamera);
	}

	void toggleEngines() {
		logger.debug("toggleEngines called");
		ship.getControl(CtShipEmissions.class).toggleEnabled();

		ship.getControl(CtShipEngine.class).setEnabled(ship.getControl(CtShipEmissions.class).isEnabled());
	}

	void fireGuns() {
		logger.debug("fireGuns called");
		ship.getControl(CtShipBlasters.class).toggleEnabled();
	}

	void fireMissile() {
		logger.debug("fireMissile called");
		// ship.getControl(CtShipMissiles.class).
		Node missile = new NdMissile(getApplication().getAssetManager());

		Vector3f translation = ship.getLocalTranslation().add(new Vector3f(0, -1, 0));
		missile.setLocalTranslation(translation);
		missile.setLocalRotation(ship.getLocalRotation());

		scene.attachChild(missile);

		missile.addControl(new CtMissileGuidance(target));

		// TODO use one method for engine start and launch, or delay engine start
		missile.getControl(CtMissileTrail.class).setEnabled(true);
		missile.getControl(CtMissileEngine.class).setEnabled(true);

		// missile.addControl(chaseCamera);
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

	public void selectTarget(Spatial target) {
		logger.debug("targetting = {}", target);
		this.target = target;
	}

}
