package sandbox3.bblaster.explosion;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import jme3.common.material.MtlParticle;

public final class PeExplosionSparks extends ParticleEmitter {

	public PeExplosionSparks(AssetManager assetManager, float size) {
		super("sparks", Type.Triangle, 400);

		setStartColor(ColorRGBA.Yellow);
		setEndColor(new ColorRGBA(1, 0, 0, 0.125f));
		setStartSize(size);
		setEndSize(size);
		setGravity(0, 0, 0);
		setLowLife(1.0f);
		setHighLife(1.5f);
		getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_Y.mult(5f * size));
		getParticleInfluencer().setVelocityVariation(1f);
		setFacingVelocity(true);
		setImagesX(1);
		setImagesY(1);

		setMaterial(new MtlParticle(assetManager, "Effects/Explosion/spark.png"));

		setParticlesPerSec(0);

		// addControl(new CtDetachParticleEmitter());
		addControl(new CtParticleEmitterSize() {
			@Override
			public void updateSize(float size) {
				setStartSize(size);
				setEndSize(size);
				getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_Y.mult(5f * size));
			}
		});
	}

}
