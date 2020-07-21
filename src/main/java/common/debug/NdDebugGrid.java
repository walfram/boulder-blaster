package common.debug;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Grid;

public final class NdDebugGrid extends Node {

	public NdDebugGrid(AssetManager assetManager, int lines, float distance, ColorRGBA color) {
		super("debug-grid");

		Geometry geometry = new Geometry("debug-grid-geometry", new Grid(lines, lines, distance));

		Material material = new Material(assetManager, Materials.UNSHADED);
		material.setColor("Color", color);
		// material.getAdditionalRenderState().setDepthTest(false);

		geometry.setMaterial(material);
		geometry.center();

		attachChild(geometry);
	}

}
