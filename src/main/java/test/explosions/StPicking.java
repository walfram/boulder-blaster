package test.explosions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;

import test.cmn.MouseClickRay;

final class StPicking extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StPicking.class);

	private static final FunctionId F_CLICK = new FunctionId("click");

	@Override
	protected void initialize(Application app) {
		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();

		inputMapper.map(F_CLICK, Button.MOUSE_BUTTON2);
		inputMapper.addStateListener((func, state, tpf) -> onClick(state, tpf), F_CLICK);
	}

	private void onClick(InputState state, double tpf) {
		if (state != InputState.Off)
			return;

		Ray ray = new MouseClickRay(getApplication()).ray();
		CollisionResults results = new CollisionResults();

		int collisions = getState(StTargets.class).collisionWith(ray, results);

		if (collisions == 0) {
			logger.debug("no collisions, ray = {}", ray);
			return;
		}

		CollisionResult closest = results.getClosestCollision();
		Vector3f translation = closest.getContactPoint();
		getState(StExplosions.class).createExplosion(translation, 30f);
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
