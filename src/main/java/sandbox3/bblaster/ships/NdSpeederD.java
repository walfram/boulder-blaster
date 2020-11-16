package sandbox3.bblaster.ships;

import java.util.ArrayList;
import java.util.List;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import jme3utilities.debug.PointVisualizer;

public final class NdSpeederD extends Node {

	public NdSpeederD(AssetManager assetManager) {
		super("ship");

		Spatial hull = assetManager.loadModel("models/spacekit2/craft_speederD.obj");
		hull.scale(10f);
		attachChild(hull);

		PointVisualizer blasterLeft = new PointVisualizer(assetManager, 10, ColorRGBA.Red, "saltire");
		blasterLeft.setLocalTranslation(14f, 2f, -1.5f);
		attachChild(blasterLeft);

		PointVisualizer blasterRight = new PointVisualizer(assetManager, 10, ColorRGBA.Green, "saltire");
		blasterRight.setLocalTranslation(-14f, 2f, -1.5f);
		attachChild(blasterRight);

		List<ParticleEmitter> blasters = new ArrayList<>();
		for (Vector3f blasterTranslation : new Vector3f[] { new Vector3f(14f, 2f, -1.5f), new Vector3f(-14f, 2f, -1.5f) }) {
			ParticleEmitter blaster = new PeShipBlaster(assetManager);
			blaster.setLocalTranslation(blasterTranslation);
			attachChild(blaster);
			blasters.add(blaster);
		}

		addControl(new CtShipBlasters(blasters));

		PointVisualizer emissionLeft = new PointVisualizer(assetManager, 10, ColorRGBA.Blue, null);
		emissionLeft.setLocalTranslation(4, 4, -12);
		attachChild(emissionLeft);

		PointVisualizer emissionRight = new PointVisualizer(assetManager, 10, ColorRGBA.Blue, null);
		emissionRight.setLocalTranslation(-4, 4, -12);
		attachChild(emissionRight);

		List<ParticleEmitter> emissions = new ArrayList<>();
		for (Vector3f emissionTranslation : new Vector3f[] { new Vector3f(4, 4, -12), new Vector3f(-4, 4, -12) }) {
			ParticleEmitter emission = new PeShipEmission(assetManager);
			emission.setLocalTranslation(emissionTranslation);
			attachChild(emission);
			emissions.add(emission);
		}

		addControl(new CtShipEmissions(emissions));
		
		PointVisualizer missileLeft = new PointVisualizer(assetManager, 10, ColorRGBA.Yellow, null);
		missileLeft.setLocalTranslation(7, 1, -2.5f);
		attachChild(missileLeft);
		
		addControl(new CtShipMissiles(missileLeft));
	}

}
