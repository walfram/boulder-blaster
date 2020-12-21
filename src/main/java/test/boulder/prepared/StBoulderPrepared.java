package test.boulder.prepared;

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
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

import jme3.common.material.MtlLighting;
import jme3.common.mesh.FlatShaded;
import jme3.common.noise.FastNoiseLite;
import jme3.common.noise.FastNoiseLite.CellularReturnType;
import jme3.common.noise.FastNoiseLite.NoiseType;
import jme3utilities.mesh.Octasphere;

final class StBoulderPrepared extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StBoulderPrepared.class);

	private final Node scene = new Node("scene");

	public StBoulderPrepared(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		FastNoiseLite noise = new FastNoiseLite();

		noise.SetNoiseType(NoiseType.Cellular);
		noise.SetCellularReturnType(CellularReturnType.CellValue);

		float frequency = 0.001f;
		noise.SetFrequency(frequency);

		float[] sizes = new float[] { 512f, 384f, 256f, 192f, 128f, 96f, 64f, 48f, 32f };
		// for each size place noised boulder in circle

		for (int idx = 0; idx < sizes.length; idx++) {
			float size = sizes[idx];

			Node wrap = new Node("wrap-" + idx);
			scene.attachChild(wrap);

			float theta = idx * FastMath.DEG_TO_RAD * (360f / sizes.length);
			wrap.rotate(0, theta, 0);

			Mesh mesh = new Octasphere(4, size);
			// int slices = 32;
			// Mesh mesh = new MBox(size, size, size, slices, slices, slices);

			frequency += 0.002f;
			noise.SetFrequency(frequency);
			logger.debug("size = {}, freq = {}", size, frequency);
			applyNoise(noise, mesh, size);

			Geometry geometry = new Geometry("boulder-" + idx, new FlatShaded(mesh).mesh());
			geometry.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray));

			geometry.move(0, 0, 6f * size);

			wrap.attachChild(geometry);
			scene.attachChild(wrap);
		}
	}

	private void applyNoise(FastNoiseLite noise, Mesh mesh, float size) {
		Vector3f[] positions = BufferUtils.getVector3Array((FloatBuffer) mesh.getBuffer(Type.Position).getData());
		Set<Integer> processed = new HashSet<>(positions.length);

		final float noiseScale = Math.max(10f, size / 8f);
		// final float noiseScale = 8f;
		// final float noiseScale = size * 0.1f;
		// final float noiseScale = 1f / ( size * 0.01f);
		logger.debug("noise scale = {}", noiseScale);

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
