package test.boulder.prepared2;

import com.google.common.base.MoreObjects;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;

final class Face {

	private final Triangle triangle;

	public Face(Vector3f a, Vector3f b, Vector3f c) {
		this.triangle = new Triangle(a, b, c);
	}

	public boolean contains(Vector3f v) {
		if (triangle.get1().equals(v))
			return true;

		if (triangle.get2().equals(v))
			return true;

		if (triangle.get3().equals(v))
			return true;

		return false;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).addValue(triangle.get1()).addValue(triangle.get2()).addValue(triangle.get3())
				.toString();
	}

	public Vector3f v1() {
		return triangle.get1();
	}

	public Vector3f v2() {
		return triangle.get2();
	}

	public Vector3f v3() {
		return triangle.get3();
	}

	public Vector3f normal() {
		return triangle.getNormal();
	}

	public Vector3f vertex(Vector3f vertex) {
		if (triangle.get1().equals(vertex))
			return triangle.get1();

		if (triangle.get2().equals(vertex))
			return triangle.get2();

		if (triangle.get3().equals(vertex))
			return triangle.get3();

		return Vector3f.NAN;
	}

}
