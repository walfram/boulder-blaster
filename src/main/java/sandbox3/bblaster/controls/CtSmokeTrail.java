package sandbox3.bblaster.controls;

import com.jme3.effect.ParticleEmitter;
import com.jme3.scene.Node;

import jme3utilities.SimpleControl;

public final class CtSmokeTrail extends SimpleControl {

	private final Node missile;

	public CtSmokeTrail(Node missile) {
		this.missile = missile;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		getSpatial().setLocalTranslation(missile.getLocalTranslation());

		if (missile.getParent() == null) {
			((ParticleEmitter) spatial).setParticlesPerSec(0);

			if (((ParticleEmitter) spatial).getNumVisibleParticles() <= 5)
				spatial.removeFromParent();
		}
	}
}