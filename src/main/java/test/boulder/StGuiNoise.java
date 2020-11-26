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

	// private final FastNoiseLite noise = new FastNoiseLite();
	private final NoiseSettings settings = new NoiseSettings();

	private VersionedReference<PropertyPanel> reference;

	public StGuiNoise(Node guiNode) {
		guiNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Container container = new Container();
		container.addChild(new Label("noise settings")).setMaxWidth(480f);

		PropertyPanel panel = new PropertyPanel(BaseStyles.GLASS);

		panel.addFloatField("strength", settings, "strength", 0f, 100f, 0.25f);

		panel.addEnumField("mNoiseType", settings, "mNoiseType");
		panel.addEnumField("mFractalType", settings, "mFractalType");

		panel.addEnumField("mRotationType3D", settings, "mRotationType3D");
		panel.addEnumField("mCellularDistanceFunction", settings, "mCellularDistanceFunction");
		panel.addEnumField("mCellularReturnType", settings, "mCellularReturnType");

		panel.addFloatField("mFrequency", settings, "mFrequency", 0f, 1f, 0.001f);
		panel.addIntField("mOctaves", settings, "mOctaves", 0, 32, 1);
		panel.addFloatField("mLacunarity", settings, "mLacunarity", 0, 32, 0.25f);
		panel.addFloatField("mGain", settings, "mGain", 0, 1f, 0.01f);

		container.addChild(panel);

		scene.attachChild(container);
		container.setLocalTranslation(
				app.getCamera().getWidth() - container.getPreferredSize().x - 5,
				app.getCamera().getHeight() - 5,
				0);

		reference = panel.createReference();

		EventBus.publish(Events.noiseChange, settings);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		if (reference.update()) {
			EventBus.publish(Events.noiseChange, settings);
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
