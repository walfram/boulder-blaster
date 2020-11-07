package test.ship;

import com.jme3.app.Application;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

final class MouseRay {

	private final Application app;

	public MouseRay(Application app) {
		this.app = app;
	}

	public Ray ray() {
		Vector2f click2d = app.getInputManager().getCursorPosition();
		Vector3f origin = app.getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();

		Vector3f direction = app.getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(origin)
				.normalizeLocal();

		return new Ray(origin, direction);
	}

}
