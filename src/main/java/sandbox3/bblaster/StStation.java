package sandbox3.bblaster;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;

import common.mtl.MtlUnshaded;

final class StStation extends BaseAppState {
	
	private static final Logger logger = LoggerFactory.getLogger(StStation.class);
	
	private final Node station = new Node("station");

	private final List<Spatial> docked = new ArrayList<>();
	
	public StStation(Node rootNode) {
		rootNode.attachChild(station);
	}

	@Override
	protected void initialize(Application app) {
		
		Geometry hull = new Geometry("station-hull", new WireBox(50f, 50f, 50f));
		hull.setMaterial(new MtlUnshaded(app.getAssetManager(), ColorRGBA.Gray, 5f));
		station.attachChild(hull);
		
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
