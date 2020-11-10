package test.navigation;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;

import jme3utilities.MyCamera;
import jme3utilities.SimpleControl;
import jme3utilities.math.MyVector3f;

final class StGui extends BaseAppState {

	private final Node scene = new Node("scene");

	public StGui(Node guiNode) {
		guiNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Container container = new Container();

		container.addChild(new Label("camera azimuth"));
		Label labelCameraAzm = container.addChild(new Label("camera.azimuth.value"));
		labelCameraAzm.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);
				labelCameraAzm.setText(String.valueOf(MyCamera.azimuth(app.getCamera())));
			}
		});

		container.addChild(new Label("camera altitude"));
		Label labelCameraAlt = container.addChild(new Label("camera.altitude.value"));
		labelCameraAlt.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);
				labelCameraAlt.setText(String.valueOf(MyVector3f.altitude(app.getCamera().getLocation())));
			}
		});

		container.addChild(new Label("target azimuth"));
		Label labelTargetAzm = container.addChild(new Label("target.azimuth"));
		labelTargetAzm.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				Vector3f offset = getState(StTarget.class).directionTo(app.getCamera().getLocation());
				float azimuth = MyVector3f.azimuth(offset);

				labelTargetAzm.setText(String.valueOf(azimuth));
			}
		});

		container.addChild(new Label("target altitude"));
		Label labelTargetAlt = container.addChild(new Label("target.altitude"));

		labelTargetAlt.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				Vector3f offset = getState(StTarget.class).directionTo(app.getCamera().getLocation());
				float altitude = MyVector3f.altitude(offset);

				labelTargetAlt.setText(String.valueOf(altitude));

			}
		});

		scene.attachChild(container);
		container.setLocalTranslation(5, 800 - 5, 0);
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
