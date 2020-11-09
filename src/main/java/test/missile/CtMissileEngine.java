package test.missile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

public final class CtMissileEngine extends SimpleControl {

	private static final Logger logger = LoggerFactory.getLogger(CtMissileEngine.class);

	private final float speed;

	private float elapsed = 0f;

	public CtMissileEngine(float speed) {
		this.speed = speed;
		setEnabled(false);
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		Vector3f forward = spatial.getLocalRotation().mult(Vector3f.UNIT_Z);
		spatial.move(forward.mult(updateInterval).mult(speed));

		elapsed += updateInterval;

		if (elapsed > 10f) {
			spatial.removeFromParent();
			logger.debug("missile self-destruct");
		}
	}

}
