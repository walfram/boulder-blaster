package test.boulder.instanced;

import jme3utilities.SimpleControl;

final class CtBoulderSpin extends SimpleControl {

	private final float zAngle;

	public CtBoulderSpin(float size) {
		zAngle = 1f / size;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		spatial.rotate(0, 0, zAngle);
	}

}
