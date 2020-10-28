package sandbox3.bblaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.BaseStyles;

public final class LemurState extends BaseAppState {

	private static final Logger logger = LoggerFactory.getLogger(LemurState.class);

	@Override
	protected void initialize(Application app) {
		GuiGlobals.initialize(app);
		logger.info("initialized Lemur");

		BaseStyles.loadGlassStyle();
		GuiGlobals.getInstance().getStyles().setDefaultStyle(BaseStyles.GLASS);
		logger.info("initialized Lemur glass style");
	}

	@Override
	protected void cleanup(Application app) {
	}

	@Override
	protected void onEnable() {
	}

	@Override
	protected void onDisable() {
	}

}
