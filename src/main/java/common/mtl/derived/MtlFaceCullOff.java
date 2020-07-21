package common.mtl.derived;

import com.jme3.material.Material;
import com.jme3.material.RenderState.FaceCullMode;

public class MtlFaceCullOff extends MtlBase {

	public MtlFaceCullOff(Material other) {
		super(other);
		getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
	}

}
