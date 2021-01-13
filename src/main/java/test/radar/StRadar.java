package test.radar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.bounding.BoundingVolume;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import jme3utilities.SimpleControl;
import jme3utilities.debug.AxesVisualizer;
import jme3utilities.debug.BoundsVisualizer;

final class StRadar extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(StRadar.class);

	private final Node scene = new Node("radar-scene");

	@Override
	protected void initialize(Application app) {
		Camera camera = new Camera(1600, 800);
		camera.setViewPort(0.3f, 0.7f, 0f, 0.4f);
		
		camera.setParallelProjection(false);
		camera.setFrustumPerspective(45, 1600f / 800f, 0.1f, 500f);
		
		logger.debug("camera = {}", camera);

		ViewPort viewport = app.getRenderManager().createPostView("radar view", camera);

		viewport.setClearFlags(false, true, true);
		// viewport.setBackgroundColor(ColorRGBA.Gray);

		viewport.attachScene(scene);

		Spatial model = app.getAssetManager().loadModel("Models/Teapot/Teapot.obj");
		model.scale(5f);
		logger.debug("model = {}", model);
		logger.debug("model.bound = {}", model.getWorldBound());

		BoundingVolume bound = model.getWorldBound().clone();
		
		camera.setLocation(new Vector3f(0, bound.getCenter().y, 10));
		camera.lookAt(new Vector3f(0, bound.getCenter().y, 0), Vector3f.UNIT_Y);
		
		scene.attachChild(model);

		scene.addLight(new DirectionalLight(Vector3f.UNIT_XYZ.negate(), ColorRGBA.White));

		AxesVisualizer axesVisualizer = new AxesVisualizer(app.getAssetManager(), 50, 3);
		scene.addControl(axesVisualizer);
		axesVisualizer.setEnabled(true);

		BoundsVisualizer boundsVisualizer = new BoundsVisualizer(app.getAssetManager());
		boundsVisualizer.setSubject(model);
		scene.addControl(boundsVisualizer);
		boundsVisualizer.setEnabled(true);

		model.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);
				getSpatial().rotate(0, FastMath.DEG_TO_RAD * 10f * updateInterval, 0);
			}
		});
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		scene.updateLogicalState(tpf);
		scene.updateGeometricState();
	}

	@Override
	protected void cleanup(Application app) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onEnable() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDisable() {
		// TODO Auto-generated method stub

	}

}
