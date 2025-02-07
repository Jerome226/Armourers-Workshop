package riskyken.armourersWorkshop.client.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import riskyken.armourersWorkshop.api.common.IPoint3D;
import riskyken.armourersWorkshop.api.common.IRectangle3D;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinPartType;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinType;
import riskyken.armourersWorkshop.common.config.ConfigHandlerClient;
import riskyken.armourersWorkshop.common.lib.LibModInfo;
import riskyken.armourersWorkshop.common.skin.data.SkinProperties;
import riskyken.armourersWorkshop.common.skin.type.SkinTypeRegistry;
import riskyken.armourersWorkshop.common.skin.type.block.SkinBlock;

@SideOnly(Side.CLIENT)
public final class SkinRenderHelper {
    
    private static final ResourceLocation guideImage = new ResourceLocation(LibModInfo.ID.toLowerCase(), "textures/blocks/guide.png");
    
    public static void renderBuildingGuide(ISkinType skinType, float scale, SkinProperties skinProps, boolean showHelper) {
        for (int i = 0; i < skinType.getSkinParts().size(); i++) {
            ISkinPartType skinPart = skinType.getSkinParts().get(i);
            if (skinPart.isModelOverridden(skinProps)) {
                continue;
            }
            IPoint3D partOffset = skinPart.getOffset();
            GL11.glTranslated(partOffset.getX() * scale, partOffset.getY() * scale, partOffset.getZ() * scale);
            skinPart.renderBuildingGuide(scale, skinProps, showHelper);
            GL11.glTranslated(-partOffset.getX() * scale, -partOffset.getY() * scale, -partOffset.getZ() * scale);
        }
    }
    
    public static void renderBuildingGrid(ISkinType skinType, float scale, boolean showGuides, SkinProperties skinProps, boolean multiblock) {
        for (int i = 0; i < skinType.getSkinParts().size(); i++) {
            ISkinPartType skinPartType = skinType.getSkinParts().get(i);
            IPoint3D partOffset = skinPartType.getOffset();
            GL11.glTranslated(partOffset.getX() * scale, partOffset.getY() * scale, partOffset.getZ() * scale);
            if (skinType == SkinTypeRegistry.skinBlock) {
                if (skinPartType.getPartName().equals("multiblock") & multiblock) {
                    renderBuildingGrid(((SkinBlock)SkinTypeRegistry.skinBlock).partBase, scale, showGuides, skinProps, 1F, 1F, 0.0F, 0.2F);
                    GL11.glPolygonOffset(6F, 6F);
                    renderBuildingGrid(skinPartType, scale, showGuides, skinProps, 0.5F, 0.5F, 0.5F, 0.25F);
                } else if (skinPartType.getPartName().equals("base") & !multiblock) {
                    renderBuildingGrid(skinPartType, scale, showGuides, skinProps, 0.5F, 0.5F, 0.5F, 0.25F);
                }
            } else {
                renderBuildingGrid(skinPartType, scale, showGuides, skinProps, 0.5F, 0.5F, 0.5F, 0.25F);
            }
            GL11.glTranslated(-partOffset.getX() * scale, -partOffset.getY() * scale, -partOffset.getZ() * scale);
        }  
    }
    
    public static void renderBuildingGrid(ISkinPartType skinPartType, float scale, boolean showGuides, SkinProperties skinProps, float r, float g, float b, float a) {
        GL11.glTranslated(0, skinPartType.getBuildingSpace().getY() * scale, 0);
        GL11.glScalef(-1, -1, 1);
        SkinRenderHelper.renderGuidePart(skinPartType, scale, showGuides, skinProps, r, g, b, a);
        GL11.glScalef(-1, -1, 1);
        GL11.glTranslated(0, -skinPartType.getBuildingSpace().getY() * scale, 0);
    }
    
    private static void renderGuidePart(ISkinPartType part, float scale, boolean showGuides, SkinProperties skinProps, float r, float g, float b, float a) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(guideImage);
        
        IRectangle3D buildRec = part.getBuildingSpace();
        IRectangle3D guideRec = part.getGuideSpace();
        
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_LIGHTING);
        
        if (showGuides) {
            //render building grid
            //
            GL11.glColor4f(r, g, b, a);
            renderGuideBox(buildRec.getX(), buildRec.getY(), buildRec.getZ(), buildRec.getWidth(), buildRec.getHeight(), buildRec.getDepth(), scale);
            //render origin
            GL11.glColor4f(0F, 1F, 0F, 0.5F);
            renderGuideBox(-0.5F, -0.5F, -0.5F, 1, 1, 1, scale);
        }

        
        if (ConfigHandlerClient.showArmourerDebugRender) {
            //render debug box
            GL11.glColor4f(1F, 0F, 0F, 0.25F);
            renderGuideBox(guideRec.getX(), guideRec.getY(), guideRec.getZ(), guideRec.getWidth(), guideRec.getHeight(), guideRec.getDepth(), scale);
        }

        if (part.isModelOverridden(skinProps)) {
            GL11.glColor4f(0F, 0F, 1F, 0.25F);
            renderGuideBox(guideRec.getX(), guideRec.getY(), guideRec.getZ(), guideRec.getWidth(), guideRec.getHeight(), guideRec.getDepth(), scale);
        }
        
        GL11.glColor4f(1F, 1F, 1F, 1F);
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
    
    private static void renderGuideBox(double x, double y, double z, int width, int height, int depth, float scale) {
        renderGuideFace(ForgeDirection.DOWN, x, y, z, width, depth, scale);
        renderGuideFace(ForgeDirection.UP, x, y + height, z, width, depth, scale);
        renderGuideFace(ForgeDirection.EAST, x + width, y, z, depth, height, scale);
        renderGuideFace(ForgeDirection.WEST, x, y, z, depth, height, scale);
        renderGuideFace(ForgeDirection.NORTH, x, y, z, width, height, scale);
        renderGuideFace(ForgeDirection.SOUTH, x, y, z + depth, width, height, scale);
    }
    
    private static void renderGuideFace(ForgeDirection dir, double x, double y, double z, double sizeX, double sizeY, float scale) {
        RenderManager renderManager = RenderManager.instance;
        Tessellator tessellator = Tessellator.instance;
        
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        ModRenderHelper.enableAlphaBlend();
        
        
        float scale1 = 0.999F;
        //GL11.glScalef(scale1, scale1, scale1);
        GL11.glTranslated(x * scale, y * scale, z * scale);
        
        switch (dir) {
        case EAST:
            GL11.glRotated(-90, 0, 1, 0);
            break;
        case WEST:
            GL11.glRotated(-90, 0, 1, 0);
            break;
        case UP:
            GL11.glRotated(90, 1, 0, 0);
            break;
        case DOWN:
            GL11.glRotated(90, 1, 0, 0);
            break;
        default:
            break;
        }
        
        tessellator.setBrightness(15728880);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0, 0, 0, 0, 0);
        tessellator.addVertexWithUV(0, sizeY * scale, 0, sizeY, 0);
        tessellator.addVertexWithUV(sizeX * scale, sizeY * scale, 0, sizeY, sizeX);
        tessellator.addVertexWithUV(sizeX * scale, 0, 0, 0, sizeX);
        tessellator.draw();
        
        ModRenderHelper.disableAlphaBlend();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
