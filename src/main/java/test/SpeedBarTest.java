package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Action;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.style.BaseStyles;

public class SpeedBarTest extends SimpleApplication {

	private static final Logger logger = LoggerFactory.getLogger(SpeedBarTest.class);

	private static final FunctionId F_SPEED_UP = new FunctionId("speed-up");
	private static final FunctionId F_SLOW_DOWN = new FunctionId("slow-down");

	private static final FunctionId F_SPEED_0 = new FunctionId("speed-0");
	private static final FunctionId F_SPEED_1 = new FunctionId("speed-1");
	private static final FunctionId F_SPEED_3 = new FunctionId("speed-3");
	private static final FunctionId F_SPEED_5 = new FunctionId("speed-5");
	private static final FunctionId F_SPEED_7 = new FunctionId("speed-7");
	private static final FunctionId F_SPEED_10 = new FunctionId("speed-10");


	private ProgressBar speedBar;
	private SpeedControl speedControl = new SpeedControl();

	@Override
	public void simpleInitApp() {
		GuiGlobals.initialize(this);
		BaseStyles.loadGlassStyle();
		GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);

		stateManager.getState(FlyCamAppState.class).setEnabled(false);

		Container container = new Container();

		container.addChild(new Label("speed progress")).setMaxWidth(200);

		speedBar = container.addChild(new ProgressBar());

		container.addChild(new ActionButton(new Action("press me") {
			@Override
			public void execute(Button source) {
				logger.debug("pressed");
			}
		}));

		guiNode.attachChild(container);
		container.setLocalTranslation(10, 800 - 10, 0);

		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();

		inputMapper.map(F_SPEED_UP, KeyInput.KEY_W);
		inputMapper.map(F_SLOW_DOWN, KeyInput.KEY_S);

		inputMapper.addAnalogListener((func, value, tpf) -> changeSpeed(1d, tpf), F_SPEED_UP);
		inputMapper.addAnalogListener((func, value, tpf) -> changeSpeed(-1d, tpf), F_SLOW_DOWN);

		inputMapper.map(F_SPEED_0, KeyInput.KEY_NUMPAD0);
		inputMapper.addStateListener((func, state, tpf) -> changeSpeed(0.0, state), F_SPEED_0);
		inputMapper.map(F_SPEED_1, KeyInput.KEY_NUMPAD1);
		inputMapper.addStateListener((func, state, tpf) -> changeSpeed(0.1, state), F_SPEED_1);
		inputMapper.map(F_SPEED_3, KeyInput.KEY_NUMPAD3);
		inputMapper.addStateListener((func, state, tpf) -> changeSpeed(0.3, state), F_SPEED_3);
		inputMapper.map(F_SPEED_5, KeyInput.KEY_NUMPAD5);
		inputMapper.addStateListener((func, state, tpf) -> changeSpeed(0.5, state), F_SPEED_5);
		inputMapper.map(F_SPEED_7, KeyInput.KEY_NUMPAD7);
		inputMapper.addStateListener((func, state, tpf) -> changeSpeed(0.7, state), F_SPEED_7);
		inputMapper.map(F_SPEED_10, KeyInput.KEY_NUMPAD9);
		inputMapper.addStateListener((func, state, tpf) -> changeSpeed(1, state), F_SPEED_10);

		// inputMapper.add
	}

	private void changeSpeed(double delta, InputState state) {
		if (state == InputState.Off) {
			speedControl.limitSpeedTo(delta);
		}
	}

	private void changeSpeed(double direction, double tpf) {
		speedControl.applySpeedDelta(direction * tpf * 0.5f);
	}

	public static void main(String[] args) {
		SpeedBarTest app = new SpeedBarTest();

		AppSettings settings = new AppSettings(true);
		settings.setResolution(1600, 800);

		app.setSettings(settings);
		app.setShowSettings(false);

		app.start();
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);

		speedControl.update(tpf);

		speedBar.getModel().setPercent(speedControl.percent());
		speedBar.setMessage(String.format("%.05f", speedControl.percent()));
	}

}
