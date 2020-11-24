package test.boulder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

import jme3utilities.math.MyVector3f;

public class AzimuthTest {

	private static final Logger logger = LoggerFactory.getLogger(AzimuthTest.class);
	
	public static void main(String[] args) {
		for (int i = 0; i < 360; i++) {
			float x = 10f * FastMath.sin(FastMath.DEG_TO_RAD * i);
			float z = 10f * FastMath.cos(FastMath.DEG_TO_RAD * i);

			float azimuth = MyVector3f.azimuth(new Vector3f(x, 0, z));
			logger.debug("i = {}, azimuth = {}", i, azimuth);
		}
	}

}
