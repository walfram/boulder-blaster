package test.boulder.dmesh;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.simsilica.lemur.geom.DMesh;
import com.simsilica.lemur.geom.Deformations;
import com.simsilica.lemur.geom.Deformations.Cylindrical;

import jme3.common.material.MtlLighting;
import jme3utilities.mesh.Octasphere;

final class StBoulder extends BaseAppState {

	private final Node scene = new Node("scene");
	private Cylindrical cylindrical;
	private DMesh dmesh;

	public StBoulder(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Octasphere mesh = new Octasphere(2, 32f);

		cylindrical = Deformations.cylindrical(1, 0, new Vector3f(), 0, 0, 0);
		dmesh = new DMesh(mesh, cylindrical);

		Geometry boulder = new Geometry("boulder", dmesh);
		boulder.setMaterial(new MtlLighting(app.getAssetManager(), ColorRGBA.Gray));
		boulder.getMaterial().getAdditionalRenderState().setWireframe(true);

		scene.attachChild(boulder);
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

	public Cylindrical cylindricalDeform() {
		return cylindrical;
	}

	public void updateMesh() {
		dmesh.updateMesh();
	}

}
