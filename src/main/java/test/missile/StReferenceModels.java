package test.missile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import jme3.common.material.MtlLighting;
import jme3.common.mesh.FlatShaded;
import jme3utilities.mesh.Octasphere;

final class StReferenceModels extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StReferenceModels.class);

	private final Node scene = new Node("scene");

	public StReferenceModels(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {

		Geometry boulder = new Geometry("reference-boulder", new FlatShaded(new Octasphere(1, 50f)).mesh());
		boulder.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray));
		scene.attachChild(boulder);
		boulder.move(-100f, 0, 0);

		Spatial shipHull = app.getAssetManager().loadModel("models/spacekit2/craft_speederD.obj");
		scene.attachChild(shipHull);
		shipHull.move(-25f, 0, 0);
		shipHull.scale(10f);
		logger.debug("ship hull bound = {}", shipHull.getWorldBound());

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
