package sandbox3.bblaster;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import sandbox3.bblaster.misc.Cooldown;
import sandbox3.bblaster.player.CtBlastersFx;
import sandbox3.bblaster.player.CtEmissionFx;
import sandbox3.bblaster.player.CtShipBlasterFx;
import sandbox3.bblaster.player.CtShipBlasterProjectiles;
import sandbox3.bblaster.player.CtShipEmissions;
import sandbox3.bblaster.player.CtShipEngines;
import sandbox3.bblaster.player.CtShipMissiles;
import sandbox3.bblaster.player.CtShipPitch;
import sandbox3.bblaster.player.CtShipRoll;
import sandbox3.bblaster.player.CtShipYaw;
import sandbox3.bblaster.player.CtTransformCopy;
import sandbox3.bblaster.player.NdSpeederD;
import sandbox3.bblaster.player.PeShipBlaster;
import sandbox3.bblaster.player.PeShipEmission;

public final class StPlayer extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StPlayer.class);

	private final Node player = new Node("player");

	private final Node fx = new Node("player-fx");

	// TODO move to CtShipWeapons
	private final Cooldown cooldownBlasters = new Cooldown(1f / 8.33f);
	private final Cooldown cooldownMissiles = new Cooldown(0.33f);

	private Node ship;

	private Node emissionFx;
	private Node blastersFx;

	public StPlayer(Node rootNode) {
		rootNode.attachChild(player);
		rootNode.attachChild(fx);
	}

	@Override
	protected void initialize(Application app) {
		ship = new NdSpeederD(app.getAssetManager());
		player.attachChild(ship);

		// BoundsVisualizer boundsVisualizer = new BoundsVisualizer(app.getAssetManager());
		// player.getParent().addControl(boundsVisualizer);
		// boundsVisualizer.setSubject(ship);
		// boundsVisualizer.setEnabled(true);

		ship.addControl(new CtShipYaw());
		ship.addControl(new CtShipPitch());
		ship.addControl(new CtShipRoll());

		ship.addControl(new CtShipEngines(Settings.playerMaxSpeed));

		emissionFx = new Node("emission-fx");
		List<ParticleEmitter> emissions = new ArrayList<>();
		for (Vector3f offset : ship.getControl(CtShipEmissions.class).offsets()) {
			ParticleEmitter emission = new PeShipEmission(app.getAssetManager());
			emission.setLocalTranslation(offset);
			emissionFx.attachChild(emission);
			emissions.add(emission);
		}
		fx.attachChild(emissionFx);
		emissionFx.addControl(new CtTransformCopy(ship));
		emissionFx.addControl(new CtEmissionFx(emissions));

		blastersFx = new Node("balsters-fx");
		List<ParticleEmitter> blasters = new ArrayList<>();
		for (Transform t : ship.getControl(CtShipBlasterFx.class).transforms()) {
			ParticleEmitter blaster = new PeShipBlaster(app.getAssetManager());
			blaster.setLocalTranslation(t.getTranslation());
			blastersFx.attachChild(blaster);
			blasters.add(blaster);
		}
		fx.attachChild(blastersFx);
		blastersFx.addControl(new CtTransformCopy(ship));
		blastersFx.addControl(new CtBlastersFx(blasters));

		// player.addControl(new CtCollision(other -> {
		// }));
		// getState(StCollision.class).register(player);

		getState(StCamera.class).enableFlightCamera(ship);

		// ship.lookAt(new Vector3f(3688.6997f, -1715.2268f, 478.2326f), Vector3f.UNIT_Y);
		ship.lookAt(new Vector3f(-5000.0f, -2462.05f, -212.08636f), Vector3f.UNIT_Y);

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

	void fireBlasters() {
		if (!cooldownBlasters.isReady())
			return;

		cooldownBlasters.reset();

		List<Transform> transforms = ship.getControl(CtShipBlasterProjectiles.class).transforms();
		getState(StBlasters.class).spawnProjectiles(transforms);
		blastersFx.getControl(CtBlastersFx.class).emit();
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
		float thrust = ship.getControl(CtShipEngines.class).thrust(value, tpf);
		emissionFx.getControl(CtEmissionFx.class).updateThrust(thrust);
	}

	public double thrustValue() {
		return ship.getControl(CtShipEngines.class).thrust();
	}

	public Vector3f position() {
		return ship.getLocalTranslation();
	}

	public Vector3f direction() {
		return ship.getLocalRotation().mult(Vector3f.UNIT_Z);
	}

	public Quaternion rotataion() {
		return ship.getLocalRotation().clone();
	}

}
