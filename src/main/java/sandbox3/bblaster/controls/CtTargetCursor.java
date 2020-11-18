package sandbox3.bblaster.controls;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import jme3utilities.SimpleControl;
import sandbox3.bblaster.StTargetting;

public final class CtTargetCursor extends SimpleControl {

	private final StTargetting stTargetting;
	private final Camera camera;

	private final int width;
	private final int height;

	public CtTargetCursor(StTargetting stTargetting, Camera camera) {
		this.stTargetting = stTargetting;
		this.camera = camera;
		this.width = camera.getWidth();
		this.height = camera.getHeight();
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
			else if (xy.x > (width - 72))
				xy.x = width - 72;

			if (xy.y < 0)
				xy.y = 0;
			else if (xy.y > (height - 72))
				xy.y = height - 72;

			if (xy.z > 1) {
				// TODO this is not quite correct, must refactor
				float dx = xy.x / width;
				float dy = xy.y / height;
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
