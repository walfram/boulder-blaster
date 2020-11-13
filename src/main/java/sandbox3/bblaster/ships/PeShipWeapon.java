package sandbox3.bblaster.ships;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import jme3.common.material.MtlParticle;

final class PeShipWeapon extends ParticleEmitter {

	public PeShipWeapon(AssetManager assetManager) {
		super("weapon", Type.Triangle, 8);

		setMaterial(new MtlParticle(assetManager, "Effects/Explosion/flash.png"));
		getMaterial().getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);

		setImagesX(2);
		setImagesY(2);

		setStartColor(ColorRGBA.Yellow);
		setEndColor(ColorRGBA.Red);
		setStartSize(2.25f);
		setEndSize(2.5f);
		setLowLife(0.09f);
		setHighLife(1f / 8.33f);
		getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_Z.clone());

		setInWorldSpace(false);
		setEnabled(false);
	}

}
