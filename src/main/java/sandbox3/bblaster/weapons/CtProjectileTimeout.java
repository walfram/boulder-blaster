package sandbox3.bblaster.weapons;

import java.util.function.Consumer;

import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;

public final class CtProjectileTimeout extends SimpleControl {

	private final float timeout;
	private final Consumer<Spatial> callback;

	private float elapsed;

	public CtProjectileTimeout(float seconds, Consumer<Spatial> callback) {
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
