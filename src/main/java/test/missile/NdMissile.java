package test.missile;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;

import jme3.common.material.MtlLighting;
import jme3.common.mesh.FlatShaded;
import jme3utilities.mesh.Cone;
import jme3utilities.mesh.Prism;

final class NdMissile extends Node {

	public NdMissile(AssetManager assetManager) {
		super("missile");

		Material material = new MtlLighting(assetManager, ColorRGBA.Gray);

		Geometry body = new Geometry("body", new FlatShaded(new Cylinder(8, 8, 0.25f, 5f, true)).mesh());
		body.setMaterial(material);
		attachChild(body);

		Geometry head = new Geometry("head", new Cone(8, 0.25f, 0.5f, true));
		head.setMaterial(material);
		attachChild(head);
		head.rotate(FastMath.HALF_PI, 0, 0);
		head.move(0, 0, 2.75f);

		Geometry wingVertical = new Geometry("wing-vertical", new Box(0.0625f, 0.5f, 0.25f));
		wingVertical.setMaterial(material);
		attachChild(wingVertical);
		wingVertical.move(0, 0, 1.25f);
		wingVertical.rotate(0, 0, FastMath.QUARTER_PI);

		Geometry wingHorizontal = new Geometry("wing-horizontal", new Box(0.5f, 0.0625f, 0.25f));
		wingHorizontal.setMaterial(material);
		attachChild(wingHorizontal);
		wingHorizontal.move(0, 0, 1.25f);
		wingHorizontal.rotate(0, 0, FastMath.QUARTER_PI);

		Geometry tailVert = new Geometry("tail-vertical", new Prism(6, 0.5f, 0.125f, true));
		tailVert.setMaterial(material);
		attachChild(tailVert);
		tailVert.scale(1.5f, 1, 1);
		tailVert.rotate(0, 0, FastMath.HALF_PI);
		tailVert.move(0, 0, -1.75f);

		Geometry tailHoriz = new Geometry("tail-horizontal", new Prism(6, 0.5f, 0.125f, true));
		tailHoriz.setMaterial(material);
		attachChild(tailHoriz);
		tailHoriz.scale(1.5f, 1, 1);
		tailHoriz.move(0, 0, -1.75f);
	}

}
