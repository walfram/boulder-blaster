package sandbox3.bblaster.player;

import com.jme3.math.Transform;
import com.jme3.scene.Node;

import jme3utilities.SimpleControl;

public final class CtTransformCopy extends SimpleControl {

	private final Node source;

	public CtTransformCopy(Node source) {
		this.source = source;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		// from source we get world
		Transform t = source.getWorldTransform().clone();

		// set to local
		spatial.setLocalTransform(t);
	}

}
