package sandbox3.bblaster.ships;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import jme3utilities.debug.PointVisualizer;

public final class NdSpeederD extends Node {

	public NdSpeederD(AssetManager assetManager) {
		super("ship");

		Spatial hull = assetManager.loadModel("models/spacekit2/craft_speederD.obj");
		hull.scale(10f);
		attachChild(hull);

		PointVisualizer emissionLeft = new PointVisualizer(assetManager, 10, ColorRGBA.Blue, null);
		emissionLeft.setLocalTranslation(4, 4, -12);
		attachChild(emissionLeft);
		PointVisualizer emissionRight = new PointVisualizer(assetManager, 10, ColorRGBA.Blue, null);
		emissionRight.setLocalTranslation(-4, 4, -12);
		attachChild(emissionRight);
		addControl(new CtShipEmissions(emissionLeft, emissionRight));

		PointVisualizer blasterLeft = new PointVisualizer(assetManager, 10, ColorRGBA.Red, "saltire");
		blasterLeft.setLocalTranslation(14f, 2f, -1.5f);
		attachChild(blasterLeft);
		PointVisualizer blasterRight = new PointVisualizer(assetManager, 10, ColorRGBA.Green, "saltire");
		blasterRight.setLocalTranslation(-14f, 2f, -1.5f);
		attachChild(blasterRight);
		addControl(new CtShipBlasters(blasterLeft, blasterRight));

		PointVisualizer missileLeft = new PointVisualizer(assetManager, 10, ColorRGBA.Yellow, null);
		missileLeft.setLocalTranslation(7, 1, -2.5f);
		attachChild(missileLeft);
		addControl(new CtShipMissiles(missileLeft));
	}

}
