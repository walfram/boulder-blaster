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

import sandbox3.bblaster.explosion.CtParticleEmitterSize;
import sandbox3.bblaster.explosion.FxExplosion;
import sandbox3.bblaster.explosion.PeExplosionFlash;
import sandbox3.bblaster.explosion.PeExplosionShockwave;
import sandbox3.bblaster.explosion.PeExplosionSparks;
import sandbox3.bblaster.explosion.PeMissileFlash;
import sandbox3.bblaster.explosion.PeMissileSparks;
import sandbox3.bblaster.explosion.PeProjectileSparks;

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
			s.getControl(CtParticleEmitterSize.class).updateSize(50f);
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

		boulderExplosion.getChildren().forEach(s -> {
			s.getControl(CtParticleEmitterSize.class).updateSize(size);
			((ParticleEmitter) s).emitAllParticles();
		});
	}

	private void moreBoulderExplosions() {
		for (int idx = 0; idx < BOULDER_EXPLOSIONS; idx++) {
			Node boulderExplosion = new FxExplosion(boulderExplosions, new PeExplosionShockwave(getApplication()
					.getAssetManager(), 1f), new PeExplosionSparks(getApplication().getAssetManager(), 1f),
					new PeExplosionFlash(getApplication().getAssetManager(), 1f));
			boulderExplosions.push(boulderExplosion);
		}

		getApplication().getRenderManager().preloadScene(boulderExplosions.peek());
	}

	private void moreMissileExplosions() {
		for (int idx = 0; idx < MISSILE_EXPLOSIONS; idx++) {
			Node missileExplosion = new FxExplosion(missileExplosions, new PeMissileFlash(getApplication().getAssetManager(),
					50f), new PeMissileSparks(getApplication().getAssetManager(), 50f));
			missileExplosions.push(missileExplosion);
		}

		getApplication().getRenderManager().preloadScene(missileExplosions.peek());
	}

	private void moreProjectileExplosions() {
		for (int idx = 0; idx <= PROJECTILE_EXPLOSIONS; idx++) {
			Node projectileExplosion = new FxExplosion(projectileExplosions, new PeProjectileSparks(getApplication()
					.getAssetManager(), 30f));
			projectileExplosions.push(projectileExplosion);
		}

		getApplication().getRenderManager().preloadScene(projectileExplosions.peek());
	}

}
