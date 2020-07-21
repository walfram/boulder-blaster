package sandbox3.bblaster;

import java.util.function.Consumer;

import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;

final class CtCollision extends SimpleControl {

	private final Consumer<Spatial> consumer;

	public CtCollision(Consumer<Spatial> consumer) {
		this.consumer = consumer;
	}

	public void collideWith(Spatial other) {
		consumer.accept(other);
	}

}
