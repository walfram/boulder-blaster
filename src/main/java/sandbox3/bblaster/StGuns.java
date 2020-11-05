package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingSphere;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

import jme3.common.material.MtlShowNormals;
import sandbox3.bblaster.controls.CtCollision;
import sandbox3.bblaster.controls.CtDamage;
import sandbox3.bblaster.controls.CtMissileMove;
import sandbox3.bblaster.controls.CtTargettable;
import sandbox3.bblaster.controls.CtTimeout;

public final class StGuns extends BaseAppState {

	private final Node projectiles = new Node("projectiles");

	public StGuns(Node rootNode) {
		rootNode.attachChild(projectiles);
	}

	@Override
	protected void initialize(Application app) {
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

	void spawnProjectile(Transform transform) {
		Geometry geometry = new Geometry("projectile-geometry", new Box(0.25f, 0.25f, 1f));
		geometry.setMaterial(new MtlShowNormals(getApplication().getAssetManager()));

		geometry.setModelBound(new BoundingSphere(1f, new Vector3f()));

		Node projectile = new Node("projectile");
		projectile.attachChild(geometry);

		projectile.setLocalTransform(transform);

		projectile.addControl(new CtDamage(new GameSettings().projectileDamage()));
		projectile.addControl(new CtMissileMove(new GameSettings().projectileSpeed()));

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

}
