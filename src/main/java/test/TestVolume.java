package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.bounding.BoundingSphere;
import com.jme3.math.Vector3f;

public class TestVolume {

	private static final Logger logger = LoggerFactory.getLogger(TestVolume.class);

	public static void main(String[] args) {
		float radius = 5.12f;
		BoundingSphere origin = new BoundingSphere(radius, new Vector3f());
		logger.debug("origin volume = {}", String.format("%10.5f", origin.getVolume()));

		float r = radius * 0.75f;
		BoundingSphere bound = new BoundingSphere(r, new Vector3f());
		logger.debug("0.5 sphere volume = {}", String.format("%10.5f", bound.getVolume()));

		logger.debug("ratio = {}", String.format("%10.5f", origin.getVolume() / (2f * bound.getVolume())));
	}

}
