package sandbox3.bblaster;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import jme3.common.material.MtlShowNormals;
import jme3.common.material.MtlUnshaded;
import jme3utilities.SimpleControl;
import jme3utilities.mesh.Octasphere;
import sandbox3.bblaster.controls.CtCollision;
import sandbox3.bblaster.controls.CtDamage;
import sandbox3.bblaster.controls.CtProjectileMove;
import sandbox3.bblaster.controls.CtTargettable;
import sandbox3.bblaster.controls.CtTimeout;

public final class StBlasters extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StBlasters.class);

	private final Node projectiles = new Node("projectiles");

	private Mesh projectileMesh;
	private Material projectileMaterial;

	public StBlasters(Node rootNode) {
		rootNode.attachChild(projectiles);
	}

	@Override
	protected void initialize(Application app) {
		projectileMesh = new Octasphere(0, 1f);
		projectileMaterial = new MtlShowNormals(app.getAssetManager());
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

	@Override
	public void update(float tpf) {
		super.update(tpf);
	}

	@Deprecated
	void spawnProjectile(Transform transform) {
		Geometry geometry = new Geometry("projectile-geometry", new Box(0.25f, 0.25f, 1f));
		geometry.setMaterial(new MtlShowNormals(getApplication().getAssetManager()));

		geometry.setModelBound(new BoundingSphere(1f, new Vector3f()));

		Node projectile = new Node("projectile");
		projectile.attachChild(geometry);

		projectile.setLocalTransform(transform);

		projectile.addControl(new CtDamage(new GameSettings().projectileDamage()));
		projectile.addControl(new CtProjectileMove(new GameSettings().projectileSpeed()));

		projectile.addControl(new CtTimeout(5f, (spatial) -> {
			projectile.removeFromParent();
		}));

		projectiles.attachChild(projectile);

		projectile.addControl(new CtCollision(other -> {
			if (other.getControl(CtTargettable.class) != null) {
				projectile.removeFromParent();
				getState(StCollision.class).unregister(projectile);
				getState(StExplosion.class).projectileExplosion(projectile.getLocalTranslation());
			}
		}));

		getState(StCollision.class).register(projectile);
	}

	public void spawnProjectiles(List<Transform> transforms) {
		for (Transform t : transforms) {
			Spatial projectile = new Geometry("projectile", projectileMesh);
			projectile.setMaterial(projectileMaterial);
			
			// TODO temporary
			Vector3f direction = getState(StCrosshair.class).direction();
			t.getRotation().lookAt(direction, Vector3f.UNIT_Y);
			projectile.setLocalTransform(t);

			projectile.addControl(new CtProjectileMove(1000f));

			projectile.addControl(new SimpleControl() {
				float elapsed = 0f;

				@Override
				protected void controlUpdate(float updateInterval) {
					super.controlUpdate(updateInterval);
					elapsed += updateInterval;

					if (elapsed >= 5f)
						projectile.removeFromParent();
				}
			});

			projectiles.attachChild(projectile);
		}
	}

}
