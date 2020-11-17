package sandbox3.bblaster.explosion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.effect.ParticleEmitter;

import jme3utilities.SimpleControl;

final class CtDetachParticleEmitter extends SimpleControl {
	private static final Logger logger = LoggerFactory.getLogger(CtDetachParticleEmitter.class);

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		if (((ParticleEmitter) spatial).getNumVisibleParticles() < 1) {
			spatial.removeFromParent();
			logger.debug("detached = {}", spatial);
		}
	}
}