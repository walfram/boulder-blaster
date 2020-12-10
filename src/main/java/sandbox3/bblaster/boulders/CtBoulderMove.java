package sandbox3.bblaster.boulders;

import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;
import sandbox3.bblaster.Settings;

public final class CtBoulderMove extends SimpleControl {

	private final float speed;

	public CtBoulderMove(float size) {
		this.speed = Settings.boulderMaxSpeed * (1f - size / Settings.boulderMaxRadius);
	}

	// TODO debug constructor, no move for boulder
	public CtBoulderMove(float size, float speed) {
		this.speed = speed;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		Vector3f velocity = spatial.getLocalRotation().mult(Vector3f.UNIT_Z).mult(updateInterval).mult(speed);
		spatial.move(velocity);

	}

	public float speed() {
		return speed;
	}

}
