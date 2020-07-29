package common.controls;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

public final class CtYaw extends SimpleControl {

	public void yaw(double value, double tpf) {
		Vector3f up = spatial.getLocalRotation().mult(Vector3f.UNIT_Y);
		
		Quaternion yaw = new Quaternion().fromAngleAxis((float) (value * tpf), up);
		Quaternion yawed = yaw.mult(spatial.getLocalRotation());
		
		spatial.setLocalRotation(yawed);
	}

}
