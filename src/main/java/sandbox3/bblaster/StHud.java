package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.ElementId;

import jme3.common.vector.FormattedVector3f;
import jme3utilities.SimpleControl;

public final class StHud extends BaseAppState {

	private final Node hud = new Node("flight-hud");

	public StHud(Node guiNode) {
		guiNode.attachChild(hud);
	}

	@Override
	protected void initialize(Application app) {
		Container playerContainer = new Container();
		playerContainer.addChild(new Label("player", new ElementId("window.title.label"))).setMaxWidth(320f);

		Container playerPanel = playerContainer.addChild(
				new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.Even, FillMode.Last)));

		playerPanel.addChild(new Label("thrust")).setMaxWidth(72f);
		ProgressBar thrust = playerPanel.addChild(new ProgressBar(), 1);
		thrust.getLabel().setColor(ColorRGBA.Yellow);

		playerPanel.addChild(new Label("position"));
		Label position = playerPanel.addChild(new Label("position.value"), 1);

		playerPanel.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				thrust.setProgressPercent(getState(StPlayer.class).thrustValue());
				thrust.setMessage(String.format("%.03f", getState(StPlayer.class).thrustValue()));

				position.setText(new FormattedVector3f(getState(StPlayer.class).position()).format());
			}
		});

		hud.attachChild(playerContainer);
		playerContainer.setLocalTranslation(10, app.getCamera().getHeight() - 10, 0);
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
		// getApplication().getInputManager().setCursorVisible( !getState(StControls.class).isMouseLook() );
	}

}
