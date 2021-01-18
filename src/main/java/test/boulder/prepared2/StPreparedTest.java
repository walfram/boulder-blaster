package test.boulder.prepared2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

import jme3.common.material.MtlLighting;
import jme3.common.mesh.FlatShaded;
import jme3.common.noise.FastNoiseLite;
import jme3.common.noise.FastNoiseLite.FractalType;
import jme3.common.noise.FastNoiseLite.NoiseType;
import jme3utilities.math.noise.Generator;
import jme3utilities.mesh.Octasphere;
import sandbox3.bblaster.misc.NoisedMesh;

final class StPreparedTest extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StPreparedTest.class);

	private final Node scene = new Node("prepared");

	public StPreparedTest(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		FastNoiseLite noise = new FastNoiseLite();
		noise.SetNoiseType(NoiseType.Perlin);
		noise.SetFractalType(FractalType.FBm);

		Generator random = new Generator(1337);

		Mesh mesh = new NoisedMesh(new Octasphere(2, 256f), noise, 128f).mesh();
		Geometry geometry = new Geometry("test", new FlatShaded(mesh).mesh());

		float scalex = random.nextFloat(1f, 1.5f);
		float scaley = random.nextFloat(1f, 1.5f);
		float scalez = random.nextFloat(1f, 1.5f);
		geometry.scale(scalex, scaley, scalez);
		logger.debug("scale = {}", geometry.getLocalScale());

		geometry.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray));

		scene.attachChild(geometry);
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
