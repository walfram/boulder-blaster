package common.controls;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import jme3utilities.SimpleControl;

public final class CtCameraFollow extends SimpleControl {

	private final Camera camera;
	private final Vector3f cameraOffset;

	private final float distanceDamping;
	private final float rotataionDamping;
	
	public CtCameraFollow(Camera camera, Vector3f cameraOffset) {
		this(camera, cameraOffset, 2.0f, 3.0f);
	}
	
	public CtCameraFollow(Camera camera, Vector3f cameraOffset, float distanceDamping, float rotataionDamping) {
		this.camera = camera;
		this.cameraOffset = cameraOffset;
		this.distanceDamping = distanceDamping;
		this.rotataionDamping = rotataionDamping;
	}

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);

		Vector3f origin = spatial.getLocalTranslation().clone();
		Quaternion rot = spatial.getLocalRotation().clone();
		Vector3f offsetRotated = rot.mult(cameraOffset);

		Vector3f interpolated = camera.getLocation().clone().interpolateLocal(origin.add(offsetRotated), distanceDamping * updateInterval);
		camera.setLocation(interpolated);

		Quaternion slerped = camera.getRotation().clone();
		slerped.slerp(rot, rotataionDamping * updateInterval);

		camera.setRotation(slerped);

	}

}
