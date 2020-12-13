package test.projectile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingSphere;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

import jme3.common.material.MtlUnshaded;
import jme3utilities.SimpleControl;
import jme3utilities.debug.PointVisualizer;

public final class NdProjectile extends Node {

	private static final Logger logger = LoggerFactory.getLogger(NdProjectile.class);

	private final float projectileSpeed = 2500f;

	private final BoundingSphere bound;
	private final Geometry body;
	private final Vector3f origin = new Vector3f();

	public NdProjectile(AssetManager assetManager) {
		super("projectile");

		bound = new BoundingSphere(0.25f, new Vector3f());

		body = new Geometry("projectile-body", new Box(0.25f, 0.25f, 20f));
		body.setModelBound(bound);

		body.setMaterial(new MtlUnshaded(assetManager, ColorRGBA.Yellow));

		attachChild(body);

		PointVisualizer pointVisualizer = new PointVisualizer(assetManager, 3, ColorRGBA.Yellow, null);
		attachChild(pointVisualizer);

		// move
		addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);
				Vector3f forward = spatial.getLocalRotation().mult(Vector3f.UNIT_Z);
				spatial.move(forward.mult(projectileSpeed * updateInterval));
			}
		});

		addControl(new SimpleControl() {
			private float time = 0f;

			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				time += updateInterval;

				if (time > 3f) {
					logger.debug(
							"removing projectile, distance = {}, bound = {}",
							spatial.getLocalTranslation().distance(origin),
							body.getWorldBound());

					NdProjectile.this.removeFromParent();
				}
			}
		});
	}

	@Override
	public void setLocalTranslation(Vector3f localTranslation) {
		super.setLocalTranslation(localTranslation);
		// TODO check if this gets called only once and not affected by above control's spatial.move call
		origin.set(localTranslation);
	}

}
