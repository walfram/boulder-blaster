package sandbox3.bblaster.player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;

public final class CtShipEmissions extends SimpleControl {

	private final List<Spatial> emissions;

	public CtShipEmissions(Spatial... emissions) {
		this(Arrays.asList(emissions));
	}

	public CtShipEmissions(List<Spatial> emissions) {
		this.emissions = emissions;
	}

	public List<Vector3f> offsets() {
		return emissions.stream().map(s -> s.getLocalTranslation().clone()).collect(Collectors.toList());
	}

}
