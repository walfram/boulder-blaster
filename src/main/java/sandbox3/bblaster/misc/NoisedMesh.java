package sandbox3.bblaster.misc;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

		Vector3f[] positions = BufferUtils.getVector3Array((FloatBuffer) mesh.getBuffer(Type.Position).getData());
		Set<Integer> processed = new HashSet<>(positions.length);

		for (int idx = 0; idx < positions.length; idx++) {
			if (processed.contains(idx))
				continue;

			Vector3f p = positions[idx];

			List<Integer> indices = new ArrayList<>();
			for (int i = idx; i < positions.length; i++) {
				if (Objects.equals(p, positions[i]))
					indices.add(i);
			}

			float noiseValue = noise.GetNoise(p.x, p.y, p.z);

			// if (noiseValue > 0) {
			float f = noiseValue * noiseScale;
			// float f = noiseValue;

			Vector3f delta = positions[idx].normalize().mult(f);

			for (int i : indices) {
				positions[i].addLocal(delta);
			}
			// }

			processed.addAll(indices);
		}

		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(positions));

		return mesh;
	}

}
