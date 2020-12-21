package test.boulder.noise;

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
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import com.simsilica.event.EventBus;

import jme3.common.material.MtlLighting;
import jme3.common.mesh.FlatShaded;
import jme3.common.noise.FastNoiseLite;
import jme3.common.noise.NoiseEvents;
import jme3.common.noise.NoiseSettings;
import jme3utilities.mesh.Octasphere;

final class StModifiedSphere extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StModifiedSphere.class);

	private final Node scene = new Node("scene");

	private NoiseSettings noiseSettings = new NoiseSettings();

	private boolean wireframe = false;

	public StModifiedSphere(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		NoiseSettings settings = new NoiseSettings();
		createGeometry(settings, new SphereSettings());

		EventBus.addListener(this, NoiseEvents.noiseChange);

		EventBus.addListener(this, Events.toggleWireframe);
		EventBus.addListener(this, Events.sphereSettings);
	}

	private void createGeometry(NoiseSettings settings, SphereSettings ss) {
		FastNoiseLite noise = new FastNoiseLite();
		settings.applyTo(noise);

		Mesh mesh = new Octasphere(5, 512f * 0.25f);

		// int slices = 32;
		// float size = 512f;
		// Mesh mesh = new MBox(size, size, size, slices, slices, slices);

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
			float f = noiseValue * ss.noiseScale;
			// float f = noiseValue;

			Vector3f delta = positions[idx].normalize().mult(f);

			for (int i : indices) {
				positions[i].addLocal(delta);
			}
			// }

			processed.addAll(indices);
		}

		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(positions));

		// Geometry geometry = new Geometry("test", mesh);
		Geometry geometry = new Geometry("test", new FlatShaded(mesh).mesh());

		geometry.setMaterial(new MtlLighting(getApplication().getAssetManager(), ColorRGBA.Gray));

		geometry.getMaterial().getAdditionalRenderState().setWireframe(wireframe);

		// geometry.setLocalScale(ss.scale());
		// geometry.setLocalScale(32f);

		scene.detachAllChildren();
		scene.attachChild(geometry);
	}

	protected void sphereSettings(SphereSettings settings) {
		createGeometry(noiseSettings, settings);
	}

	protected void noiseChange(NoiseSettings noiseSettings) {
		this.noiseSettings = noiseSettings;
		SphereSettings sphereSettings = getState(StGui.class).sphereSettings();
		createGeometry(noiseSettings, sphereSettings);
	}

	protected void toggleWireframe(Boolean isWireframe) {
		logger.debug("toggle wireframe event");

		wireframe = isWireframe.booleanValue();

		SphereSettings sphereSettings = getState(StGui.class).sphereSettings();
		createGeometry(noiseSettings, sphereSettings);

		// scene.depthFirstTraversal(child -> {
		// if (child instanceof Geometry) {
		// RenderState rs = ((Geometry) child).getMaterial().getAdditionalRenderState();
		// rs.setWireframe(!rs.isWireframe());
		// }
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

}
