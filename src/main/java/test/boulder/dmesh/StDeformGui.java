package test.boulder.dmesh;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.geom.Deformation;
import com.simsilica.lemur.props.PropertyPanel;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.lemur.style.ElementId;

final class StDeformGui extends BaseAppState {

	private final Node scene = new Node("scene");
	private VersionedReference<PropertyPanel> propsRef;

	public StDeformGui(Node guiNode) {
		guiNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Container container = new Container();
		container.addChild(new Label("Cylindrical deformation", new ElementId("window.title.label")));

		PropertyPanel props = new PropertyPanel(BaseStyles.GLASS);

		// props.addIntProperty("major axis", getState(StBoulder.class).cylindricalDeform(), "majorAxis", 0, 2, 1);
		// props.addIntProperty("minor axis", getState(StBoulder.class).cylindricalDeform(), "minorAxis", 0, 2, 1);

		props.addFloatProperty("radius", getState(StBoulder.class).cylindricalDeform(), "radius", 0, 64, 0.25f);
		props.addFloatProperty("start", getState(StBoulder.class).cylindricalDeform(), "start", 0, 64, 0.25f);
		props.addFloatProperty("limit", getState(StBoulder.class).cylindricalDeform(), "limit", 0, 64, 0.25f);

		propsRef = props.createReference();

		container.addChild(props);
		// major axis
		// minor axis
		// vector3f origin
		// float radius
		// float start
		// float limit

		scene.attachChild(container);
		container.setLocalTranslation(5, app.getCamera().getHeight() - 5, 0);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		if (propsRef.update()) {
			getState(StBoulder.class).updateMesh();
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
