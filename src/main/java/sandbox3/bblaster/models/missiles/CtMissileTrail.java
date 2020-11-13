package sandbox3.bblaster.models.missiles;

import com.jme3.effect.ParticleEmitter;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;

public final class CtMissileTrail extends SimpleControl {

	private final Spatial missile;

	public CtMissileTrail(Spatial missile) {
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
