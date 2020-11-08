package test.missile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

final class CtMissileEngine extends SimpleControl {

	private static final Logger logger = LoggerFactory.getLogger(CtMissileEngine.class);
	private final ParticleEmitter engine;
	private boolean moving = false;
	private final float speed;

	public CtMissileEngine(ParticleEmitter engine, float speed) {
		this.enabled = false;
		this.engine = engine;
		this.engine.setParticlesPerSec(0);
		this.speed = speed;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		if (!moving)
			return;

		Vector3f forward = spatial.getLocalRotation().mult(Vector3f.UNIT_Z);
		spatial.move(forward.mult(updateInterval * speed));
	}

	public void launch() {
		if (isEnabled())
			moving = true;
	}

	@Override
	public void toggleEnabled() {
		super.toggleEnabled();
		logger.debug("engine enabled = {}", isEnabled());
		engine.setParticlesPerSec(isEnabled() ? 20 : 0);
	}

}
