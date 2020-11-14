package sandbox3.bblaster.controls;

import jme3utilities.SimpleControl;

public final class CtPayload extends SimpleControl {

	private final float value;
	
	public CtPayload(float value) {
		this.value = value;
	}

	public float value() {
		return value;
	}
}
