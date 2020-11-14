package sandbox3.bblaster.ships;

import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

public final class CtShipEngine extends SimpleControl {

	public CtShipEngine() {
		setEnabled(false);
	}
	
	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);
		
		Vector3f velocity = spatial.getLocalRotation().mult(Vector3f.UNIT_Z).mult(updateInterval * 100f);
		spatial.move(velocity);
	}
	
}
