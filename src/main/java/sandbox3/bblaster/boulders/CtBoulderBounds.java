package sandbox3.bblaster.boulders;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

public final class CtBoulderBounds extends SimpleControl {

	private final float boundary;

	public CtBoulderBounds(float boundary) {
		this.boundary = boundary;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		Vector3f v = spatial.getLocalTranslation();

		if (Math.abs(v.x) > boundary) {
			v.x = FastMath.sign(v.x) * -1f * boundary;
		}

		if (Math.abs(v.y) > boundary) {
			v.y = FastMath.sign(v.y) * -1f * boundary;
		}

		if (Math.abs(v.z) > boundary) {
			v.z = FastMath.sign(v.z) * -1f * boundary;
		}

		spatial.setLocalTranslation(v);
	}

}
