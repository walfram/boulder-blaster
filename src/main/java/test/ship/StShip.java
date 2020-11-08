package test.ship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;

import jme3utilities.debug.BoundsVisualizer;
import jme3utilities.debug.PointVisualizer;
import test.cmn.ScenePick;

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

		PointVisualizer wpnLeft = new PointVisualizer(app.getAssetManager(), 10, ColorRGBA.Red, "saltire");
		wpnLeft.setLocalTranslation(14f, 2f, -2f);
		scene.attachChild(wpnLeft);

		PointVisualizer wpnRight = new PointVisualizer(app.getAssetManager(), 10, ColorRGBA.Green, "saltire");
		wpnRight.setLocalTranslation(-14f, 2f, -2f);
		scene.attachChild(wpnRight);

		BoundsVisualizer boundsVisualizer = new BoundsVisualizer(app.getAssetManager());
		scene.addControl(boundsVisualizer);
		boundsVisualizer.setSubject(hull);
		boundsVisualizer.setEnabled(true);

		PointVisualizer engineLeft = new PointVisualizer(app.getAssetManager(), 10, ColorRGBA.Blue, null);
		engineLeft.setLocalTranslation(4, 4, -10);
		scene.attachChild(engineLeft);

		PointVisualizer engineRight = new PointVisualizer(app.getAssetManager(), 10, ColorRGBA.Blue, null);
		engineRight.setLocalTranslation(-4, 4, -10);
		scene.attachChild(engineRight);

		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();
		inputMapper.map(F_CLICK, Button.MOUSE_BUTTON2);
		inputMapper.addStateListener(new ScenePick(scene, app), F_CLICK);
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
