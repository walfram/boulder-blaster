package sandbox3.bblaster.player;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

public final class CtShipRoll extends SimpleControl {

	public void roll(double value, double tpf) {
		Vector3f forward = spatial.getLocalRotation().mult(Vector3f.UNIT_Z);
		
		Quaternion roll = new Quaternion().fromAngleAxis((float) (value * tpf), forward);
		Quaternion rolled = roll.mult(spatial.getLocalRotation());
		
		spatial.setLocalRotation(rolled);
	}

}
