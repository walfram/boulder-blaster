package test.cmn;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public final class StCamera extends BaseAppState {

	private final Node pivot = new Node("pivot");

	public StCamera(Node rootNode) {
		rootNode.attachChild(pivot);
	}

	@Override
	protected void initialize(Application app) {
		ChaseCamera camera = new ChaseCamera(app.getCamera(), pivot, app.getInputManager());
		camera.setUpVector(Vector3f.UNIT_Y);

		camera.setInvertVerticalAxis(true);

		camera.setToggleRotationTrigger(new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

		camera.setMaxDistance(500);
		camera.setDefaultDistance(100);

		camera.setMinVerticalRotation(-FastMath.HALF_PI);
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
