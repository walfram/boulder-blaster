package sandbox3.bblaster;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingSphere;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.instancing.InstancedNode;

import jme3.common.material.MtlLighting;
import jme3.common.mesh.FlatShaded;
import jme3utilities.math.noise.Generator;
import jme3utilities.mesh.Octasphere;
import sandbox3.bblaster.boulders.CtBoulderBounds;
import sandbox3.bblaster.boulders.CtBoulderHealth;
import sandbox3.bblaster.boulders.CtBoulderMove;
import sandbox3.bblaster.controls.CtCollision;
import sandbox3.bblaster.controls.CtPayload;
import sandbox3.bblaster.controls.CtTargettable;

@Deprecated
public final class StBoulders extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StBoulders.class);

	// private static final float minRadius = 10f;

	// private final Node boulders = new Node("boulders");
	private final InstancedNode boulders = new InstancedNode("boulders");

	private final Generator random = new Generator(42);

	private final AtomicInteger idx = new AtomicInteger(0);

	private Mesh mesh;

	private Material material;

	public StBoulders(Node rootNode) {
		rootNode.attachChild(boulders);
	}

	@Override
	protected void initialize(Application app) {

		mesh = new FlatShaded(new Octasphere(2, 1f)).mesh();
		mesh.setBound(new BoundingSphere(1f, new Vector3f()));

		material = new MtlLighting(app.getAssetManager(), ColorRGBA.Gray);
		material.setBoolean("UseInstancing", true);

		while (idx.incrementAndGet() < 1024) {
			Vector3f translation = random.nextVector3f().mult(4096f);
			Quaternion rotation = random.nextQuaternion();
			float radius = random.nextFloat(Settings.boulderMinRadius, Settings.boulderMaxRadius);

			createBoulder(idx.get(), translation, rotation, radius);
		}
		
		boulders.instance();

		logger.debug("initialized");

	}

	private void createBoulder(int idx, Vector3f translation, Quaternion rotation, float radius) {
		Geometry boulder = new Geometry("boulder@" + idx, mesh);
		boulder.setMaterial(material);

		boulder.setLocalTranslation(translation);

		boulder.setLocalRotation(rotation);

		boulder.setLocalScale(radius);

		boulders.attachChild(boulder);

		boulder.addControl(new CtBoulderHealth(radius));

		boulder.addControl(new CtBoulderMove(radius));
		boulder.addControl(new CtBoulderBounds(Settings.boundarySize));

		boulder.addControl(new CtTargettable());
		
		boulder.addControl(new CtCollision(other -> {
			CtPayload control = other.getControl(CtPayload.class);

			if (control != null) {

				boulder.getControl(CtBoulderHealth.class).applyDamage(control.value());
				if (boulder.getControl(CtBoulderHealth.class).isDead()) {
					boulder.removeFromParent();

					getState(StCollision.class).unregister(boulder);
					getState(StExplosion.class).boulderExplosion(boulder.getLocalTranslation());

					createFragments(radius, boulder.getLocalTranslation());
				}
			}
		}));
		getState(StCollision.class).register(boulder);

		// BoundsVisualizer boundsVisualizer = new BoundsVisualizer(app.getAssetManager());
		// boundsVisualizer.setSubject(boulder);
		// boulders.addControl(boundsVisualizer);
		// boundsVisualizer.setEnabled(true);
	}

	private void createFragments(float originalRadius, Vector3f originalTranslation) {
		float radius = originalRadius * 0.5f;

		if (radius < Settings.boulderMinRadius)
			return;

		// for (int i = 0; i < 2; i++) {
		Vector3f offset = random.nextUnitVector3f().mult(radius * 1.1f);
		Vector3f translation = originalTranslation.add(offset);

		Quaternion rotation = new Quaternion().lookAt(offset, Vector3f.UNIT_Z);
		createBoulder(idx.incrementAndGet(), translation, rotation, radius);

		createBoulder(idx.incrementAndGet(), originalTranslation.add(offset.negate()), rotation.opposite(), radius);
		
		boulders.instance();
		
		// }
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

	void findTargets(Ray ray, CollisionResults results) {
		boulders.collideWith(ray, results);
	}

}
