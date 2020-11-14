package sandbox3.bblaster;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.instancing.InstancedNode;

import jme3.common.material.MtlLighting;
import jme3.common.mesh.FlatShaded;
import jme3utilities.math.noise.Generator;
import jme3utilities.mesh.Octasphere;
import sandbox3.bblaster.boulders.CtBoulderMove;
import sandbox3.bblaster.boulders.CtBoulderBounds;

public final class StBoulders2 extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StBoulders2.class);

	private final InstancedNode scene = new InstancedNode("scene");

	private final Generator random = new Generator(1337);

	private Mesh mesh;
	private Material material;

	private float elapsed = 0f;
	private int idx = 0;

	private final int maxBoulders = 2048;
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

		// for (int idx = 0; idx < 2048; idx++) {
		// spawnBoulder(idx);
		// }

		// scene.instance();
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

		scene.attachChild(boulder);

		logger.debug("spawned boulder = {}, translation = {}, scale = {}", boulder, translation, size);
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

}
