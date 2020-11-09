package test.ship;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.simsilica.lemur.event.DefaultMouseListener;
import com.simsilica.lemur.event.MouseEventControl;

import jme3.common.material.MtlLighting;
import jme3utilities.math.MyVector3f;
import jme3utilities.math.noise.Generator;

final class StTargets extends BaseAppState {

	private final Node scene = new Node("targets");

	public StTargets(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Generator random = new Generator(37);

		Mesh mesh = new Box(1, 1, 1);
		Material material = new MtlLighting(app.getAssetManager(), ColorRGBA.Gray);

		for (int idx = 0; idx < 128; idx++) {
			Geometry target = new Geometry("target#" + idx, mesh);
			target.setMaterial(material);

			float azimuth = random.nextFloat(FastMath.QUARTER_PI, FastMath.PI - FastMath.QUARTER_PI);
			float altitude = random.nextFloat(-FastMath.DEG_TO_RAD * 30f, FastMath.DEG_TO_RAD * 30f);

			Vector3f translation = MyVector3f.fromAltAz(altitude, azimuth).mult(1024f);
			target.setLocalTranslation(translation);

			float scale = random.nextFloat(10, 20f);
			target.scale(scale);

			scene.attachChild(target);

			MouseEventControl.addListenersToSpatial(target, new DefaultMouseListener() {
				@Override
				protected void click(MouseButtonEvent event, Spatial target, Spatial capture) {
					getState(StShip.class).selectTarget(target);
				}
			});
		}
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
