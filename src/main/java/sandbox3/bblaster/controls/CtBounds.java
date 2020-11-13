package sandbox3.bblaster.controls;

import com.jme3.math.Vector3f;

import jme3utilities.SimpleControl;

// TODO rename this class to better reflect it's purpose
@Deprecated
public final class CtBounds extends SimpleControl {

	private final float boundary;

	public CtBounds(float boundary) {
		this.boundary = boundary;
	}
	
	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		Vector3f v = spatial.getLocalTranslation();

		if (Math.abs(v.x) > boundary || Math.abs(v.y) > boundary || Math.abs(v.z) > boundary) {
			if (v.x > boundary) {
				v.x = -boundary;
			}
			if (v.x < -boundary) {
				v.x = boundary;
			}

			if (v.y > boundary) {
				v.y = -boundary;
			}
			if (v.y < -boundary) {
				v.y = boundary;
			}

			if (v.z > boundary) {
				v.z = -boundary;
			}
			if (v.z < -boundary) {
				v.z = boundary;
			}

			spatial.setLocalTranslation(v);
		}
	}

}
