package test.radar;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import jme3.common.material.MtlLighting;
import jme3.common.mesh.FlatShaded;
import jme3utilities.math.noise.Generator;
import jme3utilities.mesh.Octasphere;

final class StObjects extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StObjects.class);

	private final Node scene = new Node("scene");

	private final Generator random = new Generator(1337);

	public StObjects(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Mesh mesh = new FlatShaded(new Octasphere(1, 1f)).mesh();
		Material material = new MtlLighting(app.getAssetManager(), ColorRGBA.Gray);

		for (int i = 0; i < 1024; i++) {
			Geometry geometry = new Geometry("object#" + i, mesh);
			geometry.setMaterial(material);

			Vector3f translation = random.nextVector3f().mult(2048f);
			geometry.setLocalTranslation(translation);

			float scale = random.nextFloat(10, 50);
			geometry.setLocalScale(scale);

			scene.attachChild(geometry);
		}

		logger.debug("inited");
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

	public int objectsNearCount(Vector3f location) {
		return objectsNear(location).size();
	}

	public List<Spatial> objectsNear(Vector3f location) {
		return scene.getChildren().stream().filter(s -> s.getLocalTranslation().distance(location) < 512).collect(
				Collectors.toList());
	}

	public void collideWith(Ray ray, CollisionResults results) {
		scene.collideWith(ray, results);
	}

}
