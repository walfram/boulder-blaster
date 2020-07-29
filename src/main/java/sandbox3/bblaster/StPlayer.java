package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import common.controls.CtCameraFollow;
import common.controls.CtPitch;
import common.controls.CtRoll;
import common.controls.CtThrust;
import common.controls.CtYaw;
import common.misc.Cooldown;
import common.mtl.MtlUnshaded;

final class StPlayer extends BaseAppState {

	private final Node player = new Node("player");

	private Geometry wpnLeft;
	private Geometry wpnRight;

	private final Cooldown cooldownGuns = new Cooldown(60f / 600f);
	private final Cooldown cooldownMissiles = new Cooldown(0.33f);

	private final Vector3f cameraOffset = new Vector3f(0, 15, -50);

	public StPlayer(Node rootNode) {
		rootNode.attachChild(player);
	}

	@Override
	protected void initialize(Application app) {
		getState(FlyCamAppState.class).setEnabled(!isEnabled());

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
		player.addControl(new CtThrust(new Const().playerMaxSpeed()));

		app.getCamera().setLocation(player.getLocalTranslation().add(cameraOffset));
		app.getCamera().lookAt(player.getLocalTranslation(), Vector3f.UNIT_Y);

		player.addControl(new CtCameraFollow(app.getCamera(), cameraOffset, 5.0f, 5.0f));
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

	double thrustValue() {
		return player.getControl(CtThrust.class).value();
	}

	Vector3f translation() {
		return player.getLocalTranslation();
	}

}
