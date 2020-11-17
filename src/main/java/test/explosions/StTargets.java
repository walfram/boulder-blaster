package test.explosions;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

import jme3.common.material.MtlLighting;
import jme3utilities.math.noise.Generator;
import jme3utilities.mesh.Octasphere;

final class StTargets extends BaseAppState {

	private final Node scene = new Node("scene");

	private final Generator random = new Generator(127);

	public StTargets(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Mesh mesh = new Octasphere(1, 1f);
		Material material = new MtlLighting(app.getAssetManager(), ColorRGBA.Gray);

		for (int idx = 0; idx < 256; idx++) {

			Geometry target = new Geometry("target#" + idx, mesh);
			target.setMaterial(material);

			Vector3f translation = random.nextUnitVector3f().mult(1024);
			target.setLocalTranslation(translation);

			Quaternion rotation = random.nextQuaternion();
			target.setLocalRotation(rotation);

			float scale = random.nextFloat(10f, 30f);
			target.setLocalScale(scale);

			scene.attachChild(target);
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

	public int collisionWith(Ray ray, CollisionResults results) {
		return scene.collideWith(ray, results);
	}

}
