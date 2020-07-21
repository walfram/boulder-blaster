package common.mtl;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;

public final class MtlParticle extends Material {

	public MtlParticle(AssetManager assetManager, String texturePath) {
		super(assetManager, "Common/MatDefs/Misc/Particle.j3md");

		setTexture("Texture", assetManager.loadTexture(texturePath));
	}

}
