package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

public final class StHudCrosshair extends BaseAppState {

	private final Node scene = new Node("crosshair");

	private Picture crosshair;

	public StHudCrosshair(Node guiNode) {
		guiNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		crosshair = new Picture("crosshair");
		crosshair.setImage(app.getAssetManager(), "textures/crosshair/crosshair161.png", true);
		crosshair.setWidth(72);
		crosshair.setHeight(72);
		crosshair.setPosition(app.getCamera().getWidth() * 0.5f - 36, app.getCamera().getHeight() * 0.5f - 36);
		scene.attachChild(crosshair);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		if (getState(StControls.class).isMouseLook()) {
			Vector2f v = getApplication().getInputManager().getCursorPosition();
			crosshair.setLocalTranslation(v.x - 36, v.y - 36, 0);
		} else {
			crosshair.setPosition(
					getApplication().getCamera().getWidth() * 0.5f - 36,
					getApplication().getCamera().getHeight() * 0.5f - 36);
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

	public Vector3f direction() {
		if (getState(StControls.class).isMouseLook()) {
			Vector2f click2d = getApplication().getInputManager().getCursorPosition();
			Vector3f origin = getApplication().getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();

			Vector3f direction = getApplication().getCamera().getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f)
					.subtractLocal(origin).normalizeLocal();

			return direction;
		} else {
			return getState(StPlayer.class).direction();
		}
	}

}
