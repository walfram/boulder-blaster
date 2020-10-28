package sandbox3.bblaster.controls;

import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

public final class CtMissileMove extends SimpleControl {

	private final float speed;

	public CtMissileMove(float speed) {
		this.speed = speed;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		Vector3f velocity = spatial.getLocalRotation().mult(Vector3f.UNIT_Z).mult(updateInterval).mult(speed);
		Vector3f updated = spatial.getLocalTranslation().add(velocity);

		spatial.setLocalTranslation(updated);
	}

}
