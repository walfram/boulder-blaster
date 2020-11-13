package sandbox3.bblaster.controls;

import jme3utilities.SimpleControl;

@Deprecated
public final class CtDamage extends SimpleControl {

	private final float value;
	
	public CtDamage(float value) {
		this.value = value;
	}

	public float value() {
		return value;
	}
}
