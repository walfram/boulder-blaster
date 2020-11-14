package sandbox3.bblaster.ships;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

public final class CtShipEngines extends SimpleControl {

	private final float maxSpeed;
	
	private float thrust = 0f;
	private float target = 0f;
	
	public CtShipEngines(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);
		
		if ( FastMath.abs( target - thrust ) <= 0.01f ) {
			target = 0f;
		}

		if (target != 0f) {
			float dir = FastMath.sign(target - thrust);
			thrust(dir, updateInterval);
		}
		
		Vector3f velocity = spatial.getLocalRotation().mult(Vector3f.UNIT_Z).mult(updateInterval).mult(thrust).mult(maxSpeed);
		Vector3f translated = spatial.getLocalTranslation().add(velocity);

		spatial.setLocalTranslation(translated);
	}

	public void thrust(double value, double tpf) {
		thrust += value * tpf;
		thrust = Math.min(thrust, 1.0f);
		thrust = Math.max(thrust, 0f);
	}

	public double value() {
		return thrust;
	}
	
	public void thrustTo(float value) {
		target = value;
	}

}
