package moe.plushie.armourers_workshop.core.gui.hologramprojector;

import com.mojang.blaze3d.matrix.MatrixStack;
import moe.plushie.armourers_workshop.core.block.HologramProjectorContainer;
import moe.plushie.armourers_workshop.core.gui.wardrobe.WardrobeBaseSetting;
import moe.plushie.armourers_workshop.core.utils.RenderUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HologramProjectorInventorySetting extends WardrobeBaseSetting {

    protected int contentWidth = 176;
    protected int contentHeight = 40;

    public HologramProjectorInventorySetting(HologramProjectorContainer container) {
        super("inventory.armourers_workshop.hologram-projector.tab.inventory");
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int x = (width - contentWidth) / 2;
        RenderUtils.blit(matrixStack, x, 0, 0, 98, contentWidth, contentHeight, RenderUtils.TEX_HOLOGRAM_PROJECTOR);
    }
}