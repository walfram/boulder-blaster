package sandbox3.bblaster;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import sandbox3.bblaster.controls.CtCollision;
import sandbox3.bblaster.materials.MtlLighting;

public final class StStation extends BaseAppState {
	
	private static final Logger logger = LoggerFactory.getLogger(StStation.class);
	
	private final Node station = new Node("station");

	private final List<Spatial> docked = new ArrayList<>();
	
	public StStation(Node rootNode) {
		rootNode.attachChild(station);
	}

	@Override
	protected void initialize(Application app) {
		Material material = new MtlLighting(app.getAssetManager(), ColorRGBA.Gray);
		Mesh mesh = new Box(25f, 1f, 100f);
		
		Geometry topPlate = new Geometry("top-plate", mesh);
		topPlate.setMaterial(material);
		topPlate.move(0, 40, 0);
		station.attachChild(topPlate);

		Geometry leftTopPlate = new Geometry("left-top-plate", mesh);
		leftTopPlate.setMaterial(material);
		leftTopPlate.rotate(0, 0, 45f * FastMath.DEG_TO_RAD);
		leftTopPlate.move(-45, 20f, 0);
		station.attachChild(leftTopPlate);

		Geometry rightTopPlate = new Geometry("right-top-plate", mesh);
		rightTopPlate.setMaterial(material);
		rightTopPlate.rotate(0, 0, -45f * FastMath.DEG_TO_RAD);
		rightTopPlate.move(45, 20, 0);
		station.attachChild(rightTopPlate);
		
		Geometry bottomPlate = new Geometry("bottom-plate", mesh);
		bottomPlate.setMaterial(material);
		bottomPlate.rotate(0, 0, 180f * FastMath.DEG_TO_RAD);
		bottomPlate.move(0, -40, 0);
		station.attachChild(bottomPlate);

		Geometry leftBottomPlate = new Geometry("left-bottom-plate", mesh);
		leftBottomPlate.setMaterial(material);
		leftBottomPlate.rotate(0, 0, -225f * FastMath.DEG_TO_RAD);
		leftBottomPlate.move(-45, -20f, 0);
		station.attachChild(leftBottomPlate);

		Geometry rightBottomPlate = new Geometry("right-bottom-plate", mesh);
		rightBottomPlate.setMaterial(material);
		rightBottomPlate.rotate(0, 0, 225f * FastMath.DEG_TO_RAD);
		rightBottomPlate.move(45, -20, 0);
		station.attachChild(rightBottomPlate);
		
		station.addControl(new CtCollision((other) -> {
			logger.debug("collision with {}", other);
		}));
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

	public void dock(Spatial spatial) {
		docked.add(spatial);
	}

	public void undock(Spatial spatial) {
		docked.remove(spatial);
	}

	public void disableDocking() {
		station.getControl(CtCollision.class).setEnabled(false);
		getState(StCollision.class).unregister(station);
	}
	
	public void enableDocking() {
		station.getControl(CtCollision.class).setEnabled(true);
		getState(StCollision.class).register(station);
	}

}
