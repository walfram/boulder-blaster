package test;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Mesh.Mode;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.system.AppSettings;
import com.jme3.util.BufferUtils;

import jme3utilities.debug.AxesVisualizer;
import jme3utilities.mesh.Icosphere;
import jme3utilities.mesh.Octasphere;
import sandbox3.bblaster.debug.NdDebugGrid;
import sandbox3.bblaster.materials.MtlLighting;
import sandbox3.bblaster.materials.MtlShowNormals;
import sandbox3.bblaster.misc.FlatShadedMesh;

public class FlatShadedTest extends SimpleApplication {

	public static void main(String[] args) {
		FlatShadedTest app = new FlatShadedTest();

		AppSettings settings = new AppSettings(true);
		settings.setResolution(1600, 800);

		app.setSettings(settings);
		app.setShowSettings(false);

		app.start();
	}

	private static final Logger logger = LoggerFactory.getLogger(FlatShadedTest.class);

	@Override
	public void simpleInitApp() {
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(200);
		flyCam.setZoomSpeed(0);

		rootNode.attachChild(new NdDebugGrid(assetManager, 20, 100f, ColorRGBA.Gray));
		rootNode.addControl(new AxesVisualizer(assetManager, 128, 1));
		rootNode.getControl(AxesVisualizer.class).setEnabled(true);

		cam.setLocation(Vector3f.UNIT_XYZ.mult(100));
		cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

		rootNode.addLight(new AmbientLight(ColorRGBA.White));
		rootNode.addLight(new DirectionalLight(Vector3f.UNIT_XYZ.negate(), ColorRGBA.White));

		Mesh mesh = new Octasphere(2, 50f);
		// Mesh mesh = new Icosphere(2, 25f);
		// Geometry geometry = new Geometry("sphere", mesh);
		// geometry.setMaterial(new MtlShowNormals(assetManager));

		// rootNode.attachChild(geometry);

		Geometry g = new Geometry("flat-shaded", new FlatShadedMesh(mesh).create());
		// g.setMaterial(new MtlShowNormals(assetManager));
		g.setMaterial(new MtlLighting(assetManager, ColorRGBA.Gray));

		rootNode.attachChild(g);
	}

}
