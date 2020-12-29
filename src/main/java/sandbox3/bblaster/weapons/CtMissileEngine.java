package sandbox3.bblaster.weapons;

import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

public final class CtMissileEngine extends SimpleControl {

	private final float speed;

	public CtMissileEngine(float speed) {
		this.speed = speed;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		// Quaternion yaw = new Quaternion().fromAngleAxis(-FastMath.DEG_TO_RAD * 30f * updateInterval, Vector3f.UNIT_Y);
		// Quaternion pitch = new Quaternion().fromAngleAxis(-FastMath.DEG_TO_RAD * 45f * updateInterval, Vector3f.UNIT_X);
		// Quaternion q = yaw.multLocal(pitch).mult(spatial.getLocalRotation());
		// spatial.setLocalRotation(q);

		Vector3f forward = spatial.getLocalRotation().mult(Vector3f.UNIT_Z);
		spatial.move(forward.mult(updateInterval).mult(speed));
	}

}
