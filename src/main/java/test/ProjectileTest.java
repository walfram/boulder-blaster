package test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Cylinder;
import com.jme3.system.AppSettings;

import jme3.common.debug.NdDebugGrid;
import jme3.common.material.MtlShowNormals;
import jme3utilities.SimpleControl;
import jme3utilities.debug.AxesVisualizer;

public class ProjectileTest extends SimpleApplication {

	public static void main(String[] args) {
		ProjectileTest app = new ProjectileTest();

		AppSettings settings = new AppSettings(true);
		settings.setResolution(1600, 800);

		app.setSettings(settings);
		app.setShowSettings(false);

		app.start();
	}

	private static final Logger logger = LoggerFactory.getLogger(ProjectileTest.class);
	
	private float cooldown = 0f;

	@Override
	public void simpleInitApp() {
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(100f);
		flyCam.setZoomSpeed(0);

		rootNode.attachChild(new NdDebugGrid(assetManager, 20, 20, 100f, ColorRGBA.Gray));
		rootNode.addControl(new AxesVisualizer(assetManager, 128, 1));
		rootNode.getControl(AxesVisualizer.class).setEnabled(true);

		cam.setFrustumFar(32768);

		cam.setLocation(new Vector3f(7.600002f, 3.6839457f, -13.250118f));
		cam.setRotation(new Quaternion(0.0092146965f, -0.115946025f, 0.0010751534f, 0.99321216f));

		Mesh mesh = new Cylinder(4, 6, 0.125f, 10f, true);
		Geometry projectile = new Geometry("projectile", mesh);
		// projectile.setMaterial(new MtlWireframe(new MtlShowNormals(assetManager)));
		projectile.setMaterial(new MtlShowNormals(assetManager));

//		rootNode.attachChild(projectile);
	}

	@Override
	public void simpleUpdate(float tpf) {
		super.simpleUpdate(tpf);

		if (cooldown >= 0.125f) {
			cooldown = 0f;

			Mesh mesh = new Cylinder(4, 6, 0.5f, 10f, true);
			Geometry projectile = new Geometry("projectile", mesh);
			projectile.setMaterial(new MtlShowNormals(assetManager));

			rootNode.attachChild(projectile);
			
			projectile.addControl(new SimpleControl() {
				@Override
				protected void controlUpdate(float updateInterval) {
					super.controlUpdate(updateInterval);
					
					Vector3f fwd = spatial.getLocalRotation().mult(Vector3f.UNIT_Z);
//					fwd.multLocal(10f);
					
//					Vector3f updated = spatial.getLocalTranslation().add(fwd);
//					spatial.setLocalTranslation(updated);
					spatial.move(fwd);
				}
			});
			
			projectile.addControl(new SimpleControl() {
				float elapsed = 0f;
				
				@Override
				protected void controlUpdate(float updateInterval) {
					super.controlUpdate(updateInterval);
					
					elapsed += updateInterval;
					
					if (elapsed > 3.0f) {
						spatial.removeFromParent();
						logger.debug("removed spatial = {}", spatial);
					}
				}
			});
		}

		cooldown += tpf;
	}

}
