package test.missile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.effect.ParticleEmitter;

import jme3utilities.SimpleControl;

final class CtMissileEngine extends SimpleControl {

	private static final Logger logger = LoggerFactory.getLogger(CtMissileEngine.class);
	private final ParticleEmitter engine;

	public CtMissileEngine(ParticleEmitter engine) {
		this.enabled = false;
		this.engine = engine;
		this.engine.setParticlesPerSec(0);
	}
	
	@Override
	public void toggleEnabled() {
		super.toggleEnabled();
		logger.debug("engine enabled = {}", isEnabled());
		engine.setParticlesPerSec(isEnabled() ? 20 : 0);
	}
	
}
