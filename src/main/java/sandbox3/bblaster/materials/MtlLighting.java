package sandbox3.bblaster.materials;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;

public final class MtlLighting extends Material {

	public MtlLighting(AssetManager assetManager, ColorRGBA color) {
		super(assetManager, Materials.LIGHTING);

		setBoolean("UseMaterialColors", true);
		
		setColor("Diffuse", color);
		setColor("Ambient", color);
	}

}
