package sandbox3.bblaster.player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.jme3.math.Transform;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;

public final class CtShipBlasterProjectiles extends SimpleControl {

	private final List<Spatial> spatials;

	public CtShipBlasterProjectiles(Spatial... spatials) {
		this(Arrays.asList(spatials));
	}

	public CtShipBlasterProjectiles(List<Spatial> spatials) {
		this.spatials = spatials;
	}

	public List<Transform> transforms() {
		return spatials.stream().map(s -> s.getWorldTransform().clone()).collect(Collectors.toList());
	}

}
