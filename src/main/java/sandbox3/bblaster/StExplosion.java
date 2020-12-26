package sandbox3.bblaster;

import java.util.ArrayDeque;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;
import sandbox3.bblaster.explosion.CtParticleEmitterSize;
import sandbox3.bblaster.explosion.FxBoulderExplosion;
import sandbox3.bblaster.explosion.FxMissileExplosion;
import sandbox3.bblaster.explosion.FxProjectileExplosion;

public final class StExplosion extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StExplosion.class);

	private static final int BOULDER_EXPLOSIONS = 64;
	private static final int MISSILE_EXPLOSIONS = 64;
	private static final int PROJECTILE_EXPLOSIONS = 512;

	private final Node explosions = new Node("explosions");

	private final Deque<Node> boulderExplosions = new ArrayDeque<>(BOULDER_EXPLOSIONS);
	private final Deque<Node> missileExplosions = new ArrayDeque<>(MISSILE_EXPLOSIONS);
	private final Deque<Node> projectileExplosions = new ArrayDeque<>(PROJECTILE_EXPLOSIONS);

	public StExplosion(Node rootNode) {
		rootNode.attachChild(explosions);
	}

	@Override
	protected void initialize(Application app) {
		moreBoulderExplosions();
		moreMissileExplosions();
		moreProjectileExplosions();
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

	public void missileExplosion(Vector3f translation) {
		if (missileExplosions.isEmpty()) {
			logger.debug("missile explosions exhausted = {}, adding more", missileExplosions.size());
			moreMissileExplosions();
		}

		Node missileExplosion = missileExplosions.pop();
		missileExplosion.setLocalTranslation(translation);
		explosions.attachChild(missileExplosion);
		missileExplosion.getChildren().forEach(s -> {
			s.getControl(CtParticleEmitterSize.class).updateSize(30f);
			((ParticleEmitter) s).emitAllParticles();
		});
	}

	public void projectileExplosion(Vector3f translation) {
		if (projectileExplosions.isEmpty()) {
			logger.debug("projectile explosions exhausted = {}, adding more", projectileExplosions.size());
			moreProjectileExplosions();
		}

		Node projectileExplosion = projectileExplosions.pop();
		projectileExplosion.setLocalTranslation(translation);
		explosions.attachChild(projectileExplosion);

		projectileExplosion.getChildren().forEach(s -> {
			((ParticleEmitter) s).emitAllParticles();
		});
	}

	public void boulderExplosion(Vector3f translation, float size) {
		if (boulderExplosions.isEmpty()) {
			logger.debug("boulder explosions exhausted = {}, adding more", boulderExplosions.size());
			moreBoulderExplosions();
		}

		Node boulderExplosion = boulderExplosions.pop();
		boulderExplosion.setLocalTranslation(translation);
		explosions.attachChild(boulderExplosion);
		// logger.debug("created {} at {}, size = {}", boulderExplosion, translation, size);
		boulderExplosion.getChildren().forEach(s -> {
			s.getControl(CtParticleEmitterSize.class).updateSize(size);
			((ParticleEmitter) s).emitAllParticles();
		});
	}

	// TODO move common "removal control" code to separate class
	private void moreBoulderExplosions() {
		for (int idx = 0; idx < BOULDER_EXPLOSIONS; idx++) {
			Node boulderExplosion = new FxBoulderExplosion(getApplication().getAssetManager());
			getApplication().getRenderManager().preloadScene(boulderExplosion);

			boulderExplosion.addControl(new SimpleControl() {
				@Override
				protected void controlUpdate(float updateInterval) {
					super.controlUpdate(updateInterval);

					// true because &= is "a & b"
					boolean done = true;

					for (Spatial s : boulderExplosion.getChildren()) {
						if (s instanceof ParticleEmitter) {
							done &= ((ParticleEmitter) s).getNumVisibleParticles() < 1;
						}
					}

					if (!done)
						return;

					boulderExplosion.removeFromParent();
					boulderExplosions.push(boulderExplosion);
				}
			});

			boulderExplosions.push(boulderExplosion);
		}
	}

	private void moreMissileExplosions() {
		for (int idx = 0; idx < MISSILE_EXPLOSIONS; idx++) {
			Node missileExplosion = new FxMissileExplosion(getApplication().getAssetManager());
			getApplication().getRenderManager().preloadScene(missileExplosion);

			missileExplosion.addControl(new SimpleControl() {
				@Override
				protected void controlUpdate(float updateInterval) {
					super.controlUpdate(updateInterval);

					boolean done = true;

					for (Spatial s : missileExplosion.getChildren()) {
						if (s instanceof ParticleEmitter) {
							done &= ((ParticleEmitter) s).getNumVisibleParticles() < 1;
						}
					}

					if (!done)
						return;

					missileExplosion.removeFromParent();
					missileExplosions.push(missileExplosion);
				}
			});

			missileExplosions.push(missileExplosion);
		}
	}

	private void moreProjectileExplosions() {
		for (int idx = 0; idx <= PROJECTILE_EXPLOSIONS; idx++) {
			Node projectileExplosion = new FxProjectileExplosion(getApplication().getAssetManager());
			getApplication().getRenderManager().preloadScene(projectileExplosion);

			projectileExplosion.addControl(new SimpleControl() {
				@Override
				protected void controlUpdate(float updateInterval) {
					super.controlUpdate(updateInterval);

					boolean done = true;

					for (Spatial s : projectileExplosion.getChildren()) {
						if (s instanceof ParticleEmitter) {
							done &= ((ParticleEmitter) s).getNumVisibleParticles() < 1;
						}
					}

					if (!done)
						return;

					projectileExplosion.removeFromParent();
					projectileExplosions.push(projectileExplosion);
				}
			});

			projectileExplosions.push(projectileExplosion);
		}

	}

}
