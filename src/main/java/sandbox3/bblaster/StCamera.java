package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import sandbox3.bblaster.controls.CtFollowingCamera;

public final class StCamera extends BaseAppState {

	private final Vector3f cameraOffset = new Vector3f(0, 15, -50);

	private CtFollowingCamera followingCamera;

	@Override
	protected void initialize(Application app) {
		getState(FlyCamAppState.class).setEnabled(false);

		followingCamera = new CtFollowingCamera(app.getCamera(), cameraOffset, 5.0f, 5.0f);
		followingCamera.setEnabled(false);
		
		app.getCamera().setLocation(cameraOffset);
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

	public void enableFlightCamera(Spatial cameraTarget) {
		if (cameraTarget.getControl(CtFollowingCamera.class) == null) {
			cameraTarget.addControl(followingCamera);
		}

		cameraTarget.getControl(CtFollowingCamera.class).setEnabled(true);
	}

}
