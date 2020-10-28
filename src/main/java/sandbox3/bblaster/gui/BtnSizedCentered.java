package sandbox3.bblaster.gui;

import com.jme3.math.Vector3f;
import com.simsilica.lemur.Action;
import com.simsilica.lemur.ActionButton;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.VAlignment;

final class BtnSizedCentered extends ActionButton {

	public BtnSizedCentered(float size, Action action) {
		this(size, size, action);
	}

	public BtnSizedCentered(float width, float height, Action action) {
		super(action);
		setPreferredSize(new Vector3f(width, height, 0));
		setTextHAlignment(HAlignment.Center);
		setTextVAlignment(VAlignment.Center);	
	}
	
}
