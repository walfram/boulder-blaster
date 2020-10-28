package sandbox3.bblaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import sandbox3.bblaster.controls.CtHealth;

public final class StTargetting extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StTargetting.class);
	
	private Spatial currentTarget;

	@Override
	protected void initialize(Application app) {
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

	@Override
	public void update(float tpf) {
		super.update(tpf);

		if (currentTarget != null) {
			if (currentTarget.getParent() == null) {
				currentTarget = null;
			}
		}
	}

	void aquireTarget() {
		Vector2f click2d = getApplication().getInputManager().getCursorPosition().clone();
		Vector3f click3d = getApplication().getCamera().getWorldCoordinates(click2d, 0f).clone();
		Vector3f dir = getApplication().getCamera().getWorldCoordinates(click2d, 1f).subtractLocal(click3d).normalizeLocal();

		Ray ray = new Ray(click3d, dir);
		CollisionResults results = new CollisionResults();

		currentTarget = null;
		getState(StBoulders.class).findTargets(ray, results);

		if (results.size() > 0) {
			currentTarget = results.getClosestCollision().getGeometry();
		} else {
			ray = new Ray(getApplication().getCamera().getLocation(), getApplication().getCamera().getDirection());
			getState(StBoulders.class).findTargets(ray, results);

			if (results.size() > 0) {
				currentTarget = results.getClosestCollision().getGeometry();
			}
		}
		
		logger.debug("aquire target = {}", currentTarget);
	}

	public Spatial currentTarget() {
		return currentTarget;
	}

	public float healthPercent() {
		if (currentTarget != null) {
			return currentTarget.getControl(CtHealth.class).percent();
		}

		return Float.NaN;
	}

	public float healthValue() {
		if (currentTarget != null) {
			return currentTarget.getControl(CtHealth.class).value();
		}

		return Float.NaN;
	}

	public float healthMax() {
		if (currentTarget != null) {
			return currentTarget.getControl(CtHealth.class).max();
		}
		
		return Float.NaN;
	}

}
