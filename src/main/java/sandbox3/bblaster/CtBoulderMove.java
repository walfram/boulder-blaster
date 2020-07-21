package sandbox3.bblaster;

import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

final class CtBoulderMove extends SimpleControl {

	private final float speed;

	public CtBoulderMove(float size) {
		this.speed = new Const().boulderMaxSpeed() * (1f - size / new Const().boulderMaxSize());
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		Vector3f velocity = spatial.getLocalRotation().mult(Vector3f.UNIT_Z).mult(updateInterval).mult(speed);
		Vector3f updated = spatial.getLocalTranslation().add(velocity);

		spatial.setLocalTranslation(updated);

	}

}
