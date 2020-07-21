package common.mtl.derived;

import com.jme3.material.Material;

public final class MtlInstanced extends MtlBase {

	public MtlInstanced(Material other) {
		super(other);
		setBoolean("UseInstancing", true);
	}

}
