package moe.plushie.armourers_workshop.core.skin.part.unknown;

import moe.plushie.armourers_workshop.core.api.common.skin.ISkinProperties;
import moe.plushie.armourers_workshop.core.skin.part.SkinPartType;
import moe.plushie.armourers_workshop.core.utils.Point3D;
import moe.plushie.armourers_workshop.core.utils.Rectangle3D;

public class SkinUnknownPartUnknown extends SkinPartType {

    public SkinUnknownPartUnknown() {
        super();
        this.buildingSpace = new Rectangle3D(-32, -32, -32, 64, 64, 64);
        this.guideSpace = new Rectangle3D(0, 0, 0, 0, 0, 0);
        this.offset = new Point3D(0, 0, 0);
    }

    @Override
    public void renderBuildingGuide(float scale, ISkinProperties skinProps, boolean showHelper) {
    }
}
