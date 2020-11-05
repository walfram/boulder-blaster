package test;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

import jme3.common.debug.NdDebugGrid;
import jme3.common.material.MtlShowNormals;
import jme3utilities.MyCamera;
import jme3utilities.debug.AxesVisualizer;

public class StationTest extends SimpleApplication {

	public static void main(String[] args) {
		StationTest app = new StationTest();

		AppSettings settings = new AppSettings(true);
		settings.setResolution(1600, 800);

		app.setSettings(settings);
		app.setShowSettings(false);

		app.start();
	}

	@Override
	public void simpleInitApp() {
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(100);
		flyCam.setZoomSpeed(0);

		MyCamera.setNearFar(cam, 0.1f, 32768f);

		cam.setLocation(new Vector3f(0, 0, 200));

		rootNode.attachChild(new NdDebugGrid(assetManager, 10, 10, 500, ColorRGBA.Gray));

		AxesVisualizer axesVisualizer = new AxesVisualizer(assetManager, 250, 1);
		rootNode.addControl(axesVisualizer);
		axesVisualizer.setEnabled(true);

		Mesh mesh = new Box(25, 1, 100);
		Material material = new MtlShowNormals(assetManager);

		Geometry topPlate = new Geometry("top-plate", mesh);
		topPlate.setMaterial(material);
		topPlate.move(0, 40, 0);
		rootNode.attachChild(topPlate);

		Geometry leftTopPlate = new Geometry("left-top-plate", mesh);
		leftTopPlate.setMaterial(material);
		leftTopPlate.rotate(0, 0, 45f * FastMath.DEG_TO_RAD);
		leftTopPlate.move(-45, 20f, 0);
		rootNode.attachChild(leftTopPlate);

		Geometry rightTopPlate = new Geometry("right-top-plate", mesh);
		rightTopPlate.setMaterial(material);
		rightTopPlate.rotate(0, 0, -45f * FastMath.DEG_TO_RAD);
		rightTopPlate.move(45, 20, 0);
		rootNode.attachChild(rightTopPlate);
		
		Geometry bottomPlate = new Geometry("bottom-plate", mesh);
		bottomPlate.setMaterial(material);
		bottomPlate.rotate(0, 0, 180f * FastMath.DEG_TO_RAD);
		bottomPlate.move(0, -40, 0);
		rootNode.attachChild(bottomPlate);

		Geometry leftBottomPlate = new Geometry("left-top-plate", mesh);
		leftBottomPlate.setMaterial(material);
		leftBottomPlate.rotate(0, 0, -225f * FastMath.DEG_TO_RAD);
		leftBottomPlate.move(-45, -20f, 0);
		rootNode.attachChild(leftBottomPlate);

		Geometry rightBottomPlate = new Geometry("left-bottom-plate", mesh);
		rightBottomPlate.setMaterial(material);
		rightBottomPlate.rotate(0, 0, 225f * FastMath.DEG_TO_RAD);
		rightBottomPlate.move(45, -20, 0);
		rootNode.attachChild(rightBottomPlate);
	}

}
