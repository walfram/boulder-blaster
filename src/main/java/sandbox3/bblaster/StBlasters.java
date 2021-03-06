package sandbox3.bblaster;

import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

import jme3.common.material.MtlUnshaded;
import sandbox3.bblaster.misc.CtCollision;
import sandbox3.bblaster.weapons.NdProjectile;

public final class StBlasters extends BaseAppState {

	private final Node projectiles = new Node("projectiles");

	private Mesh projectileMesh;
	private Material projectileMaterial;

	public StBlasters(Node rootNode) {
		rootNode.attachChild(projectiles);
	}

	@Override
	protected void initialize(Application app) {
		// projectileMesh = new Octasphere(0, 1f);
		projectileMesh = new Box(0.25f, 0.25f, 1.5f);
		// projectileMaterial = new MtlShowNormals(app.getAssetManager());
		projectileMaterial = new MtlUnshaded(app.getAssetManager(), ColorRGBA.Yellow);

		// projectileMaterial.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		// projectileMaterial.getAdditionalRenderState().setDepthTest(false);
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

	public void spawnProjectiles(List<Transform> transforms) {
		for (Transform t : transforms) {
			// Spatial projectile = new Geometry("projectile", projectileMesh);
			// projectile.setMaterial(projectileMaterial);

			Node projectile = new NdProjectile(getApplication().getAssetManager(), projectileMesh, projectileMaterial);

			// TODO temporary
			Vector3f direction = getState(StHudCrosshair.class).direction();
			t.getRotation().lookAt(direction, Vector3f.UNIT_Y);
			projectile.setLocalTransform(t);
			projectile.scale(1f, 1f, 10f);

			// projectile.addControl(new CtProjectileMove(Settings.projectileSpeed));
			// projectile.addControl(new CtDamagePayload(Settings.projectileDamage));

			// projectile.addControl(new SimpleControl() {
			// float elapsed = 0f;
			//
			// @Override
			// protected void controlUpdate(float updateInterval) {
			// super.controlUpdate(updateInterval);
			// elapsed += updateInterval;
			//
			// if (elapsed >= 5f)
			// projectile.removeFromParent();
			// }
			// });

			projectile.addControl(new CtCollision((other, collision) -> {
				projectile.removeFromParent();
				getState(StCollision.class).unregister(projectile);
				getState(StExplosion.class).projectileExplosion(projectile.getLocalTranslation());
			}));
			getState(StCollision.class).register(projectile);

			projectiles.attachChild(projectile);
		}
	}

}
