package test.boulder;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.simsilica.event.EventBus;
import com.simsilica.lemur.Action;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;

final class StGui extends BaseAppState {

	private final Node scene = new Node("scene");

	public StGui(Node guiNode) {
		guiNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Container c = new Container();
		c.addChild(new ActionButton(new Action("toggle wireframe") {
			@Override
			public void execute(Button source) {
				EventBus.publish(Events.toggleWireframe, null);
			}
		}));
		
		scene.attachChild(c);
		c.setLocalTranslation(5, app.getCamera().getHeight() - 5, 0);
	}

	@Override
	protected void cleanup(Application app) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onEnable() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDisable() {
		// TODO Auto-generated method stub

	}

}
