package sandbox3.bblaster.ships;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;

public final class CtMissileGuidance extends SimpleControl {

	private static final Logger logger = LoggerFactory.getLogger(CtMissileGuidance.class);

	private final Spatial target;


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

		Vector3f offset = target.getWorldTranslation().subtract(spatial.getWorldTranslation());

		Quaternion from = spatial.getLocalRotation().clone();
		Quaternion to = new Quaternion().lookAt(offset, Vector3f.UNIT_Y);

		from.slerp(to, updateInterval * 2f);
		
		spatial.setLocalRotation(from);
	}

}
