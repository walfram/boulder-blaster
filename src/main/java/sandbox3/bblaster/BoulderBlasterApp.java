package sandbox3.bblaster;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;

import common.debug.NdDebugGrid;
import common.states.LemurState;
import common.states.SkyState;
import jme3utilities.MyCamera;
import jme3utilities.debug.AxesVisualizer;

public class BoulderBlasterApp extends SimpleApplication {

	public static void main(String[] args) {
		BoulderBlasterApp app = new BoulderBlasterApp();

		AppSettings settings = new AppSettings(true);
		settings.setResolution(1600, 800);

		app.setSettings(settings);
		app.setShowSettings(false);

		app.start();
	}

	@Override
	public void simpleInitApp() {
		stateManager.attach(new LemurState());
		stateManager.attach(new SkyState(rootNode));

		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(150f);
		flyCam.setZoomSpeed(0f);

		viewPort.setBackgroundColor(ColorRGBA.DarkGray);

		AxesVisualizer axesVisualizer = new AxesVisualizer(assetManager, 250f, 2f);
		rootNode.addControl(axesVisualizer);
		axesVisualizer.setEnabled(true);

		rootNode.attachChild(new NdDebugGrid(assetManager, 20, 500, ColorRGBA.Gray));

		rootNode.addLight(new AmbientLight(ColorRGBA.White));

		rootNode.addLight(new DirectionalLight(Vector3f.UNIT_XYZ.negate(), ColorRGBA.White));

//		cam.setFrustumFar(32768f);
//		cam.update();
		MyCamera.setNearFar(cam, 0.1f, 32768f);

		// cam.setLocation(new Vector3f(1.0309658f, 21.743809f, -46.47502f));
		// cam.setRotation(new Quaternion(0.14882252f, -0.02220475f, 0.003342259f, 0.98860896f));

		stateManager.attach(new StCollision());
		stateManager.attach(new StExplosion(rootNode));

		stateManager.attach(new StBoulders(rootNode));

		stateManager.attach(new StMissiles(rootNode));
		stateManager.attach(new StGuns(rootNode));

		stateManager.attach(new StPlayer(rootNode));

		stateManager.attach(new StTargetting());
		stateManager.attach(new StControls());
		stateManager.attach(new StHud(guiNode));
	}

}
