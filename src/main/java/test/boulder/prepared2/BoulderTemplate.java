package test.boulder.prepared2;

import com.jme3.scene.Mesh;

import jme3utilities.mesh.Octasphere;

final class BoulderTemplate {

	private final int steps;
	private final float radius;

	public BoulderTemplate(int steps, float radius) {
		this.steps = steps;
		this.radius = radius;
	}

	public Mesh mesh() {
		return new Octasphere(steps, radius);
	}

	public float radius() {
		return radius;
	}

	public float noiseScale() {
		return radius * 0.63f;
	}

}
