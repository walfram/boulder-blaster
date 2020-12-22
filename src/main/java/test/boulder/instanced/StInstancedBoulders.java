package test.boulder.instanced;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.instancing.InstancedNode;

import jme3.common.material.MtlLighting;
import jme3.common.mesh.FlatShaded;
import jme3.common.noise.FastNoiseLite;
import jme3.common.noise.FastNoiseLite.CellularReturnType;
import jme3.common.noise.FastNoiseLite.NoiseType;
import jme3utilities.debug.BoundsVisualizer;
import jme3utilities.math.noise.Generator;
import jme3utilities.mesh.Octasphere;
import sandbox3.bblaster.misc.NoisedMesh;

final class StInstancedBoulders extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StInstancedBoulders.class);

	private final InstancedNode scene = new InstancedNode("scene");

	private final Node bounds = new Node("bounds");

	public StInstancedBoulders(Node rootNode) {
		rootNode.attachChild(scene);
		rootNode.attachChild(bounds);
	}

	@Override
	protected void initialize(Application app) {
		float[] sizes = new float[] { 512f, 384f, 256f, 192f, 128f, 96f, 64f, 48f, 32f, 24f };

		List<Mesh> meshes = new ArrayList<>(sizes.length);

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

			Mesh flatshaded = new FlatShaded(noised).mesh();
			flatshaded.setBound(new BoundingSphere(size, new Vector3f()));
			logger.debug("flatshaded bound = {}", flatshaded.getBound());

			meshes.add(flatshaded);
		}

		Generator random = new Generator(47);

		Material material = new MtlLighting(app.getAssetManager(), ColorRGBA.Gray);
		material.setBoolean("UseInstancing", true);

		for (int i = 0; i < 2; i++) {
			Mesh mesh = meshes.get(i);
			float size = sizes[i];

			for (int idx = 0; idx < 32; idx++) {
				String name = String.format("boulder-%s-%s", size, idx);
				Geometry geometry = new Geometry(name, mesh);
				geometry.setMaterial(material);

				Vector3f translation = random.nextVector3f().mult(4096f);
				geometry.setLocalTranslation(translation);
				geometry.setLocalRotation(random.nextQuaternion());

				// geometry.addControl(new CtBoulderSpin(size));

				scene.attachChild(geometry);
			}
		}

		logger.debug("scene qty before instance = {}", scene.getQuantity());
		scene.instance();
		logger.debug("Scene qty after instance = {}", scene.getQuantity());

		scene.getChildren().stream().filter(s -> !s.getName().startsWith("boulder")).forEach(s -> {
			logger.debug("s = {}, bound = {}", s, s.getWorldBound());

			BoundsVisualizer boundsVisualizer = new BoundsVisualizer(app.getAssetManager());
			boundsVisualizer.setSubject(s);
			bounds.addControl(boundsVisualizer);
			boundsVisualizer.setEnabled(true);
		});

		for (int i = 0; i < 8; i++) {
			Geometry geometry = new Geometry("boulder", meshes.get(0));
			geometry.setMaterial(material);

			Vector3f translation = random.nextVector3f().mult(4096f);
			geometry.setLocalTranslation(translation);
			geometry.setLocalRotation(random.nextQuaternion());

			// geometry.addControl(new CtBoulderSpin(size));

			scene.attachChild(geometry);
		}
		
		logger.debug("scene qty before 2nd instance = {}", scene.getQuantity());
		scene.instance();
		logger.debug("Scene qty after 2nd instance = {}", scene.getQuantity());
		
		scene.getChildren().stream().filter(s -> !s.getName().startsWith("boulder")).forEach(s -> {
			logger.debug("s = {}, bound = {}", s, s.getWorldBound());
		});
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
