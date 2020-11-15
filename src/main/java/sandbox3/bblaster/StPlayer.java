package sandbox3.bblaster;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import jme3utilities.debug.BoundsVisualizer;
import sandbox3.bblaster.misc.Cooldown;
import sandbox3.bblaster.ships.CtShipPitch;
import sandbox3.bblaster.ships.CtShipRoll;
import sandbox3.bblaster.ships.CtShipBlasters;
import sandbox3.bblaster.ships.CtShipMissiles;
import sandbox3.bblaster.ships.CtShipEngines;
import sandbox3.bblaster.ships.CtShipYaw;
import sandbox3.bblaster.ships.NdSpeederD;

public final class StPlayer extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StPlayer.class);

	private final Node player = new Node("player");

	// TODO move to CtShipWeapons
	private final Cooldown cooldownBlasters = new Cooldown(1f / 8.33f);
	private final Cooldown cooldownMissiles = new Cooldown(0.33f);

	private Node ship;

	public StPlayer(Node rootNode) {
		rootNode.attachChild(player);
	}

	@Override
	protected void initialize(Application app) {
		ship = new NdSpeederD(app.getAssetManager());
		player.attachChild(ship);

		BoundsVisualizer boundsVisualizer = new BoundsVisualizer(app.getAssetManager());
		player.getParent().addControl(boundsVisualizer);
		boundsVisualizer.setSubject(ship);
		boundsVisualizer.setEnabled(true);

		ship.addControl(new CtShipYaw());
		ship.addControl(new CtShipPitch());
		ship.addControl(new CtShipRoll());

		ship.addControl(new CtShipEngines(Settings.playerMaxSpeed));

		// player.addControl(new CtCollision(other -> {
		// }));

		// getState(StCollision.class).register(player);

		getState(StCamera.class).enableFlightCamera(ship);

		logger.debug("initialized");
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

	@Override
	public void update(float tpf) {
		super.update(tpf);

		cooldownBlasters.update(tpf);
		cooldownMissiles.update(tpf);
	}

	void fireMissile() {
		if (!cooldownMissiles.isReady())
			return;

		cooldownMissiles.reset();

		List<Transform> transforms = ship.getControl(CtShipMissiles.class).transforms();
		getState(StMissiles.class).spawnMissiles(transforms);
	}

	void fireGuns() {
		if (!cooldownBlasters.isReady())
			return;

		cooldownBlasters.reset();

		List<Transform> transforms = ship.getControl(CtShipBlasters.class).transforms();

		getState(StBlasters.class).spawnProjectiles(transforms);
	}

	void yaw(double value, double tpf) {
		ship.getControl(CtShipYaw.class).yaw(value, tpf);
	}

	void pitch(double value, double tpf) {
		ship.getControl(CtShipPitch.class).pitch(value, tpf);
	}

	void roll(double value, double tpf) {
		ship.getControl(CtShipRoll.class).roll(value, tpf);
	}

	void updateThrust(double value, double tpf) {
		ship.getControl(CtShipEngines.class).thrust(value, tpf);
	}

	public double thrustValue() {
		return ship.getControl(CtShipEngines.class).value();
	}

	public Vector3f position() {
		return ship.getLocalTranslation();
	}

	public Vector3f direction() {
		return ship.getLocalRotation().mult(Vector3f.UNIT_Z);
	}

}
