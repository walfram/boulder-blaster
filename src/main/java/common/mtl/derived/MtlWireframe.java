package common.mtl.derived;

import com.jme3.material.Material;

public final class MtlWireframe extends MtlBase {

	public MtlWireframe(Material other) {
		super(other);
		getAdditionalRenderState().setWireframe(true);
	}

}
