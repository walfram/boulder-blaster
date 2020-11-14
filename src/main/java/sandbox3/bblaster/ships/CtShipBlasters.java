package sandbox3.bblaster.ships;

import java.util.List;
import java.util.stream.Collectors;

import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Transform;

import jme3utilities.SimpleControl;

public final class CtShipBlasters extends SimpleControl {

	private final List<ParticleEmitter> blasters;

	public CtShipBlasters(List<ParticleEmitter> blasters) {
		this.blasters = List.copyOf(blasters);
		setEnabled(false);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		blasters.forEach(blaster -> {
			blaster.setEnabled(isEnabled());
			if (!isEnabled())
				blaster.killAllParticles();
		});
	}

	public List<Transform> transforms() {
		return blasters.stream().map(pe -> pe.getWorldTransform().clone()).collect(Collectors.toList());
	}

}
