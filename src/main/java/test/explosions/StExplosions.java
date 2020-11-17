package test.explosions;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

import sandbox3.bblaster.explosion.PeExplosionShockwave;
import sandbox3.bblaster.explosion.PeExplosionSparks;

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
		// createExplosion(translation, 150f, 1.5f);
		ParticleEmitter shockwave = new PeExplosionShockwave(getApplication().getAssetManager(), size);
		shockwave.setLocalTranslation(translation);
		scene.attachChild(shockwave);

		ParticleEmitter sparks = new PeExplosionSparks(getApplication().getAssetManager(), size);
		sparks.setLocalTranslation(translation);
		scene.attachChild(sparks);

		shockwave.emitAllParticles();
		sparks.emitAllParticles();

	}

}
