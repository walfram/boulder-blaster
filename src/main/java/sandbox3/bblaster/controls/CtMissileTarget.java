package sandbox3.bblaster.controls;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;

public final class CtMissileTarget extends SimpleControl {

	private final Spatial target;

	public CtMissileTarget(Spatial target) {
		this.target = target;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		if (target == null || target.getParent() == null)
			return;

		Quaternion from = spatial.getLocalRotation().clone();

		Vector3f direction = target.getLocalTranslation().subtract(spatial.getLocalTranslation());
		Quaternion to = from.clone().lookAt(direction, Vector3f.UNIT_Y);

		// from.slerp(from, to, updateInterval * 2.5f);
		from.slerp(from, to, updateInterval * 5f);

		spatial.setLocalRotation(from);
	}

}
