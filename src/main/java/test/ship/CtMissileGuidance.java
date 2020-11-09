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

	private static final float MAX_AZIMUTH = FastMath.DEG_TO_RAD * 45f;
	private static final float MAX_ALTITUDE = FastMath.DEG_TO_RAD * 45f;

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
		if (target.getWorldBound().intersects(spatial.getLocalTranslation())) {
			logger.debug("hit!");
//			spatial.removeControl(this);
			spatial.removeFromParent();
			return;
		}

		Vector3f offset = target.getLocalTranslation().subtract(spatial.getLocalTranslation());

		float targetAzm = MyVector3f.azimuth(offset);
		float targetAlt = MyVector3f.altitude(offset);

		float missileAzm = MyVector3f.azimuth(spatial.getLocalRotation().mult(Vector3f.UNIT_Z));
		float missileAlt = MyVector3f.altitude(spatial.getLocalRotation().mult(Vector3f.UNIT_Z));

		float signAzm = Math.signum(targetAzm - missileAzm);
		float signAlt = Math.signum(targetAlt - missileAlt);

		float azimuth = missileAzm + (signAzm * MAX_AZIMUTH * updateInterval);
		float altitude = missileAlt + (signAlt * MAX_ALTITUDE * updateInterval);

		Vector3f direction = MyVector3f.fromAltAz(altitude, azimuth);
		Quaternion q = new Quaternion().lookAt(direction, Vector3f.UNIT_Y);

		spatial.setLocalRotation(q);

		// FastMath.extrapolateLinear(scale, startValue, endValue)
	}

}
