package sandbox3.bblaster.explosions;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import jme3.common.material.MtlParticle;

public final class PeExplosionFlash extends ParticleEmitter {

	public PeExplosionFlash(AssetManager assetManager, float size) {
		super("flash", Type.Triangle, 30);

		setStartColor(ColorRGBA.Yellow);
		setEndColor(new ColorRGBA(1, 1, 0, 0.125f));

		setStartSize(0.1f * size);
		setEndSize(3.0f * size);
		setShape(new EmitterSphereShape(Vector3f.ZERO, size));

		setGravity(0, 0, 0);
		setLowLife(.2f);
		setHighLife(.72f);
		getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_Y.mult(size));
		getParticleInfluencer().setVelocityVariation(1);
		setImagesX(2);
		setImagesY(2);

		setParticlesPerSec(0);

		setMaterial(new MtlParticle(assetManager, "Effects/Explosion/flash.png"));

		// addControl(new CtDetachParticleEmitter());
		
		addControl(new CtParticleEmitterSize() {
			
			@Override
			public void updateSize(float size) {
				setStartSize(0.1f * size);
				setEndSize(3.0f * size);
				setShape(new EmitterSphereShape(Vector3f.ZERO, size));
			}
		});
	}

}
