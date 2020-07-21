package common.misc;

import com.google.common.primitives.Floats;

public final class Cooldown {

	private final float duration;
	private float value = 0f;

	public Cooldown(float seconds) {
		this.duration = seconds;
	}

	public boolean isReady() {
		return value <= 0f;
	}

	public void update(float tpf) {
		value -= tpf;
		value = Floats.max(value, 0f);
	}

	public void reset() {
		value = duration;
	}

	public float percent() {
		return 1f - value / duration;
	}

}
