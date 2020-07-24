package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import common.misc.Cooldown;
import common.mtl.MtlUnshaded;

final class StPlayer extends BaseAppState {

	private final Node player = new Node("player");

	private Geometry wpnLeft;
	private Geometry wpnRight;

	private final Cooldown cooldownGuns = new Cooldown(60f / 600f);
	private float thrust = 0f;

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

		app.getCamera().setLocation(player.getLocalTranslation().add(cameraOffset));
		app.getCamera().setRotation(new Quaternion());
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

		Vector3f velocity = player.getLocalRotation().mult(Vector3f.UNIT_Z).mult(tpf).mult(thrust).mult(new Const().playerMaxSpeed());
		Vector3f moved = player.getLocalTranslation().add(velocity);
		player.setLocalTranslation(moved);

		Vector3f playerLocation = player.getLocalTranslation().clone();
		Quaternion playerRotation = player.getLocalRotation().clone();
		Vector3f offsetRotated = playerRotation.mult(cameraOffset);

		Vector3f interpolated = getApplication().getCamera().getLocation().clone().interpolateLocal(playerLocation.add(offsetRotated), 5f * tpf);
		getApplication().getCamera().setLocation(interpolated);

		Quaternion slerped = getApplication().getCamera().getRotation().clone();
		slerped.slerp(playerRotation, 5.0f * tpf);

		getApplication().getCamera().setRotation(slerped);
	}

	void fireMissile() {
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
		Vector3f up = player.getLocalRotation().mult(Vector3f.UNIT_Y);

		Quaternion yaw = new Quaternion().fromAngleAxis((float) (-value * tpf), up);
		Quaternion yawed = yaw.mult(player.getLocalRotation());

		player.setLocalRotation(yawed);
	}

	void pitch(double value, double tpf) {
		Vector3f left = player.getLocalRotation().mult(Vector3f.UNIT_X);

		Quaternion pitch = new Quaternion().fromAngleAxis((float) (-value * tpf), left);
		Quaternion pitched = pitch.mult(player.getLocalRotation());

		player.setLocalRotation(pitched);
	}

	void roll(double value, double tpf) {
		Vector3f forward = player.getLocalRotation().mult(Vector3f.UNIT_Z);

		Quaternion roll = new Quaternion().fromAngleAxis((float) (value * tpf), forward);
		Quaternion rolled = roll.mult(player.getLocalRotation());

		player.setLocalRotation(rolled);
	}

	void updateThrust(double tpf) {
		thrust += FastMath.sign((float) tpf) * 0.0125f;

		thrust = Math.min(thrust, 1.0f);
		thrust = Math.max(thrust, 0f);
	}

	double thrustValue() {
		return thrust;
	}

	public Vector3f translation() {
		return player.getLocalTranslation();
	}

}
