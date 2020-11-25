package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.Arrow;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.style.BaseStyles;

import jme3.common.debug.NdDebugGrid;
import jme3.common.material.MtlUnshaded;
import jme3utilities.SimpleControl;
import jme3utilities.debug.AxesVisualizer;
import jme3utilities.math.MyVector3f;

public class AzimuthTest extends SimpleApplication {

	private static final Logger logger = LoggerFactory.getLogger(AzimuthTest.class);

	// for (int i = 0; i < 360; i++) {
	// float x = 10f * FastMath.sin(FastMath.DEG_TO_RAD * i);
	// float z = 10f * FastMath.cos(FastMath.DEG_TO_RAD * i);
	//
	// float azimuth = MyVector3f.azimuth(new Vector3f(x, 0, z));
	// logger.debug("i = {}, azimuth = {}", i, azimuth);
	// }
	public static void main(String[] args) {
		AzimuthTest app = new AzimuthTest();

		AppSettings settings = new AppSettings(true);
		settings.setResolution(1600, 800);

		app.setSettings(settings);
		app.setShowSettings(false);

		app.start();
	}

	@Override
	public void simpleInitApp() {
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(100);
		flyCam.setZoomSpeed(0);

		cam.setLocation(new Vector3f(20, 30, 40));
		cam.lookAt(new Vector3f(), Vector3f.UNIT_Y);

		GuiGlobals.initialize(this);
		BaseStyles.loadGlassStyle();
		GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);

		AxesVisualizer axesVisualizer = new AxesVisualizer(assetManager, 150f, 1f);
		rootNode.addControl(axesVisualizer);
		axesVisualizer.setEnabled(true);

		rootNode.attachChild(new NdDebugGrid(assetManager, 30, 30, 25, ColorRGBA.DarkGray));

		Geometry arrow = new Geometry("arrow", new Arrow(Vector3f.UNIT_X.mult(20)));
		arrow.setMaterial(new MtlUnshaded(assetManager, ColorRGBA.Yellow));
		arrow.getMaterial().getAdditionalRenderState().setLineWidth(4f);
		rootNode.attachChild(arrow);

		rootNode.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);
				arrow.rotate(0, FastMath.DEG_TO_RAD * updateInterval * -10f, 0);
			}
		});

		Container container = new Container();
		container.addChild(new Label("azimuth"));
		Label labelAzimuth = container.addChild(new Label("azimuth.value"));

		guiNode.attachChild(container);
		container.setLocalTranslation(5, cam.getHeight() - 5, 0);

		container.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				Vector3f direction = arrow.getLocalRotation().mult(Vector3f.UNIT_X);
				float azimuth = MyVector3f.azimuth(direction);

				String s = String.format("azimuth = %.04f, degrees = %.04f", azimuth, FastMath.RAD_TO_DEG * azimuth);
				labelAzimuth.setText(s);
			}
		});

	}

}
