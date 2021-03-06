package test.navigation;

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

public class NavigationTest extends SimpleApplication {

	public static void main(String[] args) {
		NavigationTest app = new NavigationTest();

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

		cam.setLocation(new Vector3f(19.184437f, 23.092829f, 21.250187f));
		cam.setRotation(new Quaternion(-0.093374066f, 0.9066154f, -0.28852183f, -0.29340255f));

		viewPort.setBackgroundColor(ColorRGBA.DarkGray);
		MyCamera.setNearFar(cam, 1, 32768f);

		rootNode.addLight(new AmbientLight(ColorRGBA.White));
		stateManager.attach(new StLights(rootNode));
		
		stateManager.attach(new StTarget(rootNode));
		
		stateManager.attach(new StGui(guiNode));
	}

}
