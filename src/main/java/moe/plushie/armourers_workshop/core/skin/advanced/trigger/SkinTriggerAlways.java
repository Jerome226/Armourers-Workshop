package moe.plushie.armourers_workshop.core.skin.advanced.trigger;

import moe.plushie.armourers_workshop.core.registry.AdvancedSkinRegistry;
import moe.plushie.armourers_workshop.core.skin.data.Skin;
import moe.plushie.armourers_workshop.core.skin.data.SkinPart;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class SkinTriggerAlways extends AdvancedSkinRegistry.AdvancedSkinTrigger {

    public SkinTriggerAlways() {
        super("always");
    }
    
    @Override
    public boolean canTrigger(World world, Entity entity, Skin skin, SkinPart skinPart) {
        return true;
    }
}
