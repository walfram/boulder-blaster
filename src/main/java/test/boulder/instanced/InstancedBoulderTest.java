package test.boulder.instanced;

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

public class InstancedBoulderTest extends SimpleApplication {

	public static void main(String[] args) {
		InstancedBoulderTest app = new InstancedBoulderTest();

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
		flyCam.setMoveSpeed(500);
		flyCam.setZoomSpeed(0);

		rootNode.attachChild(new NdDebugGrid(assetManager, 20, 20, 250, ColorRGBA.Gray));

		AxesVisualizer axesVisualizer = new AxesVisualizer(assetManager, 100, 1);
		rootNode.addControl(axesVisualizer);
		axesVisualizer.setEnabled(true);

		cam.setLocation(new Vector3f(-778.3845f, 486.72064f, -1294.5544f));
		cam.setRotation(new Quaternion(0.12650432f, 0.3143368f, -0.042311817f, 0.9398929f));

		viewPort.setBackgroundColor(ColorRGBA.DarkGray);
		MyCamera.setNearFar(cam, 1, 32768f);

		rootNode.addLight(new AmbientLight(ColorRGBA.White));
		stateManager.attach(new StLights(rootNode));
		
		stateManager.attach(new StInstancedBoulders(rootNode));
	}

}
