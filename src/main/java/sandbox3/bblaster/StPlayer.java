package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

import common.misc.Cooldown;
import common.mtl.MtlUnshaded;
import jme3utilities.mesh.Icosphere;

final class StPlayer extends BaseAppState {

	private final Node player = new Node("player");
	private Geometry hull;
	private Geometry wpnLeft;
	private Geometry wpnRight;

	private final Cooldown cooldownGuns = new Cooldown(60f / 600f);
	private float thrust = 0f;

	public StPlayer(Node rootNode) {
		rootNode.attachChild(player);
	}

	@Override
	protected void initialize(Application app) {
		getState(FlyCamAppState.class).setEnabled(!isEnabled());

		Icosphere mesh = new Icosphere(0, 5f);
		mesh.setBound(new BoundingSphere(5f, new Vector3f()));
		hull = new Geometry("player", mesh);
		hull.rotate(FastMath.DEG_TO_RAD * 60f, 0, 0);

		Material mtlHull = new MtlUnshaded(app.getAssetManager(), ColorRGBA.LightGray);
		mtlHull.getAdditionalRenderState().setLineWidth(5f);
		mtlHull.getAdditionalRenderState().setWireframe(true);
		mtlHull.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);

		hull.setMaterial(mtlHull);
		player.attachChild(hull);

		wpnLeft = new Geometry("weapon-left", new Box(0.5f, 0.5f, 0.5f));
		wpnLeft.move(5f, 0, 0);
		wpnLeft.setMaterial(new MtlUnshaded(app.getAssetManager(), ColorRGBA.Red));
		player.attachChild(wpnLeft);

		wpnRight = new Geometry("weapon-right", new Box(0.5f, 0.5f, 0.5f));
		wpnRight.move(-5, 0, 0);
		wpnRight.setMaterial(new MtlUnshaded(app.getAssetManager(), ColorRGBA.Green));
		player.attachChild(wpnRight);

		// player.addControl(new SimpleControl() {
		// @Override
		// protected void controlUpdate(float updateInterval) {
		// super.controlUpdate(updateInterval);
		//
		// Vector3f velocity = spatial.getLocalRotation().mult(Vector3f.UNIT_Z).mult(updateInterval).mult(thrust).mult(new Const().playerMaxSpeed());
		// Vector3f moved = spatial.getLocalTranslation().add(velocity);
		//
		// spatial.setLocalTranslation(moved);
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

	@Override
	public void update(float tpf) {
		super.update(tpf);

		// player.setLocalTranslation(getApplication().getCamera().getLocation());
		// player.setLocalRotation(getApplication().getCamera().getRotation());

		cooldownGuns.update(tpf);

		Vector3f velocity = player.getLocalRotation().mult(Vector3f.UNIT_Z).mult(tpf).mult(thrust).mult(new Const().playerMaxSpeed());
		Vector3f moved = player.getLocalTranslation().add(velocity);

		player.setLocalTranslation(moved);

		getApplication().getCamera().setLocation(player.getLocalTranslation().clone());
		getApplication().getCamera().setRotation(player.getLocalRotation().clone());
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
