package test.player;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.focus.FocusNavigationState;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;

final class StControls extends BaseAppState {

	private static final FunctionId F_FIRE_PRIMARY = new FunctionId("fire-primary");
	private static final FunctionId F_FIRE_SECONDARY = new FunctionId("fire-secondary");
	private static final FunctionId F_FORWARD = new FunctionId("forward");
	private static final FunctionId F_ROLL_CW = new FunctionId("roll-cw");
	private static final FunctionId F_ROLL_CCW = new FunctionId("roll-ccw");

	@Override
	protected void initialize(Application app) {
		getState(FocusNavigationState.class).setEnabled(false);

		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();

		inputMapper.map(F_FIRE_PRIMARY, Button.MOUSE_BUTTON2);
		inputMapper.addAnalogListener((func, value, tpf) -> getState(StPlayer.class).firePrimary(value, tpf), F_FIRE_PRIMARY);

		inputMapper.map(F_FIRE_SECONDARY, KeyInput.KEY_SPACE);
		inputMapper.addAnalogListener(
				(func, value, tpf) -> getState(StPlayer.class).fireSecondary(value, tpf),
				F_FIRE_SECONDARY);

		inputMapper.map(F_FORWARD, KeyInput.KEY_W);
		inputMapper.addAnalogListener((func, value, tpf) -> getState(StPlayer.class).moveForward(value, tpf), F_FORWARD);

		inputMapper.map(F_ROLL_CW, KeyInput.KEY_D);
		inputMapper.addAnalogListener((func, value, tpf) -> getState(StPlayer.class).roll(value, tpf), F_ROLL_CW);

		inputMapper.map(F_ROLL_CCW, KeyInput.KEY_A);
		inputMapper.addAnalogListener((func, value, tpf) -> getState(StPlayer.class).roll(-value, tpf), F_ROLL_CCW);
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

}
