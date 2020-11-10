package test.navigation;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

import jme3.common.material.MtlShowNormals;

final class StTarget extends BaseAppState {

	private final Node scene = new Node("scene");
	private Geometry geometry;

	public StTarget(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {

		geometry = new Geometry("target", new Box(5, 5, 5));
		geometry.setMaterial(new MtlShowNormals(app.getAssetManager()));

		geometry.setLocalTranslation(new Vector3f(10, 50, 500));
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

	public Vector3f directionTo(Vector3f location) {
		return geometry.getWorldTranslation().subtract(location);
	}

}
