package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.ElementId;

import common.misc.FormattedVector3f;
import jme3utilities.SimpleControl;

final class StHud extends BaseAppState {

	private final Node hud = new Node("hud");
	private Picture targetCursor;

	public StHud(Node guiNode) {
		guiNode.attachChild(hud);
	}

	@Override
	protected void initialize(Application app) {
		Picture crosshair = new Picture("crosshair");

		crosshair.setImage(app.getAssetManager(), "textures/crosshair/crosshair161.png", true);
		crosshair.setWidth(72);
		crosshair.setHeight(72);
		crosshair.setPosition(800 - 36, 400 - 36);

		hud.attachChild(crosshair);

		Texture txTarget = app.getAssetManager().loadTexture("textures/crosshair/crosshair098.png");
		targetCursor = new Picture("hud-target");
		targetCursor.setTexture(app.getAssetManager(), (Texture2D) txTarget, true);
		targetCursor.setWidth(72);
		targetCursor.setHeight(72);

		Container playerInfo = new Container();
		playerInfo.addChild(new Label("player", new ElementId("window.title.label"))).setMaxWidth(320f);

		Container playerPanel = playerInfo.addChild(new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.Even, FillMode.Last)));

		playerPanel.addChild(new Label("thrust")).setMaxWidth(72f);
		ProgressBar thrust = playerPanel.addChild(new ProgressBar(), 1);

		playerPanel.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				thrust.setProgressPercent(getState(StPlayer.class).thrustValue());
				thrust.setMessage(String.format("%.03f", getState(StPlayer.class).thrustValue()));
			}
		});

		playerPanel.addChild(new Label("position"));
		Label position = playerPanel.addChild(new Label("position.value"), 1);

		playerPanel.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				position.setText(new FormattedVector3f(getState(StPlayer.class).translation()).format());
			}
		});

		hud.attachChild(playerInfo);
		playerInfo.setLocalTranslation(10, 800 - 10, 0);

		Container target = new Container();
		target.addChild(new Label("target", new ElementId("window.title.label"))).setMaxWidth(320f);;

		Container targetPanel = target.addChild(new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.Even, FillMode.Last)));

		targetPanel.addChild(new Label("health")).setMaxWidth(72f);
		ProgressBar targetHealth = targetPanel.addChild(new ProgressBar(), 1);

		targetPanel.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				targetHealth.setProgressPercent(getState(StTargetting.class).healthPercent());
				targetHealth.setMessage(String.format("%.03f / %.03f", getState(StTargetting.class).healthValue(), getState(StTargetting.class).healthMax()));
			}
		});
		
		hud.attachChild(target);
		target.setLocalTranslation(10, 800 - playerInfo.getPreferredSize().y - 20f, 0);
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

	@Override
	public void update(float tpf) {
		super.update(tpf);

		if (getState(StTargetting.class).currentTarget() == null) {
			targetCursor.removeFromParent();
		} else {
			Vector3f position = getState(StTargetting.class).currentTarget().getLocalTranslation();
			Vector3f xy = getApplication().getCamera().getScreenCoordinates(position);
			xy.subtractLocal(new Vector3f(72, 72, 0).mult(0.5f));

			if (xy.x < 0)
				xy.x = 0f;
			else if (xy.x > (1600 - 72))
				xy.x = 1600 - 72;

			if (xy.y < 0)
				xy.y = 0;
			else if (xy.y > (800 - 72))
				xy.y = 800 - 72;

			if (xy.z > 1) {
				// TODO not sure this correct, but more/less ok for now
				float dx = xy.x / 1600;
				float dy = xy.y / 800;
				if (dx > dy) {
					xy.x = 0f;
				} else {
					xy.y = 0f;
				}
			}

			hud.attachChild(targetCursor);
			targetCursor.setLocalTranslation(xy);
		}

		// getApplication().getInputManager().setCursorVisible( !getState(StControls.class).isMouseLook() );
	}

}
