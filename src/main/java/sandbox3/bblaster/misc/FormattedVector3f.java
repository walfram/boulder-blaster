package sandbox3.bblaster.misc;

import com.jme3.math.Vector3f;

public final class FormattedVector3f {

	private final Vector3f vector;

	public FormattedVector3f(Vector3f vector) {
		this.vector = vector;
	}

	public String format() {
		return String.format("x=%.03f, y=%.03f, z=%.03f", vector.x, vector.y, vector.z);
	}

}
