package common.states;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import common.debug.NdDebugGrid;
import jme3utilities.debug.AxesVisualizer;

public final class InitState extends BaseAppState {

	private final boolean flyCamEnabled;
	private final float flyCamSpeed;

	public InitState(boolean flyCamEnabled, float flyCamSpeed) {
		this.flyCamEnabled = flyCamEnabled;
		this.flyCamSpeed = flyCamSpeed;
	}

	@Override
	protected void initialize(Application app) {
		SimpleApplication sa = (SimpleApplication) app;

		sa.getFlyByCamera().setDragToRotate(true);
		sa.getFlyByCamera().setMoveSpeed(flyCamSpeed);
		sa.getFlyByCamera().setZoomSpeed(0);

		if (!flyCamEnabled)
			getState(FlyCamAppState.class).setEnabled(false);

		app.getCamera().setLocation(new Vector3f(85.314644f, 88.49422f, -123.15784f));
		app.getCamera().setRotation(new Quaternion(0.14771472f, -0.16528946f, 0.025045116f, 0.9747987f));
		app.getCamera().setFrustumFar(32768f);

		app.getViewPort().setBackgroundColor(ColorRGBA.DarkGray);

		sa.getRootNode().attachChild(new NdDebugGrid(app.getAssetManager(), 20, 1000f, ColorRGBA.Gray));
		sa.getRootNode().addControl(new AxesVisualizer(app.getAssetManager(), 500, 4));
		sa.getRootNode().getControl(AxesVisualizer.class).setEnabled(true);

		sa.getRootNode().addLight(new AmbientLight(ColorRGBA.White));
		sa.getRootNode().addLight(new DirectionalLight(new Vector3f(0.38503358f, -0.6613831f, 0.6436781f).normalize(), ColorRGBA.White));
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
