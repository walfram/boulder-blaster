package test.boulder.prepared2;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import jme3.common.material.MtlLighting;
import jme3utilities.mesh.Octasphere;

final class StReference extends BaseAppState {

	private final Node scene = new Node("reference");

	public StReference(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Geometry geometry = new Geometry("reference", new Octasphere(2, 50f));
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
