package sandbox3.bblaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingSphere;
import com.jme3.math.FastMath;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import jme3utilities.mesh.Cone;
import sandbox3.bblaster.controls.CtCollision;
import sandbox3.bblaster.controls.CtDamage;
import sandbox3.bblaster.controls.CtMissileMove;
import sandbox3.bblaster.controls.CtMissileTarget;
import sandbox3.bblaster.controls.CtSmokeTrail;
import sandbox3.bblaster.controls.CtTargettable;
import sandbox3.bblaster.controls.CtTimeout;
import sandbox3.bblaster.effects.PeSmokeTrail;
import sandbox3.bblaster.materials.MtlShowNormals;

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

	void spawnMissile(Transform transform) {
		Geometry geometry = new Geometry("missile-geometry", new Cone(4, 2f, 5f, true));
		geometry.setMaterial(new MtlShowNormals(getApplication().getAssetManager()));
		geometry.rotate(FastMath.HALF_PI, 0, 0);

		geometry.setModelBound(new BoundingSphere(2.5f, new Vector3f()));

		Node missile = new Node("missile");
		missile.attachChild(geometry);

		// BoundsVisualizer boundsVisualizer = new BoundsVisualizer(getApplication().getAssetManager());
		// boundsVisualizer.setSubject(geometry);
		// missile.addControl(boundsVisualizer);
		// boundsVisualizer.setEnabled(true);

		missile.addControl(new CtDamage(new GameSettings().missileDamage()));
		missile.addControl(new CtMissileMove(new GameSettings().missileSpeed()));

		missile.addControl(new CtMissileTarget(getState(StTargetting.class).currentTarget()));
		
		missile.addControl(new CtTimeout(15f, (spatial) -> {
			missile.removeFromParent();
			getState(StExplosion.class).missileExplosion(missile.getLocalTranslation());
			getState(StCollision.class).unregister(missile);
		}));

		// missile.setLocalTranslation(transform.getTranslation());
		// missile.setLocalRotation(transform.getRotation());
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

}
