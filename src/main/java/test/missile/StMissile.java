package test.missile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.style.ElementId;

import sandbox3.bblaster.missiles.CtMissileTrail;
import sandbox3.bblaster.missiles.NdMissile;
import test.cmn.ScenePick;

final class StMissile extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StMissile.class);

	private static final FunctionId F_CLICK = new FunctionId("click");

	private final Node scene = new Node("scene");
	private final Node gui = new Node("gui");

	public StMissile(Node rootNode, Node guiNode) {
		rootNode.attachChild(scene);
		guiNode.attachChild(gui);
	}

	@Override
	protected void initialize(Application app) {
		Node missile = new NdMissile(app.getAssetManager());
		scene.attachChild(missile);
		logger.debug("missile bound = {}", missile.getWorldBound());
		// missile.scale(2f);

		// missile.addControl(new SimpleControl() {
		// @Override
		// protected void controlUpdate(float updateInterval) {
		// super.controlUpdate(updateInterval);
		// spatial.rotate(0, 0, FastMath.DEG_TO_RAD * 30f * updateInterval);
		// }
		// });

		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();
		inputMapper.map(F_CLICK, Button.MOUSE_BUTTON2);
		inputMapper.addStateListener(new ScenePick(scene, app), F_CLICK);

		Container container = new Container();
		container.addChild(new Label("missile controls", new ElementId("window.title.label")));
		container.addChild(new ActionButton(new CallMethodAction("toggle engine", missile.getControl(CtMissileTrail.class), "toggleEnabled")));
		container.addChild(new ActionButton(new CallMethodAction("launch", missile.getControl(CtMissileTrail.class), "launch")));
		
		gui.attachChild(container);
		container.setLocalTranslation(1600 - container.getPreferredSize().x - 10, 800 - 10, 0);
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
