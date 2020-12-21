package test.boulder.noise;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import com.google.common.primitives.Floats;
import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ColorSpace;
import com.jme3.texture.image.ImageRaster;
import com.jme3.ui.Picture;

import jme3utilities.math.noise.Perlin2;

final class StGeneratedTexture extends BaseAppState {

	private final Node scene = new Node("scene");

	public StGeneratedTexture(Node guiNode) {
		guiNode.attachChild(scene);
	}

	@Override
	protected void initialize(Application app) {
		Picture p = new Picture("generated-texture");

		float size = 512;
		float halfSize = size * 0.5f;

		Texture2D texture = generateTexture(1024);

		p.setTexture(app.getAssetManager(), texture, false);
		p.setWidth(size);
		p.setHeight(size);

		p.setLocalTranslation(800 - halfSize, 400 - halfSize, 0);

		scene.attachChild(p);
	}

	// private Texture2D generateTexture() {
	// return (Texture2D) getApplication().getAssetManager().loadTexture("Common/Textures/MissingTexture.png");
	// }

	private Texture2D generateTexture(int size) {
		long seed = -35_930_871;
		int fundamental = 10;
		Perlin2 generator = new Perlin2(fundamental, fundamental, seed, seed);

		float[][] samples = new float[size][];

		for (int x = 0; x < size; x++) {
			float u = ((float) x) / size;

			samples[x] = new float[size];
			for (int y = 0; y < size; y++) {
				float v = ((float) y) / size;
				float e = 0f;
				for (int i = 1; i < 5; i++) {
					float freq = FastMath.pow(2, i);
					e += (1f / freq) * generator.sampleNormalized(freq * u, freq * v);
				}
				// e = FastMath.pow(e, 3f);

				samples[x][y] = e;
			}
		}

		float min = Floats.min(Floats.concat(samples));
		float max = Floats.max(Floats.concat(samples));
		float range = max - min;

		ByteBuffer data = BufferUtils.createByteBuffer(size * size * 4);
		Image image = new Image(Format.RGB8, size, size, data, ColorSpace.sRGB);
		ImageRaster raster = ImageRaster.create(image);
		ColorRGBA pxl = new ColorRGBA();

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				float e = (samples[x][y] - min) / range;
				samples[x][y] = e;

				pxl.set(e, e, e, 1.0f);
				raster.setPixel(x, y, pxl);
			}
		}

		return new Texture2D(image);
	}

	@Override
	protected void cleanup(Application app) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onEnable() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDisable() {
		// TODO Auto-generated method stub

	}

}
