package test.boulder.prepared2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.util.BufferUtils;

import jme3.common.material.MtlLighting;
import jme3.common.noise.FastNoiseLite;
import jme3.common.noise.FastNoiseLite.FractalType;
import jme3.common.noise.FastNoiseLite.NoiseType;
import jme3utilities.math.noise.Generator;
import jme3utilities.mesh.Octasphere;

final class StPunchedBoulder extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StPunchedBoulder.class);

	private final Node scene = new Node("scene");
	private final Generator random = new Generator(1337);

	public StPunchedBoulder(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		float radius = 100f;
		Mesh mesh = new Octasphere(3, radius);
		// Geometry geometry = new Geometry("test", mesh);
		// geometry.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray));
		// geometry.getMaterial().getAdditionalRenderState().setWireframe(true);
		// scene.attachChild(geometry);

		FastNoiseLite noise = new FastNoiseLite();
		noise.SetNoiseType(NoiseType.Perlin);
		noise.SetFractalType(FractalType.FBm);

		Vector3f[] vertices = BufferUtils.getVector3Array(mesh.getFloatBuffer(Type.Position));
		logger.debug("vertices.size = {}", vertices.length);

		IndexBuffer indexBuffer = mesh.getIndicesAsList();
		int[] indices = new int[indexBuffer.size()];
		for (int i = 0; i < indexBuffer.size(); i++) {
			indices[i] = indexBuffer.get(i);
		}
		logger.debug("indices.size = {}", indices.length);

		Stream.of(vertices).map(Vector3f::new).forEach(u -> {
			float e = noise.GetNoise(u.x, u.y, u.z);
			e *= radius * 0.5f;
			Vector3f delta = u.normalize().mult(e);

			for (Vector3f v : vertices) {
				if (v.equals(u))
					v.addLocal(delta);
			}
		});

		List<Face> faces = new ArrayList<>(indices.length / 3);
		for (int idx = 0; idx < indices.length / 3; idx++) {
			Vector3f v1 = vertices[indices[idx * 3 + 0]];
			Vector3f v2 = vertices[indices[idx * 3 + 1]];
			Vector3f v3 = vertices[indices[idx * 3 + 2]];

			Face face = new Face(v1, v2, v3);
			faces.add(face);
		}

		int punches = 4;
		for (int punch = 0; punch < punches; punch++) {
			int randomIdx = random.nextInt(vertices.length);
			Vector3f vertex = vertices[randomIdx];
			logger.debug("random idx = {}, random vertex = {}", randomIdx, vertex);

			List<Vector3f> sorted = new ArrayList<>();
			sorted.addAll(Arrays.asList(vertices));
			Collections.sort(sorted, (o1, o2) -> Float.compare(o1.distance(vertex), o2.distance(vertex)));

			logger.debug("sorted size = {}", sorted.size());

			// why not 1st 16?
			int m = (int) (vertices.length / 8);
			logger.debug("magic m = {}", m);
			for (int idx = 0; idx < m; idx++) {
				// Vector3f v = sorted.pollFirst();
				Vector3f v = sorted.get(idx);
				float scale = 1f - (((float) m - idx) / (2f * m));
				logger.debug("v {} = {}, scale = {}", v, idx, scale);
				faces.stream().filter(f -> f.contains(v)).forEach(f -> f.vertex(v).multLocal(scale));
			}
		}

		Geometry g = new Geometry("g", new FaceMesh(faces).mesh());
		g.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray));
		scene.attachChild(g);
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
