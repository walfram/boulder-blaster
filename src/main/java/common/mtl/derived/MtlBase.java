package common.mtl.derived;

import com.jme3.material.Material;

abstract class MtlBase extends Material {

	protected MtlBase(Material other) {
		super(other.getMaterialDef());

		other.getParams().forEach(param -> {
			setParam(param.getName(), param.getVarType(), param.getValue());
		});
		
		getAdditionalRenderState().set(other.getAdditionalRenderState());
	}

}
