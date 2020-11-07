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

final class NdMissile extends Node {

	public NdMissile(AssetManager assetManager) {
		super("missile");

		Material material = new MtlLighting(assetManager, ColorRGBA.Gray);

		Geometry body = new Geometry("body", new FlatShaded(new Cylinder(8, 8, 0.25f, 2.5f, true)).mesh());
		body.setMaterial(material);
		attachChild(body);

		Geometry head = new Geometry("head", new Cone(8, 0.25f, 0.5f, true));
		head.setMaterial(material);
		attachChild(head);
		head.rotate(FastMath.HALF_PI, 0, 0);
		head.move(0, 0, 1.5f);

		Geometry tailVertical = new Geometry("tail-vertical", new Box(0.0625f, 0.5f, 0.25f));
		tailVertical.setMaterial(material);
		attachChild(tailVertical);
		tailVertical.move(0, 0, -0.75f);

		Geometry tailHorizontal = new Geometry("tail-horizontal", new Box(0.5f, 0.0625f, 0.25f));
		tailHorizontal.setMaterial(material);
		attachChild(tailHorizontal);
		tailHorizontal.move(0, 0, -0.75f);
	}

}
