package sandbox3.bblaster;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;

import jme3utilities.MyCamera;
import jme3utilities.debug.AxesVisualizer;
import sandbox3.bblaster.debug.NdDebugGrid;
import sandbox3.bblaster.gui.StHud;

public class BoulderBlasterApp extends SimpleApplication {

	public static void main(String[] args) {
		BoulderBlasterApp app = new BoulderBlasterApp();

		AppSettings settings = new AppSettings(true);
		settings.setResolution(1600, 800);

		settings.setGammaCorrection(true);
		
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

		MyCamera.setNearFar(cam, 0.1f, 32768f);

		stateManager.attach(new StCollision());
		
		stateManager.attach(new StExplosion(rootNode));
		stateManager.attach(new StBoulders(rootNode));

		stateManager.attach(new StMissiles(rootNode));
		stateManager.attach(new StGuns(rootNode));

		stateManager.attach(new StStation(rootNode));
		stateManager.attach(new StCamera());
		
		stateManager.attach(new StTargetting());
		stateManager.attach(new StControls());
		
		stateManager.attach(new StHud(guiNode));
		
		stateManager.attach(new StPlayer(rootNode));
	}

}
