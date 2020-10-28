package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.DefaultRangedValueModel;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.Slider;
import com.simsilica.lemur.input.Axis;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.style.BaseStyles;

import jme3utilities.debug.AxesVisualizer;
import sandbox3.bblaster.SkyState;
import sandbox3.bblaster.debug.NdDebugGrid;

public class SteeringTest extends SimpleApplication {

	private static final FunctionId F_CURSOR_VISIBILITY = new FunctionId("curosr-visibility");

	private static final FunctionId F_YAW = new FunctionId("yaw");
	private static final FunctionId F_PITCH = new FunctionId("pitch");

	private static final Logger logger = LoggerFactory.getLogger(SteeringTest.class);

	@Override
	public void simpleInitApp() {
		GuiGlobals.initialize(this);
		BaseStyles.loadGlassStyle();
		GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);

		rootNode.attachChild(new NdDebugGrid(assetManager, 20, 100f, ColorRGBA.Gray));
		rootNode.addControl(new AxesVisualizer(assetManager, 128, 1));
		rootNode.getControl(AxesVisualizer.class).setEnabled(true);

		FlyCamAppState flyCamAppState = stateManager.getState(FlyCamAppState.class);
		stateManager.detach(flyCamAppState);
		
		cam.setLocation(new Vector3f(0, 5, 10));

		stateManager.attach(new SkyState(rootNode));

		Slider yawSlider = new Slider(com.simsilica.lemur.Axis.X);
		yawSlider.setModel(new DefaultRangedValueModel(-10, 10, 0));
		guiNode.attachChild(yawSlider);
		yawSlider.setPreferredSize(new Vector3f(200, 24, 0));
		yawSlider.setLocalTranslation(800 - yawSlider.getPreferredSize().x * 0.5f, 800 - 10, 0);

		Label yawLabel = new Label("yaw value");
		yawLabel.setLocalTranslation(yawSlider.getLocalTranslation().add(new Vector3f(0, -24, 0)));
		guiNode.attachChild(yawLabel);

		Slider pitchSlider = new Slider(com.simsilica.lemur.Axis.Y);
		pitchSlider.setModel(new DefaultRangedValueModel(-10, 10, 0));
		pitchSlider.setPreferredSize(new Vector3f(16, 200, 0));
		guiNode.attachChild(pitchSlider);
		pitchSlider.setLocalTranslation(10, 400 + pitchSlider.getPreferredSize().y * 0.5f, 0);

		Label pitchLabel = new Label("pitch value");
		pitchLabel.setLocalTranslation(pitchSlider.getLocalTranslation().add(new Vector3f(24, 0, 0)));
		guiNode.attachChild(pitchLabel);

		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();

		inputMapper.map(F_CURSOR_VISIBILITY, Button.MOUSE_BUTTON2);
		inputMapper.addStateListener((func, state, tpf) -> {
			inputManager.setCursorVisible(state == InputState.Off);
		}, F_CURSOR_VISIBILITY);

		inputMapper.map(F_YAW, Axis.MOUSE_X, Button.MOUSE_BUTTON2);
		inputMapper.map(F_PITCH, Axis.MOUSE_Y, Button.MOUSE_BUTTON2);

		inputMapper.addAnalogListener((func, value, tpf) -> {
			Vector3f up = cam.getRotation().mult(Vector3f.UNIT_Y);
			// Vector3f up = Vector3f.UNIT_Y;
			
//			float sign = FastMath.sign(inputManager.getCursorPosition().x - 800);
			
			// float sign = -1f * FastMath.sign((float) value);
			float v = -1f * (float) (value * tpf);
			logger.debug("yaw = {}, x = {}", v, inputManager.getCursorPosition().x);

			Quaternion q = new Quaternion().fromAngleAxis(v, up);
			Quaternion rotation = q.mult(cam.getRotation());
			cam.setRotation(rotation);
		}, F_YAW);
		
		inputMapper.addAnalogListener((func, value, tpf) -> {
			Vector3f left = cam.getRotation().mult(Vector3f.UNIT_X);
			
			// float sign = -1f * FastMath.sign((float) value);
			float v = -1 * (float) (value * tpf);
			
			Quaternion q = new Quaternion().fromAngleAxis(v, left);
			Quaternion rotation = q.mult(cam.getRotation());
			cam.setRotation(rotation);
			
		}, F_PITCH);
	}

	private void rotate(float value, Vector3f axis) {
		Matrix3f mat = new Matrix3f();
		mat.fromAngleNormalAxis(value, axis);

		Vector3f up = cam.getUp();
		Vector3f left = cam.getLeft();
		Vector3f dir = cam.getDirection();

		mat.mult(up, up);
		mat.mult(left, left);
		mat.mult(dir, dir);

		Quaternion q = new Quaternion();
		q.fromAxes(left, up, dir);
		q.normalizeLocal();

		cam.setAxes(q);
	}

	public static void main(String[] args) {
		SteeringTest app = new SteeringTest();

		AppSettings settings = new AppSettings(true);
		settings.setResolution(1600, 800);

		app.setSettings(settings);
		app.setShowSettings(false);

		app.start();
	}

}
