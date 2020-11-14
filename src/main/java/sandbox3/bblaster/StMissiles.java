package sandbox3.bblaster;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingSphere;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.FastMath;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import jme3.common.material.MtlShowNormals;
import jme3utilities.SimpleControl;
import jme3utilities.mesh.Cone;
import sandbox3.bblaster.controls.CtCollision;
import sandbox3.bblaster.controls.CtDamage;
import sandbox3.bblaster.controls.CtProjectileMove;
import sandbox3.bblaster.controls.CtMissileTarget;
import sandbox3.bblaster.controls.CtSmokeTrail;
import sandbox3.bblaster.controls.CtTargettable;
import sandbox3.bblaster.controls.CtTimeout;
import sandbox3.bblaster.effects.PeSmokeTrail;
import sandbox3.bblaster.missiles.CtMissileTrail;
import sandbox3.bblaster.missiles.NdMissile;
import sandbox3.bblaster.missiles.PeMissileTrail;

public final class StMissiles extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StMissiles.class);

	private final Node missiles = new Node("missiles");
	private final Node fx = new Node("missile-fx");

	public StMissiles(Node rootNode) {
		rootNode.attachChild(missiles);
		rootNode.attachChild(fx);
	}

	@Override
	protected void initialize(Application app) {
		logger.debug("initialized");
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

	@Deprecated
	void spawnMissile(Transform transform) {
		Geometry geometry = new Geometry("missile-geometry", new Cone(4, 2f, 5f, true));
		geometry.setMaterial(new MtlShowNormals(getApplication().getAssetManager()));
		geometry.rotate(FastMath.HALF_PI, 0, 0);

		geometry.setModelBound(new BoundingSphere(2.5f, new Vector3f()));

		Node missile = new Node("missile");
		missile.attachChild(geometry);

		missile.addControl(new CtDamage(new GameSettings().missileDamage()));
		missile.addControl(new CtProjectileMove(new GameSettings().missileSpeed()));

		missile.addControl(new CtMissileTarget(getState(StTargetting.class).currentTarget()));

		missile.addControl(new CtTimeout(15f, (spatial) -> {
			missile.removeFromParent();
			getState(StExplosion.class).missileExplosion(missile.getLocalTranslation());
			getState(StCollision.class).unregister(missile);
		}));

		missile.setLocalTransform(transform);

		missiles.attachChild(missile);

		PeSmokeTrail smokeTrail = new PeSmokeTrail(getApplication().getAssetManager());
		smokeTrail.addControl(new CtSmokeTrail(missile));
		fx.attachChild(smokeTrail);

		missile.addControl(new CtCollision(other -> {
			if (other.getControl(CtTargettable.class) != null) {
				missile.removeFromParent();
				getState(StCollision.class).unregister(missile);
				getState(StExplosion.class).missileExplosion(missile.getLocalTranslation());
			}
		}));

		getState(StCollision.class).register(missile);
	}

	public void spawnMissiles(List<Transform> transforms) {
		for (Transform t : transforms) {
			Node missile = new NdMissile(getApplication().getAssetManager());
			missile.setLocalTransform(t);
			missiles.attachChild(missile);

			missile.addControl(new SimpleControl() {
				float elapsed = 0f;

				@Override
				protected void controlUpdate(float updateInterval) {
					super.controlUpdate(updateInterval);

					elapsed += updateInterval;

					if (elapsed > 15f) {
						missileSelfDestruct(missile);
					}
				}
			});

			ParticleEmitter missileTrail = new PeMissileTrail(getApplication().getAssetManager());
			missileTrail.addControl(new CtMissileTrail(missile));
			fx.attachChild(missileTrail);
		}
	}

	protected void missileSelfDestruct(Spatial missile) {
		logger.debug("missile self-destruct = {}", missile);
		missile.removeFromParent();
		// TODO make explosion
	}

}
