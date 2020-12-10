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

public final class StBoulders extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StBoulders.class);

	private final InstancedNode scene = new InstancedNode("scene");

	private final Generator random = new Generator(1337);

	private Mesh mesh;
	private Material material;

	private float elapsed = 0f;
	private int idx = 0;

	private final int maxBoulders = Settings.bouldersTotal;
	private final float maxDistance = Settings.boulderSpawnDistance;
	private final float minScale = Settings.boulderMinRadius;
	private final float maxScale = Settings.boulderMaxRadius;

	public StBoulders(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		mesh = new FlatShaded(new Octasphere(1, 1f)).mesh();
		mesh.setBound(new BoundingSphere(1f, new Vector3f()));

		material = new MtlLighting(app.getAssetManager(), ColorRGBA.Gray);
		material.setBoolean("UseInstancing", true);

		for (int i = 0; i < 16; i++) {
			spawnBoulder2(idx++);
		}

		scene.instance();
	}

	// @Override
	// public void update(float tpf) {
	// super.update(tpf);

	// if (scene.getQuantity() > maxBoulders)
	// return;
	//
	// if (idx > maxBoulders)
	// return;
	//
	// elapsed += tpf;
	//
	// if (elapsed < 0.1f)
	// return;
	//
	// elapsed = 0f;
	//
	// spawnBoulder(idx++);
	// scene.instance();
	// }

	private void spawnBoulder2(int idx) {
		Geometry boulder = new Geometry("boulder#" + idx, mesh);
		boulder.setMaterial(material);

		float size = 512f;
		Vector3f translation = random.nextUnitVector3f().mult(maxDistance);
		BoundingSphere b = new BoundingSphere(size, translation);
		logger.debug("b.volume = {}", String.format("%10.2f", b.getVolume()));

		while (scene.getChildren().stream().filter(s -> s.getWorldBound().intersects(b)).findFirst().isPresent()) {
			translation = random.nextUnitVector3f().mult(maxDistance);
			b.setCenter(translation);
		}

		Quaternion rotation = random.nextQuaternion();

		boulder.setLocalTranslation(translation);
		boulder.setLocalRotation(rotation);
		boulder.setLocalScale(size);

		boulder.addControl(new CtBoulderMove(size));
		boulder.addControl(new CtBoulderBounds(Settings.boulderBoundarySize));
		boulder.addControl(new CtBoulderHealth(size));

		boulder.addControl(new CtCollision(other -> {
			CtPayload control = other.getControl(CtPayload.class);

			if (control != null) {
				boulder.getControl(CtBoulderHealth.class).applyDamage(control.value());
				if (boulder.getControl(CtBoulderHealth.class).isDead()) {
					boulder.removeFromParent();
					getState(StCollision.class).unregister(boulder);
					logger.debug("destroyed boulder = {}", boulder);
					getState(StExplosion.class).boulderExplosion(boulder.getLocalTranslation(), size);
					createFragments(size, boulder.getLocalTranslation());
					// scene.instance();
				}
			}
		}));
		getState(StCollision.class).register(boulder);

		scene.attachChild(boulder);
	}

	private void spawnBoulder(int idx) {
		float size = random.nextFloat(minScale, maxScale);

		Geometry boulder = newBoulder(idx, size);

		Vector3f translation = random.nextUnitVector3f().mult(maxDistance);
		Quaternion rotation = random.nextQuaternion();

		boulder.setLocalTranslation(translation);
		boulder.setLocalRotation(rotation);

		scene.attachChild(boulder);

	}

	private Geometry newBoulder(int idx, float size) {
		Geometry boulder = new Geometry("boulder#" + idx, mesh);
		boulder.setMaterial(material);

		boulder.setLocalScale(size);

		boulder.addControl(new CtBoulderMove(size));
		boulder.addControl(new CtBoulderBounds(Settings.boulderBoundarySize));
		boulder.addControl(new CtBoulderHealth(size));

		boulder.addControl(new CtCollision(other -> {
			CtPayload control = other.getControl(CtPayload.class);

			if (control != null) {
				boulder.getControl(CtBoulderHealth.class).applyDamage(control.value());
				if (boulder.getControl(CtBoulderHealth.class).isDead()) {
					boulder.removeFromParent();
					getState(StCollision.class).unregister(boulder);
					logger.debug("destroyed boulder = {}", boulder);
					getState(StExplosion.class).boulderExplosion(boulder.getLocalTranslation(), size);
					createFragments(size, boulder.getLocalTranslation());
					// scene.instance();
				}
			}
		}));
		getState(StCollision.class).register(boulder);
		return boulder;
	}

	private void createFragments(float originalSize, Vector3f originalTranslation) {

		float radius = 0.75f * originalSize;

		if (radius < Settings.boulderMinRadius)
			return;

		Vector3f offset = random.nextUnitVector3f().mult(radius * 1.1f);
		Vector3f translation = originalTranslation.add(offset);
		Quaternion rotation = new Quaternion().lookAt(offset, Vector3f.UNIT_Z);

		Geometry fragmenta = newBoulder(idx++, radius);
		fragmenta.setLocalTranslation(translation);
		fragmenta.setLocalRotation(rotation);
		scene.attachChild(fragmenta);

		Geometry fragmentb = newBoulder(idx++, radius);
		fragmentb.setLocalTranslation(originalTranslation.add(offset.negate()));
		fragmentb.setLocalRotation(rotation.opposite());
		scene.attachChild(fragmentb);

		scene.instance();

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

	public int boulderQuantity() {
		return scene.getQuantity() - 1;
	}

}
