package sandbox3.bblaster.explosions;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import jme3.common.material.MtlParticle;

public final class PeProjectileSparks extends ParticleEmitter {

	public PeProjectileSparks(AssetManager assetManager, float size) {
		super("sparks", Type.Triangle, 50);

		setStartColor(ColorRGBA.Yellow);
		setEndColor(new ColorRGBA(1, 0, 0, 0.125f));
		setStartSize(size);
		setEndSize(size);
		setGravity(0, 0, 0);
		setLowLife(0.5f);
		setHighLife(1.0f);
		getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_Y.mult(5f * size));
		getParticleInfluencer().setVelocityVariation(1f);
		setFacingVelocity(true);
		setImagesX(1);
		setImagesY(1);

		setMaterial(new MtlParticle(assetManager, "Effects/Explosion/spark.png"));

		setParticlesPerSec(0);

		// addControl(new CtDetachParticleEmitter());
	}

}
