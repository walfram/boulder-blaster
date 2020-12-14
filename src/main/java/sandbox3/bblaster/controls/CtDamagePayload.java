package sandbox3.bblaster.controls;

import jme3utilities.SimpleControl;

public final class CtDamagePayload extends SimpleControl {

	private final float value;
	
	public CtDamagePayload(float value) {
		this.value = value;
	}

	public float value() {
		return value;
	}
}
