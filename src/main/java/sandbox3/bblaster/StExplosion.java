package sandbox3.bblaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.effect.ParticleEmitter;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

import jme3.common.material.MtlUnshaded;
import jme3utilities.SimpleControl;
import jme3utilities.mesh.Octasphere;
import sandbox3.bblaster.explosion.PeExplosionFlash;
import sandbox3.bblaster.explosion.PeExplosionShockwave;
import sandbox3.bblaster.explosion.PeExplosionSparks;
import sandbox3.bblaster.explosion.PeProjectileSparks;

public final class StExplosion extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StExplosion.class);

	private final Node explosions = new Node("explosions");

	private Mesh mesh;
	private Material material;

	public StExplosion(Node rootNode) {
		rootNode.attachChild(explosions);
	}

	@Override
	protected void initialize(Application app) {
		mesh = new Octasphere(2, 1f);
		material = new MtlUnshaded(app.getAssetManager(), new ColorRGBA(1, 1, 0, 0.5f));
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
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
		createExplosion(translation, 50f, 0.75f);
		// explosion(translation, 50f);
	}

	public void projectileExplosion(Vector3f translation) {
		ParticleEmitter sparks = new PeProjectileSparks(getApplication().getAssetManager(), 5f);
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

	@Deprecated
	private void createExplosion(Vector3f translation, float maxSize, float time) {

		Geometry explosion = new Geometry("explosion", mesh);
		explosion.setQueueBucket(Bucket.Transparent);

		Material mtl = material.clone();
		mtl.setColor("Color", new ColorRGBA(1, 1, 0, 0.5f));

		explosion.setMaterial(mtl);

		explosion.setLocalTranslation(translation);

		explosions.attachChild(explosion);

		explosion.addControl(new SimpleControl() {
			float elapsed = 0f;

			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				elapsed += updateInterval;

				float percent = elapsed / time;

				mtl.setColor("Color", new ColorRGBA(1, 1, 0, 0.5f).interpolateLocal(new ColorRGBA(1, 0, 0, 0.5f), percent));

				explosion.setLocalScale(percent * maxSize);

				if (elapsed >= time)
					explosion.removeFromParent();
			}
		});

	}

}
