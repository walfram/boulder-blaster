package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import jme3utilities.debug.PointVisualizer;
import sandbox3.bblaster.controls.CtCollision;
import sandbox3.bblaster.controls.CtPitch;
import sandbox3.bblaster.controls.CtRoll;
import sandbox3.bblaster.controls.CtThrust;
import sandbox3.bblaster.controls.CtYaw;
import sandbox3.bblaster.misc.Cooldown;

public final class StPlayer extends BaseAppState {

	private final Node player = new Node("player");

	private Geometry wpnLeft;
	private Geometry wpnRight;

	private final Cooldown cooldownGuns = new Cooldown(60f / 600f);
	private final Cooldown cooldownMissiles = new Cooldown(0.33f);

	public StPlayer(Node rootNode) {
		rootNode.attachChild(player);
	}

	@Override
	protected void initialize(Application app) {
		Spatial hull = app.getAssetManager().loadModel("models/spacekit2/craft_speederD.obj");
		hull.scale(10f);

		player.attachChild(hull);

		wpnLeft = new PointVisualizer(app.getAssetManager(), 10, ColorRGBA.Red, "saltire");
		wpnLeft.setLocalTranslation(14f, 2f, -1.5f);
		player.attachChild(wpnLeft);

		wpnRight = new PointVisualizer(app.getAssetManager(), 10, ColorRGBA.Green, "saltire");
		wpnRight.setLocalTranslation(-14f, 2f, -1.5f);
		player.attachChild(wpnRight);

		player.addControl(new CtYaw());
		player.addControl(new CtPitch());
		player.addControl(new CtRoll());
		player.addControl(new CtThrust(new GameSettings().playerMaxSpeed()));

		player.addControl(new CtCollision(other -> {
		}));

		getState(StCollision.class).register(player);

		getState(StCamera.class).enableFlightCamera(player);
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

		cooldownGuns.update(tpf);
		cooldownMissiles.update(tpf);
	}

	void fireMissile() {
		if (!cooldownMissiles.isReady())
			return;

		cooldownMissiles.reset();

		Transform transform = wpnLeft.getWorldTransform();
		getState(StMissiles.class).spawnMissile(transform);
	}

	void fireGuns() {
		if (!cooldownGuns.isReady())
			return;

		cooldownGuns.reset();

		Transform transform = wpnRight.getWorldTransform();
		getState(StGuns.class).spawnProjectile(transform);
	}

	void yaw(double value, double tpf) {
		player.getControl(CtYaw.class).yaw(value, tpf);
	}

	void pitch(double value, double tpf) {
		player.getControl(CtPitch.class).pitch(value, tpf);
	}

	void roll(double value, double tpf) {
		player.getControl(CtRoll.class).roll(value, tpf);
	}

	void updateThrust(double value, double tpf) {
		player.getControl(CtThrust.class).thrust(value, tpf);
	}

	public double thrustValue() {
		return player.getControl(CtThrust.class).value();
	}

	public Vector3f position() {
		return player.getLocalTranslation();
	}

}
