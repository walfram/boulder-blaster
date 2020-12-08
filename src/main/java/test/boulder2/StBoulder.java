package test.boulder2;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import jme3.common.material.MtlLighting;
import jme3utilities.mesh.Octasphere;

final class StBoulder extends BaseAppState {

	private final Node scene = new Node("scene");
	
	public StBoulder(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Geometry boulder = new Geometry("boulder", new Octasphere(2, 32f));
		boulder.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray));
		scene.attachChild(boulder);
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
