package sandbox3.bblaster;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import jme3utilities.SimpleControl;

final class CtTargetCursor extends SimpleControl {

	private final StTargetting stTargetting;
	private final Camera camera;

	public CtTargetCursor(StTargetting stTargetting, Camera camera) {
		this.stTargetting = stTargetting;
		this.camera = camera;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		if (stTargetting.currentTarget() == null) {
			// TODO remove this?
		} else {
			Vector3f position = stTargetting.currentTarget().getLocalTranslation();
			Vector3f xy = camera.getScreenCoordinates(position);
			xy.subtractLocal(new Vector3f(72, 72, 0).mult(0.5f));

			if (xy.x < 0)
				xy.x = 0f;
			else if (xy.x > (1600 - 72))
				xy.x = 1600 - 72;

			if (xy.y < 0)
				xy.y = 0;
			else if (xy.y > (800 - 72))
				xy.y = 800 - 72;

			if (xy.z > 1) {
				// TODO this is not quite correct, must refactor
				float dx = xy.x / 1600;
				float dy = xy.y / 800;
				if (dx > dy) {
					xy.x = 0f;
				} else {
					xy.y = 0f;
				}
			}

			spatial.setLocalTranslation(xy);
		}

	}

}
