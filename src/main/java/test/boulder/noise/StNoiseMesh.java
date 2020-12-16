package test.boulder.noise;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.TextureKey;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Mesh.Mode;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;

import jme3.common.material.MtlLighting;
import jme3utilities.math.noise.Perlin2;

final class StNoiseMesh extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StNoiseMesh.class);

	private final Node scene = new Node("scene");

	public StNoiseMesh(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		int size = 256;

		Perlin2 perlin2 = new Perlin2(1024, 32, 10, 10);

		// Vector3f[] vertices = new Vector3f[size * size];

		float[][] heightmap = new float[size][size];
		Vector2f[] uvs = new Vector2f[size * size];

		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {

				float u = (float) x / size;
				float v = (float) z / size;

				float e = 0f;

				for (int octave = 0; octave < 8; octave++) {
					float freq = FastMath.pow(2, octave);
					e += (1f / freq) * perlin2.sampleNormalized(freq * u, freq * v);
				}

				heightmap[x][z] = Math.max(0, e);
				uvs[x * size + z] = new Vector2f(u, v);
			}
		}

		Vector3f[] positions = new Vector3f[size * size];
		int[] indices = new int[(size - 1) * (size - 1) * 6];
		logger.debug("indices.length = {}", indices.length);

		int idx = 0;

		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				// float y = FastMath.pow(heightmap[x][z], 0.5f) * 20f;
				float y = heightmap[x][z] * 20f;
				Vector3f v = new Vector3f(x, y, z).add(-size * 0.5f, 0, -size * 0.5f);
				positions[x * size + z] = v;

				if (x != (size - 1) && z != (size - 1)) {
					indices[idx] = x * size + z;
					indices[idx + 1] = x * size + z + size + 1;
					indices[idx + 2] = x * size + z + size;

					indices[idx + 3] = x * size + z;
					indices[idx + 4] = x * size + z + 1;
					indices[idx + 5] = x * size + z + size + 1;

					idx += 6;
				}
			}
		}

		Vector3f[] normals = new Vector3f[size * size];
		Arrays.setAll(normals, index -> new Vector3f());

		Triangle t = new Triangle();
		for (int i = 0; i < (indices.length / 3); i++) {
			int idx1 = indices[i * 3];
			int idx2 = indices[i * 3 + 1];
			int idx3 = indices[i * 3 + 2];

			t.set1(positions[idx1]);
			t.set2(positions[idx2]);
			t.set3(positions[idx3]);

			t.calculateNormal();
			normals[idx1].addLocal(t.getNormal());
			normals[idx2].addLocal(t.getNormal());
			normals[idx3].addLocal(t.getNormal());
		}

		Arrays.stream(normals).forEach(v -> v.normalizeLocal());

		logger.debug("idx = {}", idx);

		Mesh mesh = new Mesh();
		mesh.setMode(Mode.Triangles);

		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(positions));
		mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indices));
		mesh.setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
		mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(uvs));

		mesh.updateCounts();
		mesh.updateBound();

		// MyMesh.generateNormals(mesh);

		Geometry geometry = new Geometry("test", mesh);
		// Geometry geometry = new Geometry("test", new FlatShaded(mesh).mesh());
		// geometry.setMaterial(new MtlShowNormals(app.getAssetManager()));
		Texture texture = app.getAssetManager().loadTexture(new TextureKey("textures/uvtest.jpg", false));

		geometry.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray, texture));
		geometry.getMaterial().getAdditionalRenderState().setWireframe(true);

		scene.attachChild(geometry);
	}

	@Override
	protected void cleanup(Application app) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onEnable() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDisable() {
		// TODO Auto-generated method stub

	}

}
