package test.missile;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;

import jme3.common.debug.NdDebugGrid;
import jme3utilities.MyCamera;
import jme3utilities.debug.AxesVisualizer;
import sandbox3.bblaster.StLights;

public class MissileTest extends SimpleApplication {

	public static void main(String[] args) {
		MissileTest app = new MissileTest();

		AppSettings settings = new AppSettings(true);
		settings.setResolution(1600, 800);

		app.setSettings(settings);
		app.setShowSettings(false);

		app.start();
	}

	@Override
	public void simpleInitApp() {
		GuiGlobals.initialize(this);

		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(25);
		flyCam.setZoomSpeed(0);

		rootNode.attachChild(new NdDebugGrid(assetManager, 10, 10, 10, ColorRGBA.Gray));

		AxesVisualizer axesVisualizer = new AxesVisualizer(assetManager, 100, 3);
		rootNode.addControl(axesVisualizer);
		axesVisualizer.setEnabled(true);

		// cam.setLocation(new Vector3f(30.898027f, 17.667004f, 38.485386f));
		// cam.setRotation(new Quaternion(-0.045482054f, 0.93717295f, -0.13360022f, -0.31904417f));
		// cam.setLocation(new Vector3f(16.758022f, 6.8642063f, 13.36385f));
		// cam.setRotation(new Quaternion(0.047128785f, -0.85723186f, 0.079757564f, 0.5065285f));
		cam.setLocation(new Vector3f(15.494603f, 6.8642087f, -12.048357f));
		cam.setRotation(new Quaternion(0.123331256f, -0.5202906f, 0.07624485f, 0.84159f));

		viewPort.setBackgroundColor(ColorRGBA.DarkGray);
		MyCamera.setNearFar(cam, 1f, 32768f);

		rootNode.addLight(new AmbientLight(ColorRGBA.White));
		stateManager.attach(new StLights(rootNode));

		stateManager.attach(new StMissile(rootNode, guiNode));
		stateManager.attach(new StReferenceModels(rootNode));
	}

}
