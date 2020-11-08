package test.ship;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.Container;

final class StGui extends BaseAppState {

	private final Node scene = new Node("scene");

	public StGui(Node guiNode) {
		guiNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Container container = new Container();

		container.addChild(new ActionButton(new CallMethodAction("start engines", getState(StShip.class), "toggleEngines")));
		container.addChild(new ActionButton(new CallMethodAction("fire guns", getState(StShip.class), "fireGuns")));
		container.addChild(new ActionButton(new CallMethodAction("fire missile", getState(StShip.class), "fireMissile")));

		scene.attachChild(container);
		container.setLocalTranslation(1600 - container.getPreferredSize().x - 5, 800 - 5, 0);
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
