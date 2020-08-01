package sandbox3.bblaster;

import java.util.ArrayList;
import java.util.List;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;

import common.mtl.MtlUnshaded;

final class StStation extends BaseAppState {
	
	private final Node stations = new Node("stations");

	private final List<Spatial> docked = new ArrayList<>();
	
	public StStation(Node rootNode) {
		rootNode.attachChild(stations);
	}

	@Override
	protected void initialize(Application app) {
		
		Geometry station = new Geometry("station", new WireBox(50f, 50f, 50f));
		station.setMaterial(new MtlUnshaded(app.getAssetManager(), ColorRGBA.Gray, 5f));
		stations.attachChild(station);
		
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

}
