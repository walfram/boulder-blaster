package test.ship;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;

import jme3.common.debug.NdDebugGrid;
import jme3utilities.MyCamera;
import jme3utilities.debug.AxesVisualizer;
import sandbox3.bblaster.StLights;

public class ShipTest extends SimpleApplication {

	public static void main(String[] args) {
		ShipTest app = new ShipTest();

		AppSettings settings = new AppSettings(true);
		settings.setResolution(1600, 800);

		app.setSettings(settings);
		app.setShowSettings(false);

		app.start();
	}

	@Override
	public void simpleInitApp() {
		GuiGlobals.initialize(this);
		BaseStyles.loadGlassStyle();
		GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);

		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(250);
		flyCam.setZoomSpeed(0);

		rootNode.attachChild(new NdDebugGrid(assetManager, 10, 10, 10, ColorRGBA.Gray));

		AxesVisualizer axesVisualizer = new AxesVisualizer(assetManager, 100, 3);
		rootNode.addControl(axesVisualizer);
		axesVisualizer.setEnabled(true);

		// cam.setLocation(new Vector3f(19.184437f, 23.092829f, 21.250187f));
		// cam.setRotation(new Quaternion(-0.093374066f, 0.9066154f, -0.28852183f, -0.29340255f));
		// cam.setLocation(new Vector3f(119.05582f, 85.26459f, -259.29294f));
		// cam.setRotation(new Quaternion(0.064766675f, -0.06329711f, 0.00411477f, 0.99588245f));
		cam.setLocation(new Vector3f(47.215347f, 32.46507f, -12.565666f));
		cam.setRotation(new Quaternion(0.1492671f, -0.33998245f, 0.054748856f, 0.9268947f));

		viewPort.setBackgroundColor(ColorRGBA.DarkGray);
		MyCamera.setNearFar(cam, 1, 32768f);

		rootNode.addLight(new AmbientLight(ColorRGBA.White));
		stateManager.attach(new StLights(rootNode));

		stateManager.attach(new StTargets(rootNode));

		stateManager.attach(new StShip(rootNode));

		stateManager.attach(new StGui(guiNode));
	}

}
