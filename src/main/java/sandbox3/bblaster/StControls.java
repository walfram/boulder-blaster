package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.jme3.math.Vector2f;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;

final class StControls extends BaseAppState {

	private static final String GROUP_CONTROLS = "group-controls";

	private static final FunctionId F_FIRE_MISSILE = new FunctionId(GROUP_CONTROLS, "fire-missile");
	private static final FunctionId F_AQUIRE_TARGET = new FunctionId(GROUP_CONTROLS, "aquire-target");
	private static final FunctionId F_FIRE_GUNS = new FunctionId(GROUP_CONTROLS, "fire-guns");

	private static final FunctionId F_MOUSE_LOOK = new FunctionId(GROUP_CONTROLS, "mouse-look");

	private static final FunctionId F_ROLL_CCW = new FunctionId(GROUP_CONTROLS, "roll-ccw");
	private static final FunctionId F_ROLL_CW = new FunctionId(GROUP_CONTROLS, "roll-cw");

	private static final FunctionId F_THRUST_UP = new FunctionId(GROUP_CONTROLS, "thrust-up");
	private static final FunctionId F_THRUST_DOWN = new FunctionId(GROUP_CONTROLS, "thrust-down");

	private boolean mouseLook = false;

	private float hw;
	private float hh;

	@Override
	protected void initialize(Application app) {
		hw = app.getContext().getSettings().getWidth() * 0.5f;
		hh = app.getContext().getSettings().getHeight() * 0.5f;
		
		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();

		inputMapper.map(F_FIRE_MISSILE, KeyInput.KEY_SPACE);
		inputMapper.addAnalogListener((func, value, tpf) -> getState(StPlayer.class).fireMissile(), F_FIRE_MISSILE);

		inputMapper.map(F_AQUIRE_TARGET, KeyInput.KEY_T);
		inputMapper.addAnalogListener((func, value, tpf) -> getState(StTargetting.class).aquireTarget(), F_AQUIRE_TARGET);

		inputMapper.map(F_FIRE_GUNS, Button.MOUSE_BUTTON2);
		inputMapper.addAnalogListener((func, value, tpf) -> getState(StPlayer.class).fireGuns(), F_FIRE_GUNS);

		inputMapper.map(F_MOUSE_LOOK, Button.MOUSE_BUTTON1);
		inputMapper.addAnalogListener((func, value, tpf) -> {
			Vector2f cp = app.getInputManager().getCursorPosition();

			mouseLook = value > 0;

			float dx = (cp.x - hw) / hw;
			float dy = (cp.y - hh) / hh;

			getState(StPlayer.class).yaw(dx, tpf);
			getState(StPlayer.class).pitch(dy, tpf);
		}, F_MOUSE_LOOK);

		inputMapper.map(F_ROLL_CCW, KeyInput.KEY_A);
		inputMapper.addAnalogListener((func, value, tpf) -> getState(StPlayer.class).roll(-value, tpf), F_ROLL_CCW);

		inputMapper.map(F_ROLL_CW, KeyInput.KEY_D);
		inputMapper.addAnalogListener((func, value, tpf) -> getState(StPlayer.class).roll(value, tpf), F_ROLL_CW);

		inputMapper.map(F_THRUST_UP, KeyInput.KEY_W);
		inputMapper.addAnalogListener((func, value, tpf) -> getState(StPlayer.class).updateThrust(tpf), F_THRUST_UP);

		inputMapper.map(F_THRUST_DOWN, KeyInput.KEY_S);
		inputMapper.addAnalogListener((func, value, tpf) -> getState(StPlayer.class).updateThrust(-tpf), F_THRUST_DOWN);
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
		GuiGlobals.getInstance().getInputMapper().activateGroup(GROUP_CONTROLS);
	}

	@Override
	protected void onDisable() {
		GuiGlobals.getInstance().getInputMapper().deactivateGroup(GROUP_CONTROLS);
	}

	public boolean isMouseLook() {
		return mouseLook;
	}

}
