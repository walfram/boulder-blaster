package test.radar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;

import test.cmn.MouseClickRay;

final class StPick extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StPick.class);

	private static final FunctionId F_PICK = new FunctionId("pick");

	@Override
	protected void initialize(Application app) {
		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();

		inputMapper.map(F_PICK, Button.MOUSE_BUTTON2);
		inputMapper.addStateListener((func, state, tpf) -> pick(state, tpf), F_PICK);
	}

	private void pick(InputState state, double tpf) {
		if (state != InputState.Off)
			return;

		Ray ray = new MouseClickRay(getApplication()).ray();
		CollisionResults results = new CollisionResults();

		getState(StObjects.class).collideWith(ray, results);

		if (results.size() > 0) {
			CollisionResult cc = results.getClosestCollision();
			logger.debug("pick = {}, distance = {}", cc.getGeometry(), cc.getDistance());
		} else {
			logger.debug("nothing picked");
		}
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
