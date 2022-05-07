package moe.plushie.armourers_workshop.core.render.bake;

import moe.plushie.armourers_workshop.api.painting.IPaintColor;
import moe.plushie.armourers_workshop.api.skin.ISkinPaintType;
import moe.plushie.armourers_workshop.api.skin.ISkinPartType;
import moe.plushie.armourers_workshop.core.skin.face.SkinCubeFace;
import moe.plushie.armourers_workshop.core.skin.part.texture.TexturePart;
import moe.plushie.armourers_workshop.utils.color.ColorDescriptor;
import moe.plushie.armourers_workshop.utils.color.ColorScheme;
import moe.plushie.armourers_workshop.core.render.bufferbuilder.SkinRenderType;
import moe.plushie.armourers_workshop.core.skin.property.SkinProperties;
import moe.plushie.armourers_workshop.core.skin.painting.SkinPaintTypes;
import moe.plushie.armourers_workshop.core.skin.part.SkinPart;
import moe.plushie.armourers_workshop.core.texture.PlayerTextureLoader;
import moe.plushie.armourers_workshop.utils.extened.AWVoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.function.BiConsumer;

@OnlyIn(Dist.CLIENT)
public class BakedSkinPart {

    private final SkinPart part;
    private final PackedQuad quads;
    private final ColorDescriptor descriptor;

    private int id = 0;

    public BakedSkinPart(SkinPart part, PackedQuad quads) {
        this.part = part;
        this.quads = quads;
        this.descriptor = quads.getColorInfo();
    }

    public void forEach(BiConsumer<SkinRenderType, ArrayList<SkinCubeFace>> action) {
        quads.forEach(action);
    }

    @Nullable
    public Object requirements(ColorScheme scheme) {
        if (descriptor.isEmpty() || scheme.isEmpty()) {
            return null;
        }
        boolean needsEntityTexture = false;
        ArrayList<Object> requirements = new ArrayList<>();
        for (ISkinPaintType paintType : descriptor.getPaintTypes()) {
            if (paintType.getDyeType() != null) {
                IPaintColor resolvedColor = scheme.getResolvedColor(paintType);
                requirements.add(paintType.getId());
                requirements.add(resolvedColor);
                // we must know then texture info for the resolved color.
                if (resolvedColor != null) {
                    paintType = resolvedColor.getPaintType();
                }
            }
            if (paintType == SkinPaintTypes.TEXTURE) {
                needsEntityTexture = true;
            }
        }
        if (needsEntityTexture && PlayerTextureLoader.getInstance().getTextureModel(scheme.getTexture()) != null) {
            requirements.add(SkinPaintTypes.TEXTURE.getId());
            requirements.add(scheme.getTexture());
        }
        return requirements;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SkinPart getPart() {
        return part;
    }

    public ISkinPartType getType() {
        return part.getType();
    }

    public ColorDescriptor getColorInfo() {
        return quads.getColorInfo();
    }

    public AWVoxelShape getRenderShape() {
        return quads.getRenderShape();
    }

    public float getRenderPolygonOffset() {
        if (part instanceof TexturePart) {
            return -0.2f;
        }
        return getType().getRenderPolygonOffset();
    }

    public SkinProperties getProperties() {
        return part.getProperties();
    }

    public boolean isModelOverridden() {
        return part.getType().isModelOverridden(part.getProperties());
    }

    public boolean isOverlayOverridden() {
        return part.getType().isOverlayOverridden(part.getProperties());
    }
}
