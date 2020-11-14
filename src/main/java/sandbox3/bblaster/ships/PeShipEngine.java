package sandbox3.bblaster.ships;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import jme3.common.material.MtlParticle;

final class PeShipEngine extends ParticleEmitter {

	public PeShipEngine(AssetManager assetManager) {
		super("engine", Type.Triangle, 50);
		setMaterial(new MtlParticle(assetManager, "Effects/Explosion/flame.png"));
		setImagesX(2);
		setImagesY(2);
		setGravity(0, 0, 0);
		setStartColor(ColorRGBA.White);
		setEndColor(ColorRGBA.Blue);
		setStartSize(2f);
		setEndSize(0.15f);
		setLowLife(0.5f);
		setHighLife(0.75f);
		
		// TODO tweak these two and setInWorldSpace
		getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_Z.negate().mult(50));
		getParticleInfluencer().setVelocityVariation(0.005f);
		
		setInWorldSpace(false);
		setEnabled(false);
	}

}
