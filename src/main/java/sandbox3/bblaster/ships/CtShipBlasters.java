package sandbox3.bblaster.ships;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;

public final class CtShipBlasters extends SimpleControl {

	private final List<Spatial> blasters;

	public CtShipBlasters(Spatial... blasters) {
		this(Arrays.asList(blasters));
	}

	public CtShipBlasters(List<Spatial> blasters) {
		this.blasters = blasters;
	}

	public List<Vector3f> offsets() {
		return blasters.stream().map(s -> s.getLocalTranslation().clone()).collect(Collectors.toList());
	}

	public List<Transform> transforms() {
		return blasters.stream().map(s -> s.getWorldTransform().clone()).collect(Collectors.toList());
	}

}
