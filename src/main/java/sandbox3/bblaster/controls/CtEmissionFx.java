package sandbox3.bblaster.controls;

import java.util.List;

import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

public final class CtEmissionFx extends SimpleControl {

	private final List<ParticleEmitter> emissions;

	public CtEmissionFx(List<ParticleEmitter> emissions) {
		this.emissions = emissions;
	}

	public void updateThrust(float thrust) {
		emissions.forEach(pe -> {
			pe.getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_Z.negate().mult(50f * thrust));
		});
	}

}
