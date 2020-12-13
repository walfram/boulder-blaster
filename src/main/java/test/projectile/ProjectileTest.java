package test.projectile;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;

import jme3.common.debug.NdDebugGrid;
import jme3utilities.MyCamera;
import jme3utilities.debug.AxesVisualizer;

public class ProjectileTest extends SimpleApplication {

	public static void main(String[] args) {
		ProjectileTest app = new ProjectileTest();
		
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
		flyCam.setMoveSpeed(100);
		flyCam.setZoomSpeed(0);

		rootNode.attachChild(new NdDebugGrid(assetManager, 20, 20, 250, ColorRGBA.Gray));

		AxesVisualizer axesVisualizer = new AxesVisualizer(assetManager, 100, 1);
		rootNode.addControl(axesVisualizer);
		axesVisualizer.setEnabled(true);

		cam.setLocation(new Vector3f(24.020943f, 31.948973f, -66.383f));
		cam.setRotation(new Quaternion(0.11349983f, -0.09396878f, 0.01078204f, 0.9890255f));

		viewPort.setBackgroundColor(ColorRGBA.DarkGray);
		MyCamera.setNearFar(cam, 1, 32768f);

		rootNode.addLight(new AmbientLight(ColorRGBA.White));
		// stateManager.attach(new StLights(rootNode));
		rootNode.addLight(new DirectionalLight(Vector3f.UNIT_XYZ.negate(), ColorRGBA.White));
		
		stateManager.attach(new StProjectiles(rootNode));
	}

}
