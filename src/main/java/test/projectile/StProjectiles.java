package test.projectile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.focus.FocusNavigationState;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;

import jme3.common.material.MtlUnshaded;
import sandbox3.bblaster.misc.Cooldown;
import sandbox3.bblaster.projectiles.NdProjectile;

final class StProjectiles extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StProjectiles.class);

	private static final FunctionId F_SHOOT = new FunctionId("shoot");

	private final Node scene = new Node("projectiles");

	private final Cooldown cooldown = new Cooldown(1f / 8f);

	private Mesh mesh;
	private Material material;

	public StProjectiles(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		logger.debug("cooldown rof = {}", cooldown.rof());

		mesh = new Box(0.25f, 0.25f, 15f);
		material = new MtlUnshaded(app.getAssetManager(), ColorRGBA.Yellow);

		getState(FocusNavigationState.class).setEnabled(false);

		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();

		inputMapper.map(F_SHOOT, KeyInput.KEY_SPACE);
		inputMapper.addAnalogListener((func, value, tpf) -> shoot(value, tpf), F_SHOOT);
	}

	private void shoot(double value, double tpf) {
		cooldown.update((float) tpf);
		if (!cooldown.isReady())
			return;

		cooldown.reset();

		logger.debug("spawning projectile");

		Node projectile = new NdProjectile(getApplication().getAssetManager(), mesh, material);

		projectile.setLocalTranslation(new Vector3f());
		projectile.getLocalRotation().lookAt(getApplication().getCamera().getDirection(), Vector3f.UNIT_Y);

		scene.attachChild(projectile);

		// Geometry clone = projectile.clone(true);
		// clone.setLocalTranslation(new Vector3f());
		// clone.getLocalRotation().lookAt(getApplication().getCamera().getDirection(), Vector3f.UNIT_Y);
		//
		// final float projectileSpeed = 5000f;
		//
		// clone.addControl(new SimpleControl() {
		// private float time = 0f;
		//
		// @Override
		// protected void controlUpdate(float updateInterval) {
		// super.controlUpdate(updateInterval);
		//
		// Vector3f forward = spatial.getLocalRotation().mult(Vector3f.UNIT_Z);
		// spatial.move(forward.mult(projectileSpeed * updateInterval));
		//
		// float distance = spatial.getLocalTranslation().distance(Vector3f.ZERO);
		// int s = 1 + (int) (distance / (projectileSpeed * 0.1));
		// spatial.setLocalScale(s);
		//
		// time += updateInterval;
		// if (time > 5f) {
		// logger.debug("removed, distance = {}", spatial.getLocalTranslation().distance(new Vector3f()));
		// logger.debug("final bound = {}", spatial.getWorldBound());
		// spatial.removeFromParent();
		// }
		// }
		// });
		//
		// scene.attachChild(clone);
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

}
