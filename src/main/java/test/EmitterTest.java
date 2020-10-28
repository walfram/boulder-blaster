package test;

import com.jme3.app.SimpleApplication;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.Button;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputMapper;
import com.simsilica.lemur.input.InputState;

import jme3utilities.debug.AxesVisualizer;
import sandbox3.bblaster.SkyState;
import sandbox3.bblaster.debug.NdDebugGrid;

public class EmitterTest extends SimpleApplication {

	private static final int COUNT_FACTOR = 1;
	private static final float COUNT_FACTOR_F = 1f;
	private static final FunctionId F_FIRE = new FunctionId("fire");

	@Override
	public void simpleInitApp() {
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(100f);
		flyCam.setZoomSpeed(0);

		rootNode.attachChild(new NdDebugGrid(assetManager, 20, 100f, ColorRGBA.Gray));
		rootNode.addControl(new AxesVisualizer(assetManager, 128, 1));
		rootNode.getControl(AxesVisualizer.class).setEnabled(true);

		cam.setFrustumFar(32768);

		cam.setLocation(new Vector3f(-2.188475f, 1.188586f, -10.577302f));
		cam.setRotation(new Quaternion(-0.028448217f, 0.23830087f, 0.0069829333f, 0.9707495f));

		ParticleEmitter spark = new ParticleEmitter("Spark", Type.Triangle, 1000 * COUNT_FACTOR);
		spark.setStartColor(ColorRGBA.Yellow.mult(5));
		spark.setEndColor(ColorRGBA.Yellow);
		spark.setStartSize(10f);
		spark.setEndSize(10f);

		spark.setFacingVelocity(true);

		spark.setParticlesPerSec(50);

		spark.setGravity(Vector3f.ZERO);

		spark.setLowLife(3f);
		spark.setHighLife(3.3f);

		spark.getParticleInfluencer().setInitialVelocity(new Vector3f(1, 0.5f, 1).normalize().mult(500f));
		spark.getParticleInfluencer().setVelocityVariation(0f);

		spark.setImagesX(1);
		spark.setImagesY(1);

//		spark.setRotateSpeed(FastMath.DEG_TO_RAD * 10f);
		
		spark.setInWorldSpace(true);

		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/spark.png"));
		// mat.setTexture("Texture", assetManager.loadTexture("textures/projectile/laser-texture-1.png"));
		mat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
		mat.setFloat("Softness", 3);

		spark.setMaterial(mat);
		rootNode.attachChild(spark);

		// spark.scale(100f);
		spark.setEnabled(false);

		GuiGlobals.initialize(this);

		InputMapper inputMapper = GuiGlobals.getInstance().getInputMapper();
		inputMapper.map(F_FIRE, Button.MOUSE_BUTTON2);
		inputMapper.addStateListener((func, state, tpf) -> {
			if (state != InputState.Off) {
//				spark.setEnabled(true);
				spark.setNumParticles(1);
				spark.emitAllParticles();
		}
			else {
				spark.setEnabled(false);
				spark.killAllParticles();
			}
		}, F_FIRE);
		
		stateManager.attach(new SkyState(rootNode));

	}

	public static void main(String[] args) {
		EmitterTest app = new EmitterTest();

		AppSettings settings = new AppSettings(true);
		settings.setResolution(1600, 800);

		app.setSettings(settings);
		app.setShowSettings(false);

		app.start();
	}

}
