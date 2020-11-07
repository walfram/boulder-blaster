package test;

import com.jme3.math.FastMath;

import jme3utilities.SimpleControl;

public final class SpeedControl extends SimpleControl {

	private float value = 0f;
	
	private double speedLimit = 0;
	private double changeDirection = 0;

	@Override
	protected void controlUpdate(float updateInterval) {
		super.controlUpdate(updateInterval);
		
		if (changeDirection == 0)
			return;
		
		value += changeDirection * updateInterval * 0.5f;
		
		if (changeDirection > 0) {
			if (value >= speedLimit) {
				changeDirection = 0;
				value = (float) speedLimit;
			}
		}
		
		if (changeDirection < 0) {
			if (value <= speedLimit) {
				changeDirection = 0;
				value = (float) speedLimit;
			}
		}
	}

	public double percent() {
		return value;
	}

	public void applySpeedDelta(double delta) {
		value = FastMath.clamp(value + (float) delta, 0, 1);
	}

	public void limitSpeedTo(double limit) {
		speedLimit = limit;
		changeDirection = Math.signum(limit - value);
	}

}
