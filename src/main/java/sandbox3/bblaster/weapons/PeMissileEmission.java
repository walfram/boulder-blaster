package sandbox3.bblaster.weapons;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.math.ColorRGBA;

import jme3.common.material.MtlParticle;

final class PeMissileEmission extends ParticleEmitter {

	public PeMissileEmission(AssetManager assetManager) {
		super("engine", Type.Triangle, 10);
		setMaterial(new MtlParticle(assetManager, "Effects/Explosion/flame.png"));
		setImagesX(2);
		setImagesY(2);
		setGravity(0, 0, 0);
		setStartColor(ColorRGBA.Yellow);
		setEndColor(ColorRGBA.Red);
		
		setStartSize(0.5f);
		setEndSize(5.0f);
		
		setLowLife(0.2f);
		setHighLife(0.2f);
		setInWorldSpace(false);
	}

}
