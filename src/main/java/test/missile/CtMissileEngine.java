package test.missile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

public final class CtMissileEngine extends SimpleControl {

	private static final Logger logger = LoggerFactory.getLogger(CtMissileEngine.class);
	private final ParticleEmitter engine;
	private final ParticleEmitter trail;
	private boolean moving = false;
	private final float speed;

	private float elapsed = 0f;

	public CtMissileEngine(ParticleEmitter engine, ParticleEmitter trail, float speed) {
		this.engine = engine;
		this.trail = trail;
		this.speed = speed;

		setEnabled(false);
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		if (!moving)
			return;

		Vector3f forward = spatial.getLocalRotation().mult(Vector3f.UNIT_Z);
		spatial.move(forward.mult(updateInterval * speed));

		elapsed += updateInterval;

		if (elapsed > 7) {
			spatial.removeFromParent();
			logger.debug("missile self-destruct");
		}
	}

	public void launch() {
		if (isEnabled())
			moving = true;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		logger.debug("engine enabled = {}", isEnabled());
		engine.setParticlesPerSec(isEnabled() ? 10 : 0);
		trail.setParticlesPerSec(isEnabled() ? 25 : 0);
	}

}
