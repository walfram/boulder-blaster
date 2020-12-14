package sandbox3.bblaster.ships;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.jme3.math.Transform;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;

public final class CtShipBlasterFx extends SimpleControl {

	private final List<Spatial> blasterFx;

	public CtShipBlasterFx(Spatial... blasterFx) {
		this(Arrays.asList(blasterFx));
	}

	public CtShipBlasterFx(List<Spatial> blasterFx) {
		this.blasterFx = blasterFx;
	}

	// public List<Vector3f> offsets() {
	// return blasters.stream().map(s -> s.getLocalTranslation().clone()).collect(Collectors.toList());
	// }

	public List<Transform> transforms() {
		return blasterFx.stream().map(s -> s.getWorldTransform().clone()).collect(Collectors.toList());
	}

}
