package sandbox3.bblaster.player;

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

		// blaster fx, left + right
		PointVisualizer blasterFxLeft = new PointVisualizer(assetManager, 10, ColorRGBA.Red, "saltire");
		blasterFxLeft.setLocalTranslation(14f, 2f, -1.5f);
		attachChild(blasterFxLeft);

		PointVisualizer blasterFxRight = new PointVisualizer(assetManager, 10, ColorRGBA.Green, "saltire");
		blasterFxRight.setLocalTranslation(-14f, 2f, -1.5f);
		attachChild(blasterFxRight);

		addControl(new CtShipBlasterFx(blasterFxLeft, blasterFxRight));

		// projectile spawn point left + right
		PointVisualizer projectileSpawnLeft = new PointVisualizer(assetManager, 10, ColorRGBA.Yellow, "cross");
		projectileSpawnLeft.setLocalTranslation(14f, 2f, 10f);
		projectileSpawnLeft.getMaterial().getAdditionalRenderState().setDepthTest(true);
		attachChild(projectileSpawnLeft);

		PointVisualizer projectileSpawnRight = new PointVisualizer(assetManager, 10, ColorRGBA.White, "cross");
		projectileSpawnRight.setLocalTranslation(-14f, 2f, 10f);
		projectileSpawnRight.getMaterial().getAdditionalRenderState().setDepthTest(true);
		attachChild(projectileSpawnRight);

		addControl(new CtShipBlasterProjectiles(projectileSpawnLeft, projectileSpawnRight));

		PointVisualizer missileLeft = new PointVisualizer(assetManager, 10, ColorRGBA.Yellow, null);
		missileLeft.setLocalTranslation(7, 1, -2.5f);
		attachChild(missileLeft);
		addControl(new CtShipMissiles(missileLeft));
	}

}
