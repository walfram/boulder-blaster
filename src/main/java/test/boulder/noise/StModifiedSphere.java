package test.boulder.noise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.simsilica.event.EventBus;

import jme3.common.material.MtlLighting;
import jme3.common.mesh.FlatShaded;
import jme3.common.noise.FastNoiseLite;
import jme3.common.noise.NoiseEvents;
import jme3.common.noise.NoiseSettings;
import jme3utilities.mesh.Octasphere;
import sandbox3.bblaster.misc.NoisedMesh;

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

		Mesh source = new Octasphere(5, 512f * 0.25f);

		// int slices = 32;
		// float size = 512f;
		// Mesh source = new MBox(size, size, size, slices, slices, slices);

		Mesh mesh = new NoisedMesh(source, noise, ss.noiseScale).mesh();

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
