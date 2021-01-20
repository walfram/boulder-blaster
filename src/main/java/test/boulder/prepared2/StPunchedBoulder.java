package test.boulder.prepared2;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

import jme3.common.material.MtlLighting;
import jme3.common.noise.FastNoiseLite;
import jme3.common.noise.FastNoiseLite.FractalType;
import jme3.common.noise.FastNoiseLite.NoiseType;
import jme3utilities.debug.BoundsVisualizer;
import jme3utilities.math.noise.Generator;
import jme3utilities.mesh.Octasphere;

final class StPunchedBoulder extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StPunchedBoulder.class);

	private final Node scene = new Node("scene");
	private final Generator random = new Generator(1337);

	public StPunchedBoulder(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		FastNoiseLite noise = new FastNoiseLite();
		noise.SetNoiseType(NoiseType.Perlin);
		noise.SetFractalType(FractalType.FBm);

		float[] radiuses = new float[] { 16f, 32f, 64f, 128f, 256f, 512f };

		// float radius = 100f;
		float x = 0f;
		for (float radius : radiuses) {
			Mesh mesh = new Octasphere(4, radius);

			List<Face> faces = new PunchedMeshFaces(mesh, noise, random, radius * 0.5f).faces();

			Geometry g = new Geometry("g", new FaceMesh(faces).mesh());
			g.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray));
			scene.attachChild(g);
			
			Vector3f[] vertices = BufferUtils.getVector3Array(g.getMesh().getFloatBuffer(Type.Position));
			float avg = 0f;
			for (Vector3f v : vertices)
				avg += v.length();
			avg /= vertices.length;
			logger.debug("radius = {}, avg = {}", radius, avg);
			
			g.setModelBound(new BoundingSphere(avg, new Vector3f()));
			
			Vector3f translation = new Vector3f(x, 0, 0);
			g.setLocalTranslation(translation);
			
//			BoundingBox bound = (BoundingBox) g.getWorldBound().clone();
//			x += bound.getXExtent() * 3f;
			x += radius * 3f;
			
//			BoundsVisualizer ctrl = new BoundsVisualizer(app.getAssetManager());
//			ctrl.setSubject(g);
//			scene.addControl(ctrl);
//			ctrl.setEnabled(true);
		}
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
