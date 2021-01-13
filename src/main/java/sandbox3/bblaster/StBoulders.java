package sandbox3.bblaster;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingSphere;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.instancing.InstancedNode;
import com.jme3.util.BufferUtils;

import jme3.common.material.MtlLighting;
import jme3.common.mesh.FlatShaded;
import jme3.common.noise.FastNoiseLite;
import jme3.common.noise.FastNoiseLite.CellularReturnType;
import jme3.common.noise.FastNoiseLite.NoiseType;
import jme3utilities.math.noise.Generator;
import jme3utilities.mesh.Octasphere;
import sandbox3.bblaster.boulders.CtBoulderBounds;
import sandbox3.bblaster.boulders.CtBoulderHealth;
import sandbox3.bblaster.boulders.CtBoulderMove;
import sandbox3.bblaster.misc.CtCollision;
import sandbox3.bblaster.misc.NoisedMesh;
import sandbox3.bblaster.weapons.CtDamagePayload;

public final class StBoulders extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StBoulders.class);

	private final InstancedNode scene = new InstancedNode("scene");

	private final Generator random = new Generator(1337);

	// private final float[] sizes = new float[] { 512f, 384f, 256f, 192f, 128f, 96f, 64f, 48f, 32f, 24f };
	// use less meshes, or it quickly gets fps < 20
	private final float[] sizes = new float[] { 512f, 384f, 256f, 192f, 128f, 64f, 32f, 16f };

	private final List<Mesh> meshes = new ArrayList<>(sizes.length);

	private Material material;

	private int idx = 0;

	private final float maxDistance = Settings.boulderSpawnDistance;

	public StBoulders(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		// mesh = new FlatShaded(new Octasphere(1, 1f)).mesh();
		// mesh.setBound(new BoundingSphere(1f, new Vector3f()));

		FastNoiseLite noise = new FastNoiseLite();
		noise.SetNoiseType(NoiseType.Cellular);
		noise.SetCellularReturnType(CellularReturnType.CellValue);
		float frequency = 0.001f;

		for (float size : sizes) {
			Mesh source = new Octasphere(4, size);

			float noiseScale = Math.max(10f, size / 8f);
			frequency += 0.002f;
			noise.SetFrequency(frequency);

			Mesh noised = new NoisedMesh(source, noise, noiseScale).mesh();

			Vector3f[] positions = BufferUtils.getVector3Array((FloatBuffer) noised.getBuffer(Type.Position).getData());
			double radius = Arrays.stream(positions).mapToDouble(v -> v.length()).max().getAsDouble();

			Mesh flatshaded = new FlatShaded(noised).mesh();

			flatshaded.setBound(new BoundingSphere((float) radius, new Vector3f()));
			logger.debug("flatshaded bound = {}", flatshaded.getBound());

			meshes.add(flatshaded);
		}

		material = new MtlLighting(app.getAssetManager(), ColorRGBA.Gray);
		material.setBoolean("UseInstancing", true);

		// float size = sizes[0];
		int sizeIdx = 0;

		for (int i = 0; i < 16; i++) {

			Geometry boulder = createBoulder(idx++, sizeIdx);

			Vector3f translation = random.nextUnitVector3f().mult(maxDistance);
			BoundingSphere b = new BoundingSphere(sizes[sizeIdx], translation);

			while (scene.getChildren().stream().filter(s -> s.getWorldBound().intersects(b)).findFirst().isPresent()) {
				translation = random.nextUnitVector3f().mult(maxDistance);
				b.setCenter(translation);
			}

			Quaternion rotation = random.nextQuaternion();

			boulder.setLocalTranslation(translation);
			boulder.setLocalRotation(rotation);

			scene.attachChild(boulder);
		}

		scene.instance();

		scene.getChildren().forEach(s -> {
			logger.debug("child = {}, bound = {}", s.getName(), s.getWorldBound());
		});
	}

	private Geometry createBoulder(int idx, int sizeIdx) {
		Geometry boulder = new Geometry("boulder#" + idx, meshes.get(sizeIdx));
		boulder.setMaterial(material);

		// boulder.setLocalScale(size);
		float size = sizes[sizeIdx];

		boulder.addControl(new CtBoulderMove(size));
		boulder.addControl(new CtBoulderBounds(Settings.boulderBoundarySize));
		boulder.addControl(new CtBoulderHealth(size));

		boulder.addControl(new CtCollision((other, collision) -> {
			CtBoulderHealth otherHealth = other.getControl(CtBoulderHealth.class);
			if (otherHealth != null) {
				// logger.debug(
				// "boulder collision = {} (health = {}, size = {}) vs {} (health = {}, size = {})",
				// boulder,
				// boulder.getControl(CtBoulderHealth.class).value(),
				// boulder.getControl(CtBoulderHealth.class).max(),
				// other,
				// otherHealth.value(),
				// otherHealth.max());
				boulder.getControl(CtBoulderHealth.class).applyDamage(otherHealth.max());
			}

			CtDamagePayload otherPayload = other.getControl(CtDamagePayload.class);
			if (otherPayload != null) {
				boulder.getControl(CtBoulderHealth.class).applyDamage(otherPayload.value());
			}

			if (boulder.getControl(CtBoulderHealth.class).isDead()) {
				boulder.removeFromParent();
				getState(StCollision.class).unregister(boulder);
				// logger.debug("destroyed boulder = {}", boulder);
				getState(StExplosion.class).boulderExplosion(boulder.getLocalTranslation(), size);
				spawnFragments(sizeIdx + 1, boulder.getLocalTranslation(), collision);
				scene.instance();
			}
		}));
		getState(StCollision.class).register(boulder);

		return boulder;
	}

	private void spawnFragments(int sizeIdx, Vector3f originalTranslation, CollisionResult collision) {

		// float radius = 0.75f * originalSize;
		// float radius = 0.625f * originalSize;

		// if (radius < Settings.boulderMinRadius)
		// return;

		if (sizeIdx > (sizes.length - 1))
			return;

		Vector3f offset = random.nextUnitVector3f().mult(sizes[sizeIdx] * 1.5f);

		Vector3f translation = originalTranslation.add(offset);
		Quaternion rotation = new Quaternion().lookAt(offset, Vector3f.UNIT_Z);

		Geometry fragmenta = createBoulder(idx++, sizeIdx);
		fragmenta.setLocalTranslation(translation);
		fragmenta.setLocalRotation(rotation);
		scene.attachChild(fragmenta);

		Geometry fragmentb = createBoulder(idx++, sizeIdx);
		fragmentb.setLocalTranslation(originalTranslation.add(offset.negate()));
		fragmentb.setLocalRotation(rotation.opposite());
		scene.attachChild(fragmentb);

		// scene.instance();

		// scene.getChildren().forEach(s -> {
		// logger.debug("child = {}, bound = {}", s.getName(), s.getWorldBound());
		// });
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

	public void findTargets(Ray ray, CollisionResults results) {
		// filter out instanced meshes, check only "real" boulders
		List<Spatial> boulders = scene.getChildren().stream().filter(spatial -> {
			return spatial.getControl(CtBoulderHealth.class) != null;
		}).collect(Collectors.toList());

		boulders.forEach(boulder -> boulder.collideWith(ray, results));
	}

	public int boulderQuantity() {
		return scene.getQuantity() - 1;
	}

}
