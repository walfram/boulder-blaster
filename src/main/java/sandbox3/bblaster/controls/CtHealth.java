package sandbox3.bblaster.controls;

import jme3utilities.SimpleControl;

public final class CtHealth extends SimpleControl {

	private final float health;
	private float value;

	public CtHealth(float health) {
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