package test.player;

import jme3utilities.SimpleControl;

final class CtCooldown extends SimpleControl {

	private final float rateOfFire;
	private float cooldown = 0f;

	public CtCooldown(float rateOfFire) {
		this.rateOfFire = rateOfFire;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		if (cooldown > 0f) {
			cooldown = Math.max(0, cooldown - updateInterval);
		}
	}

	public boolean isReady() {
		return cooldown == 0f;
	}

	public void reset() {
		cooldown = rateOfFire;
	}

}
