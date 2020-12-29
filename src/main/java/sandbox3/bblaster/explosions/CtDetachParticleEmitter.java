package sandbox3.bblaster.explosions;

import com.jme3.effect.ParticleEmitter;

import jme3utilities.SimpleControl;

final class CtDetachParticleEmitter extends SimpleControl {
	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		if (((ParticleEmitter) spatial).getNumVisibleParticles() < 1) {
			spatial.removeFromParent();
		}
	}
}