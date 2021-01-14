package test.radar;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.PlaceholderAssets;

import jme3.common.debug.NdDebugGrid;
import jme3utilities.debug.PointVisualizer;
import jme3utilities.math.MyVector3f;

final class StRadar extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StRadar.class);

	private final Node scene = new Node("radar-scene");

	private final Node gridWrap = new Node("grid-wrap");
	private final Node objectsWrap = new Node("objects-wrap");

	@Override
	protected void initialize(Application app) {
		Camera camera = new Camera(1600, 800);
		camera.setViewPort(0.3f, 0.7f, 0f, 0.5f);

		camera.setParallelProjection(false);
		camera.setFrustumPerspective(45, 1600f / 800f, 0.1f, 500f);

		logger.debug("camera = {}", camera);

		ViewPort viewport = app.getRenderManager().createPostView("radar-view", camera);

		// viewport.setClearFlags(false, false, true);
		viewport.setClearFlags(true, true, true);

		viewport.attachScene(scene);

		scene.attachChild(gridWrap);
		scene.attachChild(objectsWrap);

		// Geometry base = new Geometry("base", new MBox(128, 0, 128, 9, 0, 9));
		// base.setMaterial(new MtlUnshaded(app.getAssetManager(), ColorRGBA.White));
		// base.getMaterial().setTexture("ColorMap", app.getAssetManager().loadTexture("textures/proto/texture_01.png"));
		// base.getMaterial().getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		// base.setQueueBucket(Bucket.Transparent);
		// scene.attachChild(base);

		NdDebugGrid grid = new NdDebugGrid(app.getAssetManager(), 8, 8, 40, ColorRGBA.Gray);
		gridWrap.attachChild(grid);

		logger.debug("grid bound = {}", grid.getWorldBound().clone());

		camera.setLocation(new Vector3f(0, 96, -256));
		camera.lookAt(new Vector3f(), Vector3f.UNIT_Y);

		Spatial top = PlaceholderAssets.getPlaceholderModel(app.getAssetManager());
		top.move(0, 5, 0);
		gridWrap.attachChild(top);

		Spatial bottom = PlaceholderAssets.getPlaceholderModel(app.getAssetManager());
		bottom.move(0, -5, 0);
		gridWrap.attachChild(bottom);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		Vector3f location = getApplication().getCamera().getLocation().clone();

		Vector3f direction = getApplication().getCamera().getDirection().clone();
		Quaternion rotation = getApplication().getCamera().getRotation().clone();

		objectsWrap.detachAllChildren();

		List<Spatial> objectsNear = getState(StObjects.class).objectsNear(location);

		float ratio = 120f / 512f;

		Node player = new Node("player");
		player.setLocalTranslation(location);
		player.setLocalRotation(rotation);

		for (Spatial s : objectsNear) {
			// https://hub.jmonkeyengine.org/t/radar-math-i-suck-at-this/29618/7
			// again, thanks to pspeed
			Vector3f localPos = s.getLocalTranslation().subtract(location);
			Vector3f v = rotation.inverse().mult(localPos);

			v.multLocal(ratio);

			PointVisualizer o = new PointVisualizer(getApplication().getAssetManager(), 5, ColorRGBA.Yellow, null);

			if (v.y > 0) {
				o.setColor(ColorRGBA.Red);
			}

			o.setLocalTranslation(v);

			objectsWrap.attachChild(o);
		}

		scene.updateLogicalState(tpf);
		scene.updateGeometricState();
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
		// IMPORTANT otherwise app freezes
		// scene.detachAllChildren();
		getApplication().getRenderManager().removePostView("radar-view");
	}

}
