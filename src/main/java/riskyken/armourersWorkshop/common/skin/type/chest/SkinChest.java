package riskyken.armourersWorkshop.common.skin.type.chest;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinProperty;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinPartType;
import riskyken.armourersWorkshop.client.lib.LibItemResources;
import riskyken.armourersWorkshop.common.skin.data.SkinProperties;
import riskyken.armourersWorkshop.common.skin.type.AbstractSkinTypeBase;

public class SkinChest extends AbstractSkinTypeBase {
    
    private ArrayList<ISkinPartType> skinParts;
    
    public SkinChest() {
        skinParts = new ArrayList<ISkinPartType>();
        skinParts.add(new SkinChestPartBase(this));
        skinParts.add(new SkinChestPartLeftArm(this));
        skinParts.add(new SkinChestPartRightArm(this));
    }
    
    @Override
    public ArrayList<ISkinPartType> getSkinParts() {
        return this.skinParts;
    }

    @Override
    public String getRegistryName() {
        return "armourers:chest";
    }
    
    @Override
    public String getName() {
        return "Chest";
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcon(IIconRegister register) {
        this.icon = register.registerIcon(LibItemResources.TEMPLATE_CHEST);
        this.emptySlotIcon = register.registerIcon(LibItemResources.SLOT_SKIN_CHEST);
    }

    @Override
    public int getVanillaArmourSlotId() {
        return 1;
    }
    
    @Override
    public ArrayList<ISkinProperty<?>> getProperties() {
        ArrayList<ISkinProperty<?>> properties = super.getProperties();
        properties.add(SkinProperties.PROP_MODEL_OVERRIDE_CHEST);
        properties.add(SkinProperties.PROP_MODEL_OVERRIDE_ARM_LEFT);
        properties.add(SkinProperties.PROP_MODEL_OVERRIDE_ARM_RIGHT);
        properties.add(SkinProperties.PROP_MODEL_HIDE_OVERLAY_CHEST);
        properties.add(SkinProperties.PROP_MODEL_HIDE_OVERLAY_ARM_LEFT);
        properties.add(SkinProperties.PROP_MODEL_HIDE_OVERLAY_ARM_RIGHT);
        return properties;
    }
}
