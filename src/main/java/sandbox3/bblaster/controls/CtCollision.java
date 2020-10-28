package sandbox3.bblaster.controls;

import java.util.function.Consumer;

import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;

public final class CtCollision extends SimpleControl {

	private final Consumer<Spatial> consumer;

	public CtCollision(Consumer<Spatial> consumer) {
		this.consumer = consumer;
	}

	public void collideWith(Spatial other) {
		consumer.accept(other);
	}

}
