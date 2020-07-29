package common.controls;

import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

public final class CtThrust extends SimpleControl {

	private final float maxSpeed;
	private float thrust = 0f;

	public CtThrust(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

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

}
