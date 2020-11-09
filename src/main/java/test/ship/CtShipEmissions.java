package test.ship;

import java.util.List;

import com.jme3.effect.ParticleEmitter;

import jme3utilities.SimpleControl;

final class CtShipEmissions extends SimpleControl {

	private final List<ParticleEmitter> emissions;

	public CtShipEmissions(List<ParticleEmitter> emissions) {
		this.emissions = List.copyOf(emissions);
		setEnabled(false);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		emissions.forEach(engine -> engine.setParticlesPerSec(isEnabled() ? 50 : 0));
	}

}
