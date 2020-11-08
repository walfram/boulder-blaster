package test.cmn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.scene.Node;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;

public final class ScenePick implements StateFunctionListener {

	private static final Logger logger = LoggerFactory.getLogger(ScenePick.class);

	private final Node scene;
	private final Application app;

	public ScenePick(Node scene, Application app) {
		this.scene = scene;
		this.app = app;
	}

	@Override
	public void valueChanged(FunctionId func, InputState state, double tpf) {
		if (state != InputState.Off)
			return;

		Ray ray = new MouseClickRay(app).ray();
		CollisionResults results = new CollisionResults();
		int collisions = scene.collideWith(ray, results);

		if (collisions == 0) {
			logger.debug("no collisions");
			return;
		}

		CollisionResult collision = results.getClosestCollision();

		logger.debug("collision with {} at {}", collision.getGeometry(), collision.getContactPoint());
	}

}
