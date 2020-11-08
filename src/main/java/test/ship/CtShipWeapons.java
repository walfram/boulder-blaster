package test.ship;

import java.util.List;

import com.jme3.effect.ParticleEmitter;

import jme3utilities.SimpleControl;

final class CtShipWeapons extends SimpleControl {

	private final List<ParticleEmitter> weapons;

	public CtShipWeapons(List<ParticleEmitter> weapons) {
		this.weapons = List.copyOf(weapons);
		setEnabled(false);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		weapons.forEach(weapon -> weapon.setParticlesPerSec(isEnabled() ? 8 : 0));
	}

}
