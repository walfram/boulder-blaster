package sandbox3.bblaster;

import jme3utilities.SimpleControl;

final class CtDamage extends SimpleControl {

	private final float value;
	
	public CtDamage(float value) {
		this.value = value;
	}

	public float value() {
		return value;
	}
}
