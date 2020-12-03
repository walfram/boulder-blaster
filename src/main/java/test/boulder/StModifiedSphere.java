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
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;
import com.simsilica.event.EventBus;

import jme3.common.material.MtlLighting;
import jme3.common.noise.FastNoiseLite;
import jme3.common.noise.NoiseEvents;
import jme3.common.noise.NoiseSettings;
import jme3utilities.mesh.Octasphere;

final class StModifiedSphere extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StModifiedSphere.class);

	private final Node scene = new Node("scene");

	private NoiseSettings noiseSettings = new NoiseSettings();

	public StModifiedSphere(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		createGeometry(new NoiseSettings(), new SphereSettings());

		EventBus.addListener(this, NoiseEvents.noiseChange);

		EventBus.addListener(this, Events.toggleWireframe);
		EventBus.addListener(this, Events.sphereSettings);
	}

	private void createGeometry(NoiseSettings settings, SphereSettings ss) {
		Mesh mesh = new Octasphere(5, 32f);

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

			FastNoiseLite noise = new FastNoiseLite();
			settings.applyTo(noise);
			float noiseValue = noise.GetNoise(p.x, p.y, p.z);

			if (noiseValue > 0) {
				float f = noiseValue * ss.noiseScale;
				// float f = noiseValue;

				Vector3f delta = positions[idx].normalize().mult(f);

				for (int i : indices) {
					positions[i].addLocal(delta);
				}
			}

			processed.addAll(indices);
		}

		mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(positions));

		Geometry geometry = new Geometry("test", mesh);
		// Geometry geometry = new Geometry("test", new FlatShaded(mesh).mesh());

		geometry.setMaterial(new MtlLighting(getApplication().getAssetManager(), ColorRGBA.Gray));
		// geometry.setMaterial(new MtlLighting(getApplication().getAssetManager(), ColorRGBA.Gray, "textures/uvtest.jpg"));

		// geometry.setMaterial(new MtlUnshaded(app.getAssetManager(), ColorRGBA.Gray));
		// geometry.getMaterial().setFloat("PointSize", 5);

		geometry.getMaterial().getAdditionalRenderState().setWireframe(true);
		// geometry.getMaterial().getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);

		geometry.setLocalScale(ss.scale());

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
