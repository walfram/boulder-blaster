package sandbox3.bblaster.boulders;

import jme3utilities.SimpleControl;

public final class CtBoulderHealth extends SimpleControl {

	private final float health;
	private float value;

	public CtBoulderHealth(float health) {
		this.health = health;
		this.value = health;
	}

	public void applyDamage(float damage) {
		value -= damage;
	}

	public boolean isDead() {
		return value <= 0f;
	}
	
	public float percent() {
		return value / health;
	}

	public float value() {
		return value;
	}

	public float max() {
		return health;
	}

}
