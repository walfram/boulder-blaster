package sandbox3.bblaster;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;

import jme3.common.debug.NdDebugGrid;
import jme3.common.material.MtlUnshaded;
import jme3utilities.MyCamera;
import jme3utilities.debug.PointVisualizer;

public final class StHudRadar extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StHudRadar.class);

	private static final float RADAR_RANGE = 2048f;

	private final Node scene = new Node("radar-scene");

	private final Node gridWrap = new Node("grid-wrap");
	private final Node objectsWrap = new Node("objects-wrap");

	private float distanceScale;

	@Override
	protected void initialize(Application app) {
		Camera camera = new Camera(app.getCamera().getWidth(), app.getCamera().getHeight());
		camera.setViewPort(0.25f, 0.75f, 0f, 0.4f);

		camera.setParallelProjection(false);
		float ratio = MyCamera.displayAspectRatio(app.getCamera());
		camera.setFrustumPerspective(45, ratio, 0.1f, 1000f);

		logger.debug("camera = {}", camera);

		// ViewPort viewport = app.getRenderManager().createPostView("radar-view", camera);
		ViewPort viewport = app.getRenderManager().createMainView("radar-view", camera);

		// viewport.setClearFlags(false, false, true);
		viewport.setClearFlags(false, true, true);

		viewport.attachScene(scene);

		scene.attachChild(gridWrap);
		scene.attachChild(objectsWrap);

		NdDebugGrid grid = new NdDebugGrid(app.getAssetManager(), 8, 8, 40, ColorRGBA.Gray);
		gridWrap.attachChild(grid);
		logger.debug("grid bound = {}", grid.getWorldBound().clone());

		distanceScale = ((BoundingBox) grid.getWorldBound()).getXExtent() / RADAR_RANGE;

		PointVisualizer center = new PointVisualizer(app.getAssetManager(), 4, ColorRGBA.Green, null);
		center.setLocalTranslation(new Vector3f());
		gridWrap.attachChild(center);

		camera.setLocation(new Vector3f(0, 96, -256));
		camera.lookAt(new Vector3f(), Vector3f.UNIT_Y);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		Vector3f location = getApplication().getCamera().getLocation().clone();
		Quaternion rotation = getApplication().getCamera().getRotation().clone();

		objectsWrap.detachAllChildren();

		List<Spatial> objectsNear = getState(StBoulders.class).objectsNear(location, RADAR_RANGE);

		// float ratio = 140f / RADAR_RANGE;

		Node player = new Node("player");
		player.setLocalTranslation(location);
		player.setLocalRotation(rotation);

		for (Spatial s : objectsNear) {
			// https://hub.jmonkeyengine.org/t/radar-math-i-suck-at-this/29618/7
			// again, thanks to pspeed
			Vector3f localPos = s.getLocalTranslation().subtract(location);
			Vector3f v = rotation.inverse().mult(localPos);

			v.multLocal(distanceScale);

			ColorRGBA color = v.y > 0 ? ColorRGBA.Red : ColorRGBA.Yellow;

			PointVisualizer o = new PointVisualizer(getApplication().getAssetManager(), 5, color, null);
			o.setLocalTranslation(v);
			objectsWrap.attachChild(o);

			Vector3f end = new Vector3f(v.x, 0, v.z);
			Geometry line = new Geometry("line", new Line(v, end));
			line.setMaterial(new MtlUnshaded(getApplication().getAssetManager(), color));
			objectsWrap.attachChild(line);
		}

		scene.updateLogicalState(tpf);
		scene.updateGeometricState();
	}

	@Override
	protected void cleanup(Application app) {
		// IMPORTANT otherwise app freezes
		// scene.detachAllChildren();
		// getApplication().getRenderManager().removePostView("radar-view");
		getApplication().getRenderManager().removeMainView("radar-view");
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

}
