package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingSphere;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import common.mtl.MtlShowNormals;
import jme3utilities.mesh.Octasphere;

final class StGuns extends BaseAppState {

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
		Geometry geometry = new Geometry("projectile-geometry", new Octasphere(1, 0.5f));
		geometry.setMaterial(new MtlShowNormals(getApplication().getAssetManager()));

		geometry.setModelBound(new BoundingSphere(0.5f, new Vector3f()));

		Node projectile = new Node("projectile");
		projectile.attachChild(geometry);

		projectile.setLocalTransform(transform);

		projectile.addControl(new CtDamage(new Const().projectileDamage()));
		projectile.addControl(new CtMissileMove(new Const().projectileSpeed()));

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
