package test.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.FlyCamAppState;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingSphere;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;
import jme3utilities.debug.BoundsVisualizer;
import jme3utilities.debug.PointVisualizer;
import sandbox3.bblaster.player.CtShipRoll;

final class StPlayer extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StPlayer.class);

	private final Node scene = new Node("scene");

	private Node player = new Node("player");

	public StPlayer(Node rootNode) {
		rootNode.attachChild(scene);
		scene.attachChild(player);
	}

	@Override
	protected void initialize(Application app) {
		Spatial hull = app.getAssetManager().loadModel("models/spacekit2/craft_speederD.obj");
		logger.debug("hull bound = {}", hull.getWorldBound());

		hull.setModelBound(new BoundingSphere(0.75f, new Vector3f(0, 0.45f, 0)));
		hull.scale(10f);
		player.attachChild(hull);

		player.addControl(new CtShipRoll());

		BoundsVisualizer boundsVisualizer = new BoundsVisualizer(app.getAssetManager());
		boundsVisualizer.setSubject(hull);
		scene.addControl(boundsVisualizer);
		boundsVisualizer.setEnabled(true);

		Node weaponsPrimary = new Node("weapons-primary");
		player.attachChild(weaponsPrimary);

		// TODO refactor
		weaponsPrimary.addControl(new CtCooldown(1f / 8f));

		Node weaponLeft = new Node("weapon-left");
		weaponLeft.setLocalTranslation(new Vector3f(14f, 2.5f, 1f));
		weaponLeft.attachChild(new PointVisualizer(app.getAssetManager(), 5, ColorRGBA.Red, null));
		weaponsPrimary.attachChild(weaponLeft);

		Node weaponRight = new Node("weapon-right");
		weaponRight.setLocalTranslation(new Vector3f(-14f, 2.5f, 1.f));
		weaponRight.attachChild(new PointVisualizer(app.getAssetManager(), 5, ColorRGBA.Red, null));
		weaponsPrimary.attachChild(weaponRight);

		Node cameraNode = new Node("camera-node");
		cameraNode.setLocalTranslation(new Vector3f(0, 15, -50));
		player.attachChild(cameraNode);

		getState(FlyCamAppState.class).setEnabled(false);
		cameraNode.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				app.getCamera().setLocation(cameraNode.getWorldTranslation());
				app.getCamera().setRotation(cameraNode.getWorldRotation());
			}
		});
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

	public void firePrimary(double value, double tpf) {
		// spawn projectiles at weapon-left + weapon-right' world translations
	}

	public void fireSecondary(double value, double tpf) {
	}

	public void moveForward(double value, double tpf) {
		Vector3f velocity = player.getLocalRotation().mult(Vector3f.UNIT_Z).mult((float) tpf).mult(25f);
		player.move(velocity);
	}

	public void roll(double value, double tpf) {
		player.getControl(CtShipRoll.class).roll(value, tpf);
	}

}
