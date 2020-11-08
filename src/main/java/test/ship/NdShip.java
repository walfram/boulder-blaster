package test.ship;

import java.util.ArrayList;
import java.util.List;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import jme3.common.material.MtlParticle;
import jme3utilities.debug.BoundsVisualizer;
import jme3utilities.debug.PointVisualizer;

final class NdShip extends Node {

	public NdShip(AssetManager assetManager) {
		super("ship");

		Spatial hull = assetManager.loadModel("models/spacekit2/craft_speederD.obj");
		hull.scale(10f);
		attachChild(hull);

		BoundsVisualizer boundsVisualizer = new BoundsVisualizer(assetManager);
		addControl(boundsVisualizer);
		boundsVisualizer.setSubject(hull);
		boundsVisualizer.setEnabled(true);

		PointVisualizer wpnLeft = new PointVisualizer(assetManager, 10, ColorRGBA.Red, "saltire");
		wpnLeft.setLocalTranslation(14f, 2f, -1.5f);
		attachChild(wpnLeft);

		PointVisualizer wpnRight = new PointVisualizer(assetManager, 10, ColorRGBA.Green, "saltire");
		wpnRight.setLocalTranslation(-14f, 2f, -1.5f);
		attachChild(wpnRight);

		List<ParticleEmitter> weapons = new ArrayList<>();
		for (Vector3f weaponTranslation : new Vector3f[] { new Vector3f(14f, 2f, -1.5f), new Vector3f(-14f, 2f, -1.5f) }) {
			ParticleEmitter weapon = new ParticleEmitter("weapon", Type.Triangle, 8);

			weapon.setMaterial(new MtlParticle(assetManager, "Effects/Explosion/flash.png"));
			weapon.setImagesX(2);
			weapon.setImagesY(2);

			weapon.setStartColor(ColorRGBA.Yellow);
			weapon.setEndColor(ColorRGBA.Red);
			weapon.setStartSize(2.25f);
			weapon.setEndSize(2.5f);
			weapon.setLowLife(0.09f);
			weapon.setHighLife(1f / 8.33f);
			weapon.getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_Z.clone());

			weapon.getMaterial().getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);

			weapon.setLocalTranslation(weaponTranslation);
			attachChild(weapon);

			weapons.add(weapon);
		}

		addControl(new CtShipWeapons(weapons));

		PointVisualizer engineLeft = new PointVisualizer(assetManager, 10, ColorRGBA.Blue, null);
		engineLeft.setLocalTranslation(4, 4, -12);
		attachChild(engineLeft);

		PointVisualizer engineRight = new PointVisualizer(assetManager, 10, ColorRGBA.Blue, null);
		engineRight.setLocalTranslation(-4, 4, -12);
		attachChild(engineRight);

		List<ParticleEmitter> engines = new ArrayList<>();
		for (Vector3f engineTranslation : new Vector3f[] { new Vector3f(4, 4, -12), new Vector3f(-4, 4, -12) }) {
			ParticleEmitter engine = new ParticleEmitter("engine", Type.Triangle, 100);

			engine.setMaterial(new MtlParticle(assetManager, "Effects/Explosion/flame.png"));
			engine.setImagesX(2);
			engine.setImagesY(2);
			engine.setGravity(0, 0, 0);
			engine.setStartColor(ColorRGBA.White);
			engine.setEndColor(ColorRGBA.Blue);
			engine.setStartSize(2f);
			engine.setEndSize(0.15f);
			engine.setLowLife(0.5f);
			engine.setHighLife(0.75f);
			engine.getParticleInfluencer().setInitialVelocity(Vector3f.UNIT_Z.negate().mult(50));
			engine.getParticleInfluencer().setVelocityVariation(0.005f);

			engine.setLocalTranslation(engineTranslation);
			attachChild(engine);

			engines.add(engine);
		}

		addControl(new CtShipEngines(engines));
	}

}
