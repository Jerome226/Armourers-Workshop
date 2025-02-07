package riskyken.armourersWorkshop.common.blocks;

import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import riskyken.armourersWorkshop.ArmourersWorkshop;
import riskyken.armourersWorkshop.client.lib.LibBlockResources;
import riskyken.armourersWorkshop.common.items.block.ModItemBlock;
import riskyken.armourersWorkshop.common.lib.LibBlockNames;
import riskyken.armourersWorkshop.common.lib.LibGuiIds;
import riskyken.armourersWorkshop.common.tileentities.TileEntityOutfitMaker;
import riskyken.armourersWorkshop.utils.BlockUtils;

public class BlockOutfitMaker extends AbstractModBlockContainer {

    public BlockOutfitMaker() {
        super(LibBlockNames.OUTFIT_MAKER);
    }

    @Override
    public Block setBlockName(String name) {
        GameRegistry.registerBlock(this, ModItemBlock.class, "block." + name);
        return super.setBlockName(name);
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        BlockUtils.dropInventoryBlocks(world, x, y, z);
        super.breakBlock(world, x, y, z, block, meta);
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityOutfitMaker();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float xHit, float yHit, float zHit) {
        if (!player.canPlayerEdit(x, y, z, side, player.getCurrentEquippedItem())) {
            return false;
        }
        if (!world.isRemote) {
            FMLNetworkHandler.openGui(player, ArmourersWorkshop.getInstance(), LibGuiIds.OUTFIT_MAKER, world, x, y, z);
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    private IIcon[] iconSides;
    @SideOnly(Side.CLIENT)
    private IIcon iconBottom;

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        blockIcon = register.registerIcon(LibBlockResources.OUTFIT_MAKER_TOP);
        iconBottom = register.registerIcon(LibBlockResources.OUTFIT_MAKER_BOTTOM);
        iconSides = new IIcon[4];
        iconSides[0] = register.registerIcon(LibBlockResources.OUTFIT_MAKER_SIDE_1);
        iconSides[1] = register.registerIcon(LibBlockResources.OUTFIT_MAKER_SIDE_2);
        iconSides[2] = register.registerIcon(LibBlockResources.OUTFIT_MAKER_SIDE_3);
        iconSides[3] = register.registerIcon(LibBlockResources.OUTFIT_MAKER_SIDE_4);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == 0) {
            return iconBottom;
        }
        if (side == 1) {
            return blockIcon;
        }
        if (side > 1 & side < 6) {
            return iconSides[side - 2];
        }
        return blockIcon;
    }

}
