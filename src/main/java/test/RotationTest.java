package test;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;

import jme3utilities.SimpleControl;
import jme3utilities.debug.AxesVisualizer;
import sandbox3.bblaster.debug.NdDebugGrid;

public class RotationTest extends SimpleApplication {

	public static void main(String[] args) {
		RotationTest app = new RotationTest();

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
		flyCam.setMoveSpeed(100f);
		flyCam.setZoomSpeed(0);

		rootNode.attachChild(new NdDebugGrid(assetManager, 20, 100f, ColorRGBA.Gray));
		rootNode.addControl(new AxesVisualizer(assetManager, 128, 1));
		rootNode.getControl(AxesVisualizer.class).setEnabled(true);

		// rootNode.addLight(new AmbientLight(ColorRGBA.White.mult(4)));
		rootNode.addLight(new DirectionalLight(Vector3f.UNIT_X.negate(), ColorRGBA.White.mult(2)));
		rootNode.addLight(new DirectionalLight(Vector3f.UNIT_Y.negate(), ColorRGBA.White.mult(2)));
		rootNode.addLight(new DirectionalLight(Vector3f.UNIT_Z.negate(), ColorRGBA.White.mult(2)));

		cam.setFrustumFar(32768);

		cam.setLocation(new Vector3f(-111.579185f, 33.175987f, 53.781494f));
		cam.setRotation(new Quaternion(0.07625744f, 0.84254456f, -0.123889945f, 0.51860845f));

		Spatial model = assetManager.loadModel("Models/HoverTank/Tank2.mesh.xml");
		rootNode.attachChild(model);

		model.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				Vector3f direction = cam.getLocation().subtract(spatial.getLocalTranslation());

				Quaternion from = spatial.getLocalRotation().clone();
				// Quaternion to = new Quaternion().lookAt(cam.getLocation(), Vector3f.UNIT_Y);
				Quaternion to = from.clone().lookAt(direction, Vector3f.UNIT_Y);

				from.slerp(to, updateInterval);

				spatial.setLocalRotation(from);
			}
		});

		model.move(100, 25, -200);
	}

}
