package sandbox3.bblaster.controls;

import java.util.List;

import com.jme3.effect.ParticleEmitter;

import jme3utilities.SimpleControl;

public final class CtBlastersFx extends SimpleControl {

	private final List<ParticleEmitter> blasters;
	private float elapsed;

	public CtBlastersFx(List<ParticleEmitter> blasters) {
		this.blasters = blasters;
	}

	public void emit() {
		blasters.forEach(b -> b.setEnabled(true));
		elapsed = 0f;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		elapsed += updateInterval;

		if (elapsed > (1f / 8.33f)) {
			blasters.forEach(b -> {
				b.setEnabled(false);
				b.killAllParticles();
			});
		}
	}

}
