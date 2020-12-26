package sandbox3.bblaster.explosion;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

public final class FxBoulderExplosion extends Node {

	public FxBoulderExplosion(AssetManager assetManager) {
		super("boulder-explosion-fx");

		attachChild(new PeExplosionShockwave(assetManager, 1f));
		attachChild(new PeExplosionSparks(assetManager, 1f));
		attachChild(new PeExplosionFlash(assetManager, 1f));

	}

}
