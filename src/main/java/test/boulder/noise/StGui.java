package test.boulder.noise;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.simsilica.event.EventBus;
import com.simsilica.lemur.Action;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.props.PropertyPanel;
import com.simsilica.lemur.style.BaseStyles;
import com.simsilica.lemur.style.ElementId;

final class StGui extends BaseAppState {

	private final Node scene = new Node("scene");

	private final SphereSettings settings = new SphereSettings();

	private VersionedReference<PropertyPanel> sphereSettingsRef;

	public StGui(Node guiNode) {
		guiNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Container container = new Container("");
		container.addChild(new Label("sphere settings", new ElementId("window.title.label"))).setMaxWidth(480f);

		Container actions = new Container();
		actions.addChild(new ActionButton(new Action("toggle wireframe") {
			@Override
			public void execute(Button source) {
				EventBus.publish(Events.toggleWireframe, null);
			}
		}));

		container.addChild(actions);

		// scene.attachChild(actions);
		// actions.setLocalTranslation(5, app.getCamera().getHeight() - 5, 0);

		PropertyPanel panel = new PropertyPanel(BaseStyles.GLASS);

		panel.addFloatField("noise scale", settings, "noiseScale", 0f, 64f, 0.25f);
		panel.addFloatField("scale x", settings, "scalex", 0.01f, 16f, 0.25f);
		panel.addFloatField("scale y", settings, "scaley", 0.01f, 16f, 0.25f);
		panel.addFloatField("scale z", settings, "scalez", 0.01f, 16f, 0.25f);

		container.addChild(panel);

		scene.attachChild(container);
		container.setLocalTranslation(5, app.getCamera().getHeight() - 5, 0);

		sphereSettingsRef = panel.createReference();
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		if (sphereSettingsRef.update()) {
			EventBus.publish(Events.sphereSettings, settings);
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

	public SphereSettings sphereSettings() {
		return settings;
	}

}
