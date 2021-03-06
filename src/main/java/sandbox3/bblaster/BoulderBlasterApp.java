package sandbox3.bblaster;

import java.util.Arrays;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.event.MouseAppState;
import com.simsilica.lemur.event.PickState;
import com.simsilica.lemur.style.BaseStyles;

import jme3.common.debug.NdDebugGrid;
import jme3utilities.MyCamera;
import jme3utilities.debug.AxesVisualizer;

public class BoulderBlasterApp extends SimpleApplication {

	public static void main(String[] args) {
		boolean debug = Arrays.asList(args).contains("--debug");

		BoulderBlasterApp app = new BoulderBlasterApp();

		AppSettings settings = new AppSettings(true);

		if (debug) {
			settings.setResolution(1600, 800);
			app.setShowSettings(false);
		} else {
			app.setShowSettings(true);
		}

		app.setPauseOnLostFocus(false);

		// settings.setGammaCorrection(true);

		app.setSettings(settings);

		app.start();
	}

	@Override
	public void simpleInitApp() {
		GuiGlobals.initialize(this);
		BaseStyles.loadGlassStyle();
		GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);

		// using instanced node, sometimes instanced mesh has null bound and picking throws NPE
		stateManager.getState(MouseAppState.class).setIncludeDefaultCollisionRoots(false);
		stateManager.getState(MouseAppState.class).addCollisionRoot(guiViewPort, PickState.PICK_LAYER_GUI);

		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(150f);
		flyCam.setZoomSpeed(0f);

		viewPort.setBackgroundColor(ColorRGBA.DarkGray);

		AxesVisualizer axesVisualizer = new AxesVisualizer(assetManager, 250f, 2f);
		rootNode.addControl(axesVisualizer);
		axesVisualizer.setEnabled(true);

		rootNode.attachChild(new NdDebugGrid(assetManager, 20, 20, 500, ColorRGBA.Gray));

		rootNode.addLight(new AmbientLight(ColorRGBA.White));
		rootNode.addLight(new DirectionalLight(Vector3f.UNIT_XYZ.negate(), ColorRGBA.White));

		MyCamera.setNearFar(cam, 1f, 32768f);

		// stateManager.attach(new StLights(rootNode));
		stateManager.attach(new StSky(rootNode));

		stateManager.attach(new StCollision());

		stateManager.attach(new StExplosion(rootNode));
		stateManager.attach(new StBoulders(rootNode));

		stateManager.attach(new StMissiles(rootNode));
		stateManager.attach(new StBlasters(rootNode));

		stateManager.attach(new StTargetting());
		stateManager.attach(new StHud(guiNode));
		stateManager.attach(new StHudCrosshair(guiNode));
		stateManager.attach(new StHudTarget(guiNode));
		stateManager.attach(new StHudRadar());
		
		stateManager.attach(new StCamera());
		stateManager.attach(new StControls());

		stateManager.attach(new StPlayer(rootNode));
	}

}
