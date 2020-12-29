package sandbox3.bblaster.explosions;

import java.util.Deque;

import com.jme3.effect.ParticleEmitter;
import com.jme3.scene.Node;

import jme3utilities.SimpleControl;

public final class FxExplosion extends Node {

	public FxExplosion(Deque<Node> deque, ParticleEmitter... emitters) {
		super("fx-explosion");

		for (ParticleEmitter emitter : emitters) {
			attachChild(emitter);
		}

		addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				// true because &= is "a & b"
				boolean done = true;

				for (ParticleEmitter emitter : emitters) {
					done &= emitter.getNumVisibleParticles() < 1;
				}

				if (!done)
					return;

				FxExplosion.this.removeFromParent();
				deque.push(FxExplosion.this);
			}
		});
	}

}
