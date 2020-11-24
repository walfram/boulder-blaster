package test.boulder;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import com.simsilica.event.EventBus;

import jme3.common.material.MtlLighting;
import jme3utilities.math.MyVector3f;
import jme3utilities.math.noise.Perlin2;
import jme3utilities.mesh.Octasphere;

final class StModifiedSphere extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StModifiedSphere.class);

	private final Node scene = new Node("scene");

	public StModifiedSphere(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {

		Mesh mesh = new Octasphere(5, 32f);

		logger.debug("vertices = {}", mesh.getVertexCount());
		logger.debug("triangles = {}", mesh.getTriangleCount());

		Vector2f[] uvs = BufferUtils.getVector2Array((FloatBuffer) mesh.getBuffer(Type.TexCoord).getData());
		logger.debug("uvs = {}", uvs.length);

		Vector3f[] positions = BufferUtils.getVector3Array((FloatBuffer) mesh.getBuffer(Type.Position).getData());
		logger.debug("positions length = {}", positions.length);

		long seed = -35_930_871;
		int fundamental = 10;
		Perlin2 generator = new Perlin2(fundamental, fundamental, seed, seed);

		Set<Integer> processed = new HashSet<>(positions.length);

		for (int idx = 0; idx < positions.length; idx++) {
			if (processed.contains(idx))
				continue;

			Vector3f v = positions[idx];

			List<Integer> indices = new ArrayList<>();
			for (int i = idx; i < positions.length; i++) {
				if (Objects.equals(v, positions[i]))
					indices.add(i);
			}

			float azm = MyVector3f.azimuth(v);// / FastMath.PI;
			float alt = MyVector3f.altitude(v);// / FastMath.HALF_PI;

			float e = 0f;
			int maxFreq = 5;
			for (int i = 0; i < maxFreq; i++) {
				float freq = FastMath.pow(2, i);
				// e += (1f / freq) * generator.sampleNormalized(freq * uvs[idx].x, freq * uvs[idx].y);
				// e += (1f / freq) * generator.sampleNormalized(freq * (v.x / 32f), freq * (v.y / 32f));
				e += (1f / freq) * generator.sampleNormalized(freq * azm, freq * alt);
			}

			Vector3f offset = v.normalize().mult(e * 10f);

			for (int i : indices) {
				positions[i].addLocal(offset);
			}

			processed.addAll(indices);
		}

		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(positions));

		// mesh.setMode(Mode.Points);

		Geometry geometry = new Geometry("test", mesh);
		// Geometry geometry = new Geometry("test", new FlatShaded(mesh).mesh());

		geometry.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray));
		// geometry.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray, "textures/uvtest.jpg"));

		// geometry.setMaterial(new MtlUnshaded(app.getAssetManager(), ColorRGBA.Gray));
		// geometry.getMaterial().setFloat("PointSize", 5);

		geometry.getMaterial().getAdditionalRenderState().setWireframe(true);
		// geometry.getMaterial().getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);

		scene.attachChild(geometry);

		EventBus.addListener(this, Events.toggleWireframe);
	}

	protected void toggleWireframe(Object o) {
		logger.debug("toggle wireframe event");

		scene.depthFirstTraversal(child -> {
			if (child instanceof Geometry) {
				RenderState rs = ((Geometry) child).getMaterial().getAdditionalRenderState();
				rs.setWireframe(!rs.isWireframe());
			}
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
