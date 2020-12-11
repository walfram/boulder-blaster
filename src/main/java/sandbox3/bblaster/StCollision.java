package sandbox3.bblaster;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;
import sandbox3.bblaster.controls.CtCollision;

public final class StCollision extends BaseAppState {

	private static final class CtUnregister extends SimpleControl {
	}

	private static final Logger logger = LoggerFactory.getLogger(StCollision.class);

	private final List<Spatial> registered = new ArrayList<>(1024);
	private final List<Spatial> collidables = new ArrayList<>(4096);

	@Override
	protected void initialize(Application app) {
		logger.debug("collisions initialized");
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

	public void register(Spatial spatial) {
		registered.add(spatial);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		for (int o = 0; o < collidables.size() - 1; o++) {
			Spatial outer = collidables.get(o);

			for (int i = o + 1; i < collidables.size(); i++) {
				Spatial inner = collidables.get(i);

				if (!outer.getWorldBound().intersects(inner.getWorldBound()))
					continue;

				logger.debug("collision {} with {}", outer, inner);

				CollisionResults results = new CollisionResults();
				outer.collideWith(inner.getWorldBound().clone(), results);
				CollisionResult collision = results.getClosestCollision();

				if (inner.getControl(CtUnregister.class) == null)
					inner.getControl(CtCollision.class).collideWith(outer, collision);

				if (outer.getControl(CtUnregister.class) == null)
					outer.getControl(CtCollision.class).collideWith(inner, collision);
			}
		}

		collidables.removeIf(s -> s.getControl(CtUnregister.class) != null);
		
		collidables.addAll(registered);
		registered.clear();
	}

	public void unregister(Spatial spatial) {
		// collidables.remove(spatial);
		boolean alreadyMarked = spatial.getControl(CtUnregister.class) != null;

		if (!alreadyMarked)
			spatial.addControl(new CtUnregister());
	}

}
