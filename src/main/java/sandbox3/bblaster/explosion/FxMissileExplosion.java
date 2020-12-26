package sandbox3.bblaster.explosion;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

public final class FxMissileExplosion extends Node {

	public FxMissileExplosion(AssetManager assetManager) {
		super("missile-explosion-fx");

		attachChild(new PeMissileFlash(assetManager, 1f));
		attachChild(new PeMissileSparks(assetManager, 1f));
	}

}
