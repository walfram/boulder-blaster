package sandbox3.bblaster.explosion;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import jme3.common.material.MtlParticle;

public final class PeExplosionShockwave extends ParticleEmitter {

	public PeExplosionShockwave(AssetManager assetManager, float size) {
		super("shockwave", Type.Triangle, 1);

		setStartColor(ColorRGBA.Yellow);
		setEndColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.25f));
		setStartSize(0);
		setEndSize(size * 5f);
		setGravity(0, 0, 0);
		setLowLife(1.5f);
		setHighLife(1.5f);
		getParticleInfluencer().setInitialVelocity(Vector3f.ZERO.clone());
		getParticleInfluencer().setVelocityVariation(0f);
		setImagesX(1);
		setImagesY(1);

		setMaterial(new MtlParticle(assetManager, "Effects/Explosion/shockwave.png"));

		// emitAllParticles();
		setParticlesPerSec(0);

		addControl(new CtDetachParticleEmitter());
	}

}