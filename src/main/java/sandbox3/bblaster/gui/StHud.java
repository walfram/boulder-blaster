package sandbox3.bblaster.gui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.CallMethodAction;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.EmptyAction;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.ElementId;

import jme3utilities.SimpleControl;
import sandbox3.bblaster.StPlayer;
import sandbox3.bblaster.StTargetting;
import sandbox3.bblaster.misc.FormattedVector3f;

public final class StHud extends BaseAppState {

	private final Node hud = new Node("flight-hud");

	private Picture targetCursor;

	private Picture crosshair;

	public StHud(Node guiNode) {
		guiNode.attachChild(hud);
	}

	@Override
	protected void initialize(Application app) {
		crosshair = new Picture("crosshair");
		crosshair.setImage(getApplication().getAssetManager(), "textures/crosshair/crosshair161.png", true);
		crosshair.setWidth(72);
		crosshair.setHeight(72);
		crosshair.setPosition(800 - 36, 400 - 36);

		Texture txTarget = getApplication().getAssetManager().loadTexture("textures/crosshair/crosshair098.png");
		targetCursor = new Picture("hud-target");
		targetCursor.setTexture(getApplication().getAssetManager(), (Texture2D) txTarget, true);
		targetCursor.setWidth(72);
		targetCursor.setHeight(72);

		targetCursor.addControl(new CtTargetCursor(getState(StTargetting.class), getApplication().getCamera()));
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
			hud.attachChild(targetCursor);
		}

		// getApplication().getInputManager().setCursorVisible( !getState(StControls.class).isMouseLook() );
	}

	public void showFlightHud() {
		hud.detachAllChildren();

		hud.attachChild(crosshair);

		Container playerContainer = new Container();
		playerContainer.addChild(new Label("player", new ElementId("window.title.label"))).setMaxWidth(320f);

		Container playerPanel = playerContainer.addChild(new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.Even, FillMode.Last)));

		playerPanel.addChild(new Label("thrust")).setMaxWidth(72f);
		ProgressBar thrust = playerPanel.addChild(new ProgressBar(), 1);
		thrust.getLabel().setColor(ColorRGBA.Yellow);

		playerPanel.addChild(new Label("position"));
		Label position = playerPanel.addChild(new Label("position.value"), 1);

//		playerPanel.addChild(new Label("status"));
//		playerPanel.addChild(new Label("status.value"), 1);

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
		playerContainer.setLocalTranslation(10, 800 - 10, 0);

		Container target = new Container();
		target.addChild(new Label("target", new ElementId("window.title.label"))).setMaxWidth(320f);

		Container targetPanel = target.addChild(new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.Even, FillMode.Last)));

		targetPanel.addChild(new Label("health")).setMaxWidth(72f);
		ProgressBar targetHealth = targetPanel.addChild(new ProgressBar(), 1);

		targetHealth.getLabel().setColor(ColorRGBA.Yellow);

		targetPanel.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				targetHealth.setProgressPercent(getState(StTargetting.class).healthPercent());
				targetHealth.setMessage(String.format("%.03f / %.03f", getState(StTargetting.class).healthValue(), getState(StTargetting.class).healthMax()));
			}
		});

		hud.attachChild(target);
		target.setLocalTranslation(10, 800 - playerContainer.getPreferredSize().y - 20f, 0);
	}

	public void showStationHud() {
		hud.detachAllChildren();

		Container menu = new Container();

		menu.addChild(new BtnSizedCentered(200f, 48f, new CallMethodAction("undock", getState(StPlayer.class), "undock")));
		menu.addChild(new BtnSizedCentered(200f, 48f, new EmptyAction("equipment")));
		menu.addChild(new BtnSizedCentered(200f, 48f, new EmptyAction("ship")));

		hud.attachChild(menu);
		menu.setLocalTranslation(10, 800 - 10, 0);
	}

}
