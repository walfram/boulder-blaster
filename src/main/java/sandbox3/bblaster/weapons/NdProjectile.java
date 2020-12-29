package sandbox3.bblaster.weapons;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

import jme3utilities.debug.PointVisualizer;
import sandbox3.bblaster.Settings;

public final class NdProjectile extends Node {

	private final BoundingSphere bound;
	private final Geometry body;
	private final Vector3f origin = new Vector3f();

	public NdProjectile(AssetManager assetManager, Mesh mesh, Material material) {
		super("projectile");

		// float r = Floats.max(((BoundingBox) mesh.getBound()).getExtent(null).to/Array(null));
		float r = 0.25f;
		bound = new BoundingSphere(r, new Vector3f());

		body = new Geometry("projectile-body", mesh);
		body.setModelBound(bound);

		body.setMaterial(material);

		attachChild(body);

		PointVisualizer pointVisualizer = new PointVisualizer(assetManager, 3, ColorRGBA.Yellow, null);
		pointVisualizer.getMaterial().getAdditionalRenderState().setDepthTest(true);
		attachChild(pointVisualizer);

		// move
		addControl(new CtProjectileMove(Settings.projectileSpeed));
		addControl(new CtDamagePayload(Settings.projectileDamage));
		addControl(new CtProjectileTimeout(3f, s -> s.removeFromParent()));
	}

	@Override
	public void setLocalTranslation(Vector3f localTranslation) {
		super.setLocalTranslation(localTranslation);
		// TODO check if this gets called only once and not affected by above control's spatial.move call
		origin.set(localTranslation);
	}

}
