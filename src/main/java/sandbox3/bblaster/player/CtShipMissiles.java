package sandbox3.bblaster.player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.jme3.math.Transform;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;

public final class CtShipMissiles extends SimpleControl {

	private final List<Spatial> missiles;

	public CtShipMissiles(Spatial... missiles) {
		this(Arrays.asList(missiles));
	}

	public CtShipMissiles(List<Spatial> missiles) {
		this.missiles = missiles;
	}

	public List<Transform> transforms() {
		return missiles.stream().map(spatial -> spatial.getWorldTransform().clone()).collect(Collectors.toList());
	}

}
