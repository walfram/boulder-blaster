package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import sandbox3.bblaster.controls.CtCollision;
import sandbox3.bblaster.controls.CtPitch;
import sandbox3.bblaster.controls.CtRoll;
import sandbox3.bblaster.controls.CtThrust;
import sandbox3.bblaster.controls.CtYaw;
import sandbox3.bblaster.materials.MtlUnshaded;
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
		Spatial hull = app.getAssetManager().loadModel("models/spacekit/spaceCraft1.obj");
		hull.rotate(0, FastMath.PI, 0);

		player.attachChild(hull);

		wpnLeft = new Geometry("weapon-left", new Box(0.5f, 0.5f, 0.5f));
		wpnLeft.move(5f, 0, 0);
		wpnLeft.setMaterial(new MtlUnshaded(app.getAssetManager(), ColorRGBA.Red));
		player.attachChild(wpnLeft);

		wpnRight = new Geometry("weapon-right", new Box(0.5f, 0.5f, 0.5f));
		wpnRight.move(-5, 0, 0);
		wpnRight.setMaterial(new MtlUnshaded(app.getAssetManager(), ColorRGBA.Green));
		player.attachChild(wpnRight);

		player.addControl(new CtYaw());
		player.addControl(new CtPitch());
		player.addControl(new CtRoll());
		player.addControl(new CtThrust(new GameSettings().playerMaxSpeed()));

		// player.addControl(getState(StCamera.class).flightCamera());
		// player.addControl(getState(StCamera.class).dockedCamera());

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
