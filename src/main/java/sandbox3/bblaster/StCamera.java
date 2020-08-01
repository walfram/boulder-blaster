package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

import common.controls.CtFollowingCamera;

final class StCamera extends BaseAppState {

	private final Vector3f cameraOffset = new Vector3f(0, 15, -50);

	private CtFollowingCamera followingCamera;

	private ChaseCamera chaseCamera;

	@Override
	protected void initialize(Application app) {
		getState(FlyCamAppState.class).setEnabled(false);

		// app.getCamera().setLocation(getState(StPlayer.class).position().add(cameraOffset));
		// app.getCamera().lookAt(getState(StPlayer.class).position(), Vector3f.UNIT_Y);

		followingCamera = new CtFollowingCamera(app.getCamera(), cameraOffset, 5.0f, 5.0f);
		followingCamera.setEnabled(false);

		chaseCamera = new ChaseCamera(app.getCamera(), app.getInputManager());

		chaseCamera.setUpVector(Vector3f.UNIT_Y);

		chaseCamera.setMinDistance(25f);
		chaseCamera.setMaxDistance(250f);
		chaseCamera.setDefaultDistance(75f);

		chaseCamera.setDefaultHorizontalRotation(-FastMath.HALF_PI);
		chaseCamera.setDefaultVerticalRotation(FastMath.PI / 12);

		chaseCamera.setInvertVerticalAxis(true);
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

	public void enableDockedCamera(Spatial cameraTarget) {
		if (cameraTarget.getControl(ChaseCamera.class) == null) {
			cameraTarget.addControl(chaseCamera);
		}

		cameraTarget.getControl(CtFollowingCamera.class).setEnabled(false);
		cameraTarget.getControl(ChaseCamera.class).setEnabled(true);
	}

	public void enableFlightCamera(Spatial cameraTarget) {
		if (cameraTarget.getControl(CtFollowingCamera.class) == null) {
			cameraTarget.addControl(followingCamera);
		}

		cameraTarget.getControl(ChaseCamera.class).setEnabled(false);
		cameraTarget.getControl(CtFollowingCamera.class).setEnabled(true);
	}

	Control flightCamera() {
		return followingCamera;
	}

	Control dockedCamera() {
		return chaseCamera;
	}

}
