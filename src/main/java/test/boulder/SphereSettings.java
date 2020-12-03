package test.boulder;

import com.jme3.math.Vector3f;

public final class SphereSettings {

	public float noiseScale = 1f;
	public float scalex = 1f;
	public float scaley = 1f;
	public float scalez = 1f;
	
	public Vector3f scale() {
		return new Vector3f(scalex, scaley, scalez);
	}

}
