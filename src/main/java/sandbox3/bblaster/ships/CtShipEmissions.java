package sandbox3.bblaster.ships;

import java.util.List;

import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import jme3utilities.SimpleControl;

public final class CtShipEmissions extends SimpleControl {

	private static final Vector3f emissionVector = Vector3f.UNIT_Z.negate().mult(50f);

	private final Node ship;
	private final List<ParticleEmitter> emissions;

	public CtShipEmissions(Node ship, List<ParticleEmitter> emissions) {
		this.ship = ship;
		this.emissions = List.copyOf(emissions);
		setEnabled(false);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);
		Transform shipTransform = ship.getWorldTransform().clone();
		shipTransform.setScale(1f);
		spatial.setLocalTransform(shipTransform);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		emissions.forEach(emission -> {
			emission.setEnabled(isEnabled());
			if (!isEnabled())
				emission.killAllParticles();
		});
	}

	public void updateThrust(float thrust) {
		setEnabled(thrust > 0f);

		emissions.forEach(emission -> {
			emission.getParticleInfluencer().setInitialVelocity(emissionVector.mult(thrust));
		});
	}

}
