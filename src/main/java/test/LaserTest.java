package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;

import jme3.common.debug.NdDebugGrid;
import jme3.common.material.MtlUnshaded;
import jme3utilities.debug.AxesVisualizer;

public class LaserTest extends SimpleApplication {

	private static final FunctionId F_LASER = new FunctionId("laser");

	public static void main(String[] args) {
		LaserTest app = new LaserTest();

		AppSettings settings = new AppSettings(true);
		settings.setResolution(1600, 800);

		app.setSettings(settings);
		app.setShowSettings(false);

		app.start();
	}

	private static final Logger logger = LoggerFactory.getLogger(LaserTest.class);

	@Override
	public void simpleInitApp() {
		GuiGlobals.initialize(this);

		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(100f);
		flyCam.setZoomSpeed(0);

		rootNode.attachChild(new NdDebugGrid(assetManager, 20, 20, 100f, ColorRGBA.Gray));
		rootNode.addControl(new AxesVisualizer(assetManager, 128, 1));
		rootNode.getControl(AxesVisualizer.class).setEnabled(true);

		cam.setFrustumFar(32768);

//		cam.setLocation(new Vector3f(-2.188475f, 1.188586f, -10.577302f));
//		cam.setRotation(new Quaternion(-0.028448217f, 0.23830087f, 0.0069829333f, 0.9707495f));

		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();

		inputMapper.map(F_LASER, Button.MOUSE_BUTTON2);
		inputMapper.addStateListener((func, state, tpf) -> fireLaser(state != InputState.Off), F_LASER);
	}

	private void fireLaser(boolean b) {
		logger.debug("fire = {}", b);
		if (!b) {
			rootNode.detachChildNamed("beam");
			return;
		}

		Vector3f origin = cam.getWorldCoordinates(new Vector2f(1600, 0), 0);

		// Vector3f origin = cam.getLocation().add(new Vector3f(-0.5f, -0.5f, 0));
		Vector3f end = cam.getRotation().mult(Vector3f.UNIT_Z).mult(10000f);

		logger.debug("cam = {}, origin = {}, end = {}", cam.getLocation(), origin, end);

		Material mtl = new MtlUnshaded(assetManager, ColorRGBA.Yellow);
		mtl.getAdditionalRenderState().setLineWidth(5f);

		Geometry beam = new Geometry("beam", new Line(origin, end));
		beam.setMaterial(mtl);

		rootNode.attachChild(beam);
	}

}
