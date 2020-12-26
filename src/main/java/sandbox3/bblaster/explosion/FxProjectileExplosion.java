package sandbox3.bblaster.explosion;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

public final class FxProjectileExplosion extends Node {

	public FxProjectileExplosion(AssetManager assetManager) {
		super("projectile-explosion-fx");

		attachChild(new PeProjectileSparks(assetManager, 20f));
	}

}
