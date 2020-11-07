package test.ship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;

import jme3.common.material.MtlLighting;

final class StShip extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StShip.class);

	private static final FunctionId F_CLICK = new FunctionId("click");

	private final Node scene = new Node("scene");

	public StShip(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Spatial hull = app.getAssetManager().loadModel("models/spacekit2/craft_speederD.obj");
		hull.scale(10f);
		scene.attachChild(hull);

		Geometry wpnLeft = new Geometry("weapon-left", new Box(0.5f, 0.5f, 0.5f));
		wpnLeft.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Red));
		wpnLeft.setLocalTranslation(14f, 2f, -3.5f);
		scene.attachChild(wpnLeft);
		
		Geometry wpnRight = new Geometry("weapon-right", new Box(0.5f, 0.5f, 0.5f));
		wpnRight.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Green));
		wpnRight.setLocalTranslation(-14f, 2f, -3.5f);
		scene.attachChild(wpnRight);
		
		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();
		inputMapper.map(F_CLICK, Button.MOUSE_BUTTON2);
		inputMapper.addStateListener((func, state, tpf) -> click(state, tpf), F_CLICK);
	}

	private void click(InputState state, double tpf) {
		if (state != InputState.Off)
			return;

		Ray ray = new MouseClickRay(getApplication()).ray();
		CollisionResults results = new CollisionResults();
		int collisions = scene.collideWith(ray, results);

		if (collisions == 0) {
			logger.debug("no collisions");
			return;
		}

		CollisionResult collision = results.getClosestCollision();

		logger.debug("collision with {} at {}", collision.getGeometry(), collision.getContactPoint());
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
