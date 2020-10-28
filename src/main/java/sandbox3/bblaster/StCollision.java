package sandbox3.bblaster;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Spatial;

import sandbox3.bblaster.controls.CtCollision;

public final class StCollision extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StCollision.class);

	private final List<Spatial> collidables = new ArrayList<>(4096);

	// private final List<Spatial> cleanup = new ArrayList<>(1024);

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
		collidables.add(spatial);
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

//				logger.debug("collision between {} and {}", outer, inner);
				
				inner.getControl(CtCollision.class).collideWith(outer);
				outer.getControl(CtCollision.class).collideWith(inner);
			}
		}

		// collidables.removeAll(cleanup);
		// cleanup.clear();
	}

	public void unregister(Spatial spatial) {
		collidables.remove(spatial);
	}

}
