package test.radar;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.component.SpringGridLayout;

import jme3utilities.SimpleControl;
import jme3utilities.math.MyVector3f;

final class StHud extends BaseAppState {

	private final Node scene = new Node("hud");

	public StHud(Node guiNode) {
		guiNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {

		Container container = new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.Even, FillMode.Last));
		
		container.addChild(new Label("current position"));
		Label labelPosition = container.addChild(new Label("position.value"), 1);
		labelPosition.setMaxWidth(300);
		
		container.addChild(new Label("objects near"));
		Label labelObjects = container.addChild(new Label("objects.size"), 1);

		container.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				labelPosition.setText(MyVector3f.describe(app.getCamera().getLocation()));
				labelObjects.setText(String.format("%s", getState(StObjects.class).objectsNear(app.getCamera().getLocation())));
			}
		});

		scene.attachChild(container);
		container.setLocalTranslation(10, app.getCamera().getHeight() - 10, 0);
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
