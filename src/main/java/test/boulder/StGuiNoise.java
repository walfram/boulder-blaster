package test.boulder;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.scene.Node;
import com.simsilica.event.EventBus;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.core.VersionedReference;
import com.simsilica.lemur.props.PropertyPanel;
import com.simsilica.lemur.style.BaseStyles;

final class StGuiNoise extends BaseAppState {

	private final Node scene = new Node("scene");

	private final NoiseSettings noiseSettings = new NoiseSettings();

	private VersionedReference<PropertyPanel> reference;

	public StGuiNoise(Node guiNode) {
		guiNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Container container = new Container();
		container.addChild(new Label("noise settings")).setMaxWidth(480f);

		PropertyPanel panel = new PropertyPanel(BaseStyles.GLASS);

		panel.addFloatField("strength", noiseSettings, "strength", 0f, 100f, 0.25f);
		panel.addIntField("numLayers", noiseSettings, "numLayers", 1, 8, 1);
		panel.addFloatField("baseRoughness", noiseSettings, "baseRoughness", 0f, 10f, 0.25f);
		panel.addFloatField("roughness", noiseSettings, "roughness", 0f, 16f, 0.25f);
		panel.addFloatField("persistence", noiseSettings, "persistence", 0, 10f, 0.25f);

		container.addChild(panel);

		scene.attachChild(container);
		container.setLocalTranslation(
				app.getCamera().getWidth() - container.getPreferredSize().x - 5,
				app.getCamera().getHeight() - 5,
				0);
		
		reference = panel.createReference();
	}
	
	@Override
	public void update(float tpf) {
		super.update(tpf);
		
		if (reference.update()) {
			EventBus.publish(Events.noiseSettingsChange, noiseSettings);
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
