package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import sandbox3.bblaster.explosion.PeExplosionFlash;
import sandbox3.bblaster.explosion.PeExplosionShockwave;
import sandbox3.bblaster.explosion.PeExplosionSparks;
import sandbox3.bblaster.explosion.PeMissileFlash;
import sandbox3.bblaster.explosion.PeMissileSparks;
import sandbox3.bblaster.explosion.PeProjectileSparks;

public final class StExplosion extends BaseAppState {

	private final Node explosions = new Node("explosions");

	public StExplosion(Node rootNode) {
		rootNode.attachChild(explosions);
	}

	@Override
	protected void initialize(Application app) {
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
		ParticleEmitter sparks = new PeMissileSparks(getApplication().getAssetManager(), 30f);
		sparks.setLocalTranslation(translation);
		explosions.attachChild(sparks);
		sparks.emitAllParticles();

		ParticleEmitter flash = new PeMissileFlash(getApplication().getAssetManager(), 30f);
		flash.setLocalTranslation(translation);
		explosions.attachChild(flash);
		flash.emitAllParticles();
	}

	public void projectileExplosion(Vector3f translation) {
		ParticleEmitter sparks = new PeProjectileSparks(getApplication().getAssetManager(), 20f);
		sparks.setLocalTranslation(translation);
		explosions.attachChild(sparks);
		sparks.emitAllParticles();
	}

	public void boulderExplosion(Vector3f translation, float size) {
		ParticleEmitter shockwave = new PeExplosionShockwave(getApplication().getAssetManager(), size);
		shockwave.setLocalTranslation(translation);
		explosions.attachChild(shockwave);
		shockwave.emitAllParticles();

		ParticleEmitter sparks = new PeExplosionSparks(getApplication().getAssetManager(), size);
		sparks.setLocalTranslation(translation);
		explosions.attachChild(sparks);
		sparks.emitAllParticles();

		ParticleEmitter flash = new PeExplosionFlash(getApplication().getAssetManager(), size);
		flash.setLocalTranslation(translation);
		explosions.attachChild(flash);
		flash.emitAllParticles();
	}

}
