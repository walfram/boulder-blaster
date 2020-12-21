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
	private final float defaultDistance;

	public StCamera(Node rootNode) {
		this(rootNode, 100f);
	}

	public StCamera(Node rootNode, float defaultDistance) {
		rootNode.attachChild(pivot);
		this.defaultDistance = defaultDistance;
	}

	@Override
	protected void initialize(Application app) {
		ChaseCamera camera = new ChaseCamera(app.getCamera(), pivot, app.getInputManager());
		camera.setUpVector(Vector3f.UNIT_Y);

		camera.setInvertVerticalAxis(true);

		camera.setToggleRotationTrigger(new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

		camera.setMaxDistance(1500);
		camera.setDefaultDistance(defaultDistance);

		camera.setMinVerticalRotation(-FastMath.HALF_PI);
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
