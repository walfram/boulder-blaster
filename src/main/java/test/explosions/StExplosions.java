package test.explosions;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import sandbox3.bblaster.explosions.PeExplosionFlash;
import sandbox3.bblaster.explosions.PeExplosionShockwave;
import sandbox3.bblaster.explosions.PeExplosionSparks;

final class StExplosions extends BaseAppState {

	private final Node scene = new Node("scene");

	public StExplosions(Node rootNode) {
		rootNode.attachChild(scene);
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

	public void createExplosion(Vector3f translation, float size) {
		ParticleEmitter shockwave = new PeExplosionShockwave(getApplication().getAssetManager(), size);
		shockwave.setLocalTranslation(translation);
		scene.attachChild(shockwave);
		shockwave.emitAllParticles();

		ParticleEmitter sparks = new PeExplosionSparks(getApplication().getAssetManager(), size);
		sparks.setLocalTranslation(translation);
		scene.attachChild(sparks);
		sparks.emitAllParticles();

		ParticleEmitter flash = new PeExplosionFlash(getApplication().getAssetManager(), size);
		flash.setLocalTranslation(translation);
		scene.attachChild(flash);
		flash.emitAllParticles();
	}

}
