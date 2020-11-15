package sandbox3.bblaster;

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

public final class StBoulders2 extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StBoulders2.class);

	private final InstancedNode scene = new InstancedNode("scene");

	private final Generator random = new Generator(1337);

	private Mesh mesh;
	private Material material;

	private float elapsed = 0f;
	private int idx = 0;

	private final int maxBoulders = 1024;
	private final float minDistance = 1024f;
	private final float maxDistance = 4096f;
	private final float minScale = 10f;
	private final float maxScale = 100f;

	public StBoulders2(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		mesh = new FlatShaded(new Octasphere(1, 1f)).mesh();
		mesh.setBound(new BoundingSphere(1f, new Vector3f()));

		material = new MtlLighting(app.getAssetManager(), ColorRGBA.Gray);
		material.setBoolean("UseInstancing", true);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		if (scene.getQuantity() > maxBoulders)
			return;

		elapsed += tpf;

		if (elapsed < 0.1f)
			return;

		elapsed = 0f;

		spawnBoulder(idx++);
		scene.instance();
	}

	private void spawnBoulder(int idx) {
		Geometry boulder = new Geometry("boulder#" + idx, mesh);
		boulder.setMaterial(material);

		Vector3f translation = random.nextUnitVector3f().mult(maxDistance);
		Quaternion rotation = random.nextQuaternion();
		float size = random.nextFloat(minScale, maxScale);

		boulder.setLocalTranslation(translation);
		boulder.setLocalRotation(rotation);
		boulder.setLocalScale(size);

		boulder.addControl(new CtBoulderMove(size));
		boulder.addControl(new CtBoulderBounds(5000f));
		boulder.addControl(new CtBoulderHealth(size));

		boulder.addControl(new CtCollision(other -> {
			CtPayload control = other.getControl(CtPayload.class);

			if (control != null) {
				boulder.getControl(CtBoulderHealth.class).applyDamage(control.value());
				if (boulder.getControl(CtBoulderHealth.class).isDead()) {
					boulder.removeFromParent();
					getState(StCollision.class).unregister(boulder);
					logger.debug("destroyed boulder = {}", boulder);
					getState(StExplosion.class).boulderExplosion(boulder.getLocalTranslation());
					createFragments(size, boulder.getLocalTranslation());
					// scene.instance();
				}
			}
		}));
		getState(StCollision.class).register(boulder);

		scene.attachChild(boulder);

		// logger.debug("spawned boulder = {}, translation = {}, scale = {}", boulder, translation, size);
	}

	private void createFragments(float size, Vector3f localTranslation) {
	}

	// private void createFragments(float originalRadius, Vector3f originalTranslation) {
	// float radius = originalRadius * 0.5f;
	//
	// if (radius < Settings.boulderMinRadius)
	// return;
	//
	// Vector3f offset = random.nextUnitVector3f().mult(radius * 1.1f);
	// Vector3f translation = originalTranslation.add(offset);
	//
	// Quaternion rotation = new Quaternion().lookAt(offset, Vector3f.UNIT_Z);
	// createBoulder(idx.incrementAndGet(), translation, rotation, radius);
	//
	// createBoulder(idx.incrementAndGet(), originalTranslation.add(offset.negate()), rotation.opposite(), radius);
	//
	// boulders.instance();
	// }

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

	public void findTargets(Ray ray, CollisionResults results) {
		scene.collideWith(ray, results);
	}

}
