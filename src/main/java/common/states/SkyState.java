package common.states;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.TextureKey;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;

public final class SkyState extends BaseAppState {

	private final Node scene = new Node("sky-node");

	public SkyState(Node rootNode) {
		rootNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Texture west = app.getAssetManager().loadTexture(new TextureKey("textures/sky/left.png"));
		Texture east = app.getAssetManager().loadTexture(new TextureKey("textures/sky/right.png"));
		Texture north = app.getAssetManager().loadTexture(new TextureKey("textures/sky/front.png"));
		Texture south = app.getAssetManager().loadTexture(new TextureKey("textures/sky/back.png"));

		Texture up = app.getAssetManager().loadTexture(new TextureKey("textures/sky/top-r.png"));
		Texture down = app.getAssetManager().loadTexture(new TextureKey("textures/sky/bot-r.png"));

		Spatial skySpatial = SkyFactory.createSky(app.getAssetManager(), west, east, north, south, up, down);
		scene.attachChild(skySpatial);
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
