package common.controls;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

public final class CtPitch extends SimpleControl {

	public void pitch(double value, double tpf) {
		Vector3f left = spatial.getLocalRotation().mult(Vector3f.UNIT_X);

		Quaternion pitch = new Quaternion().fromAngleAxis((float) (value * tpf), left);
		Quaternion pitched = pitch.mult(spatial.getLocalRotation());

		spatial.setLocalRotation(pitched);
	}

}
