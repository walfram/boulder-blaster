package test.boulder.prepared;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

import jme3.common.material.MtlLighting;
import jme3.common.mesh.FlatShaded;
import jme3utilities.mesh.Octasphere;

final class StBoulderReference extends BaseAppState {

	private final Node scene = new Node("scene");

	public StBoulderReference(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Mesh mesh = new Octasphere(2, 128f);

		Geometry geometry = new Geometry("reference", new FlatShaded(mesh).mesh());
		geometry.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray));
		geometry.getMaterial().getAdditionalRenderState().setWireframe(true);

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
