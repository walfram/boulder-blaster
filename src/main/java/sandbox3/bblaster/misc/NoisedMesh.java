package sandbox3.bblaster.misc;

import java.util.stream.Stream;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

import jme3.common.noise.FastNoiseLite;

public final class NoisedMesh {

	private final Mesh source;
	private final FastNoiseLite noise;
	private final float noiseScale;

	public NoisedMesh(Mesh source, FastNoiseLite noise, float noiseScale) {
		this.source = source;
		this.noise = noise;
		this.noiseScale = noiseScale;
	}

	public Mesh mesh() {
		Mesh mesh = source.clone();

		Vector3f[] vertices = BufferUtils.getVector3Array(mesh.getFloatBuffer(Type.Position));
		Stream.of(vertices).map(Vector3f::new).forEach(u -> {
			float e = noise.GetNoise(u.x, u.y, u.z);
			e *= noiseScale;
			Vector3f delta = u.normalize().mult(e);

			for (Vector3f v : vertices) {
				if (v.equals(u))
					v.addLocal(delta);
			}
		});

		// Vector3f[] positions = BufferUtils.getVector3Array((FloatBuffer) mesh.getBuffer(Type.Position).getData());
		// Set<Integer> processed = new HashSet<>(positions.length);
		//
		// for (int idx = 0; idx < positions.length; idx++) {
		// if (processed.contains(idx))
		// continue;
		//
		// Vector3f p = positions[idx];
		//
		// List<Integer> indices = new ArrayList<>();
		// for (int i = idx; i < positions.length; i++) {
		// if (Objects.equals(p, positions[i]))
		// indices.add(i);
		// }
		//
		// float noiseValue = noise.GetNoise(p.x, p.y, p.z);
		// float f = noiseValue * noiseScale;
		// Vector3f delta = positions[idx].normalize().mult(f);
		//
		// for (int i : indices) {
		// positions[i].addLocal(delta);
		// }
		//
		// processed.addAll(indices);
		// }

		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));

		mesh.updateCounts();
		mesh.updateBound();

		return mesh;
	}

}
