package sandbox3.bblaster;

import java.util.function.Consumer;

import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;

final class CtTimeout extends SimpleControl {

	private final float timeout;
	private final Consumer<Spatial> callback;

	private float elapsed;

	public CtTimeout(float seconds, Consumer<Spatial> callback) {
		this.timeout = seconds;
		this.callback = callback;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		elapsed += updateInterval;

		if (elapsed > timeout) {
			callback.accept(getSpatial());
		}
	}

}
