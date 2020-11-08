package test.ship;

import java.util.List;

import com.jme3.effect.ParticleEmitter;

import jme3utilities.SimpleControl;

final class CtShipEngines extends SimpleControl {

	private final List<ParticleEmitter> engines;

	public CtShipEngines(List<ParticleEmitter> engines) {
		this.engines = List.copyOf(engines);
		setEnabled(false);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		engines.forEach(engine -> engine.setParticlesPerSec(isEnabled() ? 50 : 0));
	}

}
