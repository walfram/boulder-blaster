package sandbox3.bblaster.misc;

import java.util.function.BiConsumer;

import com.jme3.collision.CollisionResult;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;

public final class CtCollision extends SimpleControl {

	private final BiConsumer<Spatial, CollisionResult> consumer;

	public CtCollision(BiConsumer<Spatial, CollisionResult> consumer) {
		this.consumer = consumer;
	}

	public void collideWith(Spatial other, CollisionResult collision) {
		consumer.accept(other, collision);
	}

}
