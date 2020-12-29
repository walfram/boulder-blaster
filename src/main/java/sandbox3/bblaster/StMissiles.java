package sandbox3.bblaster;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.Transform;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;
import sandbox3.bblaster.misc.CtCollision;
import sandbox3.bblaster.weapons.CtDamagePayload;
import sandbox3.bblaster.weapons.CtMissileEngine;
import sandbox3.bblaster.weapons.CtMissileGuidance;
import sandbox3.bblaster.weapons.CtMissileTrail;
import sandbox3.bblaster.weapons.NdMissile;
import sandbox3.bblaster.weapons.PeMissileTrail;

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

	public void spawnMissiles(List<Transform> transforms) {
		for (Transform t : transforms) {
			Node missile = new NdMissile(getApplication().getAssetManager());
			missile.setLocalTransform(t);
			missiles.attachChild(missile);

			missile.addControl(new CtDamagePayload(Settings.missileDamage));
			missile.addControl(new CtMissileGuidance(getState(StTargetting.class).currentTarget()));
			missile.addControl(new CtMissileEngine(Settings.missileSpeed));

			missile.addControl(new SimpleControl() {
				float elapsed = 0f;

				@Override
				protected void controlUpdate(float updateInterval) {
					super.controlUpdate(updateInterval);

					elapsed += updateInterval;

					if (elapsed > 15f) {
						logger.debug("missile self-destruct = {}", missile);
						destroyMissile(missile);
					}
				}
			});

			missile.addControl(new CtCollision((other, collision) -> {
				logger.debug("missile {} hit = {}", missile, other);
				destroyMissile(missile);
			}));
			getState(StCollision.class).register(missile);

			// TODO missile trail offset
			ParticleEmitter missileTrail = new PeMissileTrail(getApplication().getAssetManager());
			missileTrail.addControl(new CtMissileTrail(missile));
			fx.attachChild(missileTrail);
		}
	}

	protected void destroyMissile(Spatial missile) {
		missile.removeFromParent();
		getState(StCollision.class).unregister(missile);
		getState(StExplosion.class).missileExplosion(missile.getLocalTranslation());
	}

}
