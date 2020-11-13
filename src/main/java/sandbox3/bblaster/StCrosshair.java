package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

public final class StCrosshair extends BaseAppState {

	private final Node scene = new Node("crosshair");

	private Picture crosshair;

	public StCrosshair(Node guiNode) {
		guiNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		crosshair = new Picture("crosshair");
		crosshair.setImage(getApplication().getAssetManager(), "textures/crosshair/crosshair161.png", true);
		crosshair.setWidth(72);
		crosshair.setHeight(72);
		crosshair.setPosition(800 - 36, 400 - 36);
		scene.attachChild(crosshair);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		if (getState(StControls.class).isMouseLook()) {
			Vector2f v = getApplication().getInputManager().getCursorPosition();
			crosshair.setLocalTranslation(v.x - 36, v.y - 36, 0);
		} else {
			crosshair.setPosition(800 - 36, 400 - 36);
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
