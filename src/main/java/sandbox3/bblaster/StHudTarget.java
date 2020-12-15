package sandbox3.bblaster;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.FillMode;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.ElementId;

import jme3.common.vector.FormattedVector3f;
import jme3utilities.SimpleControl;

public final class StHudTarget extends BaseAppState {

	private final Node hud = new Node("hud-target");

	private Picture targetCursor;

	public StHudTarget(Node guiNode) {
		guiNode.attachChild(hud);
	}

	@Override
	protected void initialize(Application app) {
		targetCursor = new Picture("hud-target");
		targetCursor.setImage(getApplication().getAssetManager(), "textures/crosshair/crosshair098.png", true);
		targetCursor.setWidth(72);
		targetCursor.setHeight(72);

		// targetCursor.addControl(new CtTargetCursor(getState(StTargetting.class), getApplication().getCamera()));

		Container target = new Container();
		target.addChild(new Label("target", new ElementId("window.title.label"))).setMaxWidth(320f);

		Container content = target.addChild(new Container(new SpringGridLayout(Axis.Y, Axis.X, FillMode.Even, FillMode.Last)));

		content.addChild(new Label("total"));
		Label labelTotal = content.addChild(new Label("total.value"), 1);

		content.addChild(new Label("id")).setMaxWidth(72f);
		Label targetId = content.addChild(new Label("id.value"), 1);

		content.addChild(new Label("health"));
		ProgressBar targetHealth = content.addChild(new ProgressBar(), 1);
		targetHealth.getLabel().setColor(ColorRGBA.Yellow);

		content.addChild(new Label("speed"));
		Label targetSpeed = content.addChild(new Label("speed.value"), 1);

		content.addChild(new Label("distance"));
		Label targetDistance = content.addChild(new Label("distance.value"), 1);

		content.addChild(new Label("scr.coords"));
		Label screenCoords = content.addChild(new Label("scr.coords.value"), 1);

		content.addControl(new SimpleControl() {
			@Override
			protected void controlUpdate(float updateInterval) {
				super.controlUpdate(updateInterval);

				labelTotal.setText(String.format("%s", getState(StBoulders.class).boulderQuantity()));

				targetId.setText(getState(StTargetting.class).targetId());

				targetHealth.setProgressPercent(getState(StTargetting.class).healthPercent());
				targetHealth.setMessage(
						String.format(
								"%.03f / %.03f",
								getState(StTargetting.class).healthValue(),
								getState(StTargetting.class).healthMax()));

				targetSpeed.setText(String.format("%.03f", getState(StTargetting.class).speed()));

				targetDistance.setText(String.format("%.03f", getState(StTargetting.class).distance()));

				Vector3f v = getState(StTargetting.class).relativePosition();
				screenCoords.setText(new FormattedVector3f(v).format());
			}
		});

		hud.attachChild(target);
		// target.setLocalTranslation(10, 800 - playerContainer.getPreferredSize().y - 20f, 0);
		target.setLocalTranslation(
				app.getCamera().getWidth() - target.getPreferredSize().x - 10f,
				app.getCamera().getHeight() - 10,
				0);
	}

	@Override
	public void update(float tpf) {
		super.update(tpf);

		if (getState(StTargetting.class).currentTarget() == null) {
			targetCursor.removeFromParent();
			return;
		}

		hud.attachChild(targetCursor);

		Vector3f translation = getState(StTargetting.class).translation();
		Vector3f v = getApplication().getCamera().getScreenCoordinates(translation);

		int width = getApplication().getCamera().getWidth();
		int height = getApplication().getCamera().getHeight();

		v.subtractLocal(new Vector3f(72, 72, 0).mult(0.5f));
		
		if (v.x < 0)
			v.x = 0f;
		else if (v.x > (width - 72))
			v.x = width - 72;

		if (v.y < 0)
			v.y = 0;
		else if (v.y > (height - 72))
			v.y = height - 72;

		if (v.z >= 1) {
			// TODO this is not quite correct, must refactor
			float dx = Math.abs(v.x / width);
			float dy = Math.abs(v.y / height);
			if (dx > dy) {
				v.x = 0f;
			} else {
				v.y = 0f;
			}
		}

		targetCursor.setLocalTranslation(v);
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
