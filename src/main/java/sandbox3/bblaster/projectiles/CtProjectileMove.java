package sandbox3.bblaster.projectiles;

import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

public final class CtProjectileMove extends SimpleControl {

	private final float speed;

	public CtProjectileMove(float speed) {
		this.speed = speed;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		Vector3f velocity = spatial.getLocalRotation().mult(Vector3f.UNIT_Z).mult(updateInterval).mult(speed);
		spatial.move(velocity);
	}

}
