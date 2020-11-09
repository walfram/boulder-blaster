package test.missile;

import com.jme3.effect.ParticleEmitter;

import jme3utilities.SimpleControl;

public final class CtMissileEmission extends SimpleControl {

	private final ParticleEmitter emission;
	private final ParticleEmitter trail;

	public CtMissileEmission(ParticleEmitter emission, ParticleEmitter trail) {
		this.emission = emission;
		this.trail = trail;

		setEnabled(false);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		emission.setParticlesPerSec(isEnabled() ? 10 : 0);
		trail.setParticlesPerSec(isEnabled() ? 25 : 0);
	}

}
