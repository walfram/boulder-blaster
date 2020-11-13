package sandbox3.bblaster.models.missiles;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.math.ColorRGBA;

import jme3.common.material.MtlParticle;

final class PeMissileTrail extends ParticleEmitter {

	public PeMissileTrail(AssetManager assetManager) {
		super("smoke-trail", Type.Triangle, 300);
		
		setMaterial(new MtlParticle(assetManager, "Effects/Smoke/Smoke.png"));
		setImagesX(15);
		setImagesY(1);
		setGravity(0, 0, 0);
		setStartColor(ColorRGBA.White);
		setEndColor(ColorRGBA.DarkGray);
		setStartSize(2.5f);
		setEndSize(0.5f);
		setLowLife(6f);
		setHighLife(7f);
		setInWorldSpace(true);
	}

}
