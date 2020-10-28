package test;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

import jme3utilities.debug.AxesVisualizer;
import sandbox3.bblaster.debug.NdDebugGrid;
import sandbox3.bblaster.materials.MtlUnshaded;

public class TransparencyTest extends SimpleApplication {

	public static void main(String[] args) {
		TransparencyTest app = new TransparencyTest();

		AppSettings settings = new AppSettings(true);
		settings.setResolution(1600, 800);

		app.setSettings(settings);
		app.setShowSettings(false);

		app.start();
	}

	@Override
	public void simpleInitApp() {
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(100f);
		flyCam.setZoomSpeed(0);

		AxesVisualizer axesVisualizer = new AxesVisualizer(assetManager, 200f, 1f);
		rootNode.addControl(axesVisualizer);
		axesVisualizer.setEnabled(true);

		NdDebugGrid ndDebugGrid = new NdDebugGrid(assetManager, 10, 100, ColorRGBA.DarkGray);
		rootNode.attachChild(ndDebugGrid);

		Geometry redBox = new Geometry("box", new Box(5, 5, 5));
		redBox.setQueueBucket(Bucket.Transparent);
		Material mtlRed = new MtlUnshaded(assetManager, new ColorRGBA(1, 0, 0, 0.5f));
		mtlRed.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		redBox.setMaterial(mtlRed);
		
		redBox.move(0, 0, -30);
		rootNode.attachChild(redBox);
		
		Geometry greenBox = new Geometry("box", new Box(5, 5, 5));
		greenBox.setQueueBucket(Bucket.Transparent);
		Material mtlGreen = new MtlUnshaded(assetManager, new ColorRGBA(0, 1, 0, 0.5f));
		mtlGreen.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		greenBox.setMaterial(mtlGreen);
		
		greenBox.move(0, 0, -60);
		rootNode.attachChild(greenBox);
		
		Geometry bluenBox = new Geometry("box", new Box(5, 5, 5));
		bluenBox.setQueueBucket(Bucket.Transparent);
		Material mtlBlue = new MtlUnshaded(assetManager, new ColorRGBA(0, 0, 1, 0.5f));
		mtlBlue.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		bluenBox.setMaterial(mtlBlue);
		
		bluenBox.move(0, 0, -90);
		rootNode.attachChild(bluenBox);
	}

}
