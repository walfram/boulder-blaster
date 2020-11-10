package test.ship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;
import jme3utilities.math.MyVector3f;

final class CtMissileGuidance extends SimpleControl {

	private static final Logger logger = LoggerFactory.getLogger(CtMissileGuidance.class);

	private final Spatial target;

	private static final float MAX_YAW = FastMath.DEG_TO_RAD * 60f;
	private static final float MAX_PITCH = FastMath.DEG_TO_RAD * 30f;

	public CtMissileGuidance(Spatial target) {
		this.target = target;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		if (target == null || target.getParent() == null) {
			logger.debug("no target or target lost");
			// setEnabled(false);
			spatial.removeControl(this);
			return;
		}

		// if (target.getWorldBound().intersects(spatial.getWorldBound())) {
		if (target.getWorldBound().intersects(spatial.getWorldTranslation())) {
			logger.debug("hit! = {}", target);
			// spatial.removeControl(this);
			spatial.removeFromParent();
			target.removeFromParent();
			return;
		}

		Quaternion from = spatial.getLocalRotation().clone();
		Quaternion to = from.clone().lookAt(
				target.getWorldTranslation().subtract(spatial.getWorldTranslation()),
				Vector3f.UNIT_Y);

		float[] aFrom = from.toAngles(null);
		float[] aTo = to.toAngles(null);

		float deltaPitch = aTo[0] - aFrom[0];
		float deltaYaw = aTo[1] - aFrom[1];

		Quaternion pitch = new Quaternion();
		Quaternion yaw = new Quaternion();

		float maxPitch = MAX_PITCH * updateInterval;
		if (Math.abs(deltaPitch * updateInterval) > maxPitch) {
			pitch.fromAngleAxis(Math.signum(deltaPitch) * maxPitch, Vector3f.UNIT_X);
		} else {
			pitch.fromAngleAxis(deltaPitch * updateInterval, Vector3f.UNIT_X);
		}

		float maxYaw = MAX_YAW * updateInterval;
		if (Math.abs(deltaYaw * updateInterval) > maxYaw) {
			yaw.fromAngleAxis(Math.signum(deltaYaw) * maxYaw, Vector3f.UNIT_Y);
		} else {
			yaw.fromAngleAxis(deltaYaw * updateInterval, Vector3f.UNIT_Y);
		}

		// pitch.multLocal(updateInterval);
		// yaw.multLocal(updateInterval);

		Quaternion q = yaw.mult(pitch).mult(from);
		spatial.setLocalRotation(q);
		// spatial.rotate(yaw.mult(pitch));

		// Vector3f offset = target.getLocalTranslation().subtract(spatial.getLocalTranslation());
		// float targetAzm = MyVector3f.azimuth(offset);
		// float targetAlt = MyVector3f.altitude(offset);
		//
		// float missileAzm = MyVector3f.azimuth(spatial.getLocalRotation().mult(Vector3f.UNIT_Z));
		// float missileAlt = MyVector3f.altitude(spatial.getLocalRotation().mult(Vector3f.UNIT_Z));
		//
		// float signAzm = Math.signum(targetAzm - missileAzm);
		// float signAlt = Math.signum(targetAlt - missileAlt);
		// float azimuth = missileAzm + (signAzm * MAX_AZIMUTH * updateInterval);
		// float altitude = missileAlt + (signAlt * MAX_ALTITUDE * updateInterval);
		// Vector3f direction = MyVector3f.fromAltAz(altitude, azimuth);
		//
		// // Quaternion q = new Quaternion().lookAt(direction, Vector3f.UNIT_Y);
		// Quaternion q = spatial.getLocalRotation().lookAt(direction, Vector3f.UNIT_Y);
		//
		// spatial.setLocalRotation(q);

	}

}
