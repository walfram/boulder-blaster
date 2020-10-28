package sandbox3.bblaster.effects;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

import sandbox3.bblaster.materials.MtlParticle;

public final class PeSmokeTrail extends ParticleEmitter {

	public PeSmokeTrail(AssetManager assetManager) {
		super("smoke-trail", Type.Triangle, 1024);

		Material mtlSmoke = new MtlParticle(assetManager, "Effects/Smoke/Smoke.png");
		// mtlSmoke.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		setMaterial(mtlSmoke);

		setImagesX(15);
		setImagesY(1);

		setStartColor(ColorRGBA.DarkGray);
		setEndColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.0125f));

		setStartSize(2);
		setEndSize(8);

		setLowLife(5f);
		setHighLife(7.5f);

		setGravity(0, 0, 0);

		setParticlesPerSec(100);

		setInWorldSpace(true);
	}
}
