package test.boulder.prepared2;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Mesh.Mode;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.util.BufferUtils;

import jme3.common.material.MtlLighting;
import jme3.common.mesh.FlatShaded;
import jme3utilities.debug.PointVisualizer;
import jme3utilities.mesh.Octasphere;

final class StBoulders extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StBoulders.class);

	private final Node scene = new Node("scene");

	public StBoulders(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Mesh mesh = new Octasphere(0, 100f);
		// Mesh mesh = new MBox(100, 100, 100, 3, 3, 3);

		Geometry geometry = new Geometry("test", new FlatShaded(mesh).mesh());
		// Geometry geometry = new Geometry("test", mesh);
		geometry.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray));
		geometry.getMaterial().getAdditionalRenderState().setWireframe(true);
		scene.attachChild(geometry);

		// geometry.move(0, 0, 250);

		logger.debug("mesh mode = {}", mesh.getMode());
		logger.debug("vertices = {}", mesh.getVertexCount());
		logger.debug("indices = {}", mesh.getIndexBuffer().size());

		FloatBuffer buffer = (FloatBuffer) mesh.getBuffer(Type.Position).getData();

		Vector3f[] positions = BufferUtils.getVector3Array(buffer);
		logger.debug("vector3Array size = {}", positions.length);
		logger.debug("unique vertices = {}", Stream.of(positions).collect(Collectors.toSet()).size());

		List<Vector3f> asList = Stream.of(positions).collect(Collectors.toList());
		Set<Vector3f> unique = Stream.of(positions).collect(Collectors.toSet());
		logger.debug("aslist = {}, unique = {}", asList.size(), unique.size());

		asList.removeAll(unique);
		logger.debug("shared = {}", asList);
		
		unique.forEach(v -> {
			PointVisualizer p = new PointVisualizer(app.getAssetManager(), 5, ColorRGBA.Yellow, null);
			p.setLocalTranslation(v);
			scene.attachChild(p);
		});

		IndexBuffer indexBuffer = mesh.getIndicesAsList();
		logger.debug("index buffer class = {}", indexBuffer.getClass());
		int[] indexArray = new int[indexBuffer.size()];
		for (int idx = 0; idx < indexBuffer.size(); idx++) {
			indexArray[idx] = indexBuffer.get(idx);
		}
		// logger.debug("indices = {}", indexArray);

		List<Vector3f> vertices = new ArrayList<>(indexArray.length);
		List<Vector3f> normals = new ArrayList<>(indexArray.length);
		List<Integer> indices = new ArrayList<>(indexArray.length);

		List<List<Integer>> partition = Lists.partition(Ints.asList(indexArray), 3);
		int idx = 0;
		for (List<Integer> tri : partition) {
			Triangle t = new Triangle(positions[tri.get(0)], positions[tri.get(1)], positions[tri.get(2)]);

			Vector3f normal = t.getNormal();

			float scalar = 10f;
			t.get1().addLocal(normal.mult(scalar));
			t.get2().addLocal(normal.mult(scalar));
			t.get3().addLocal(normal.mult(scalar));

			vertices.add(t.get1());
			vertices.add(t.get2());
			vertices.add(t.get3());

			normals.add(normal);
			normals.add(normal);
			normals.add(normal);

			indices.add(idx++);
			indices.add(idx++);
			indices.add(idx++);
		}

		logger.debug("vertices size = {}", vertices.size());
		logger.debug("normals size = {}", normals.size());
		logger.debug("indices size = {}", indices.size());

		Mesh m = new Mesh();
		m.setMode(Mode.Triangles);

		m.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices.stream().toArray(Vector3f[]::new)));
		m.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indices.stream().mapToInt(Integer::intValue).toArray()));

		m.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals.stream().toArray(Vector3f[]::new)));

		m.updateCounts();
		m.updateBound();

		Geometry g = new Geometry("g", m);
		g.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray));
		g.getMaterial().getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
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
