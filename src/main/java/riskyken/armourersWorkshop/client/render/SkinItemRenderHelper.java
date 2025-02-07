package riskyken.armourersWorkshop.client.render;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import riskyken.armourersWorkshop.api.common.IPoint3D;
import riskyken.armourersWorkshop.api.common.IRectangle3D;
import riskyken.armourersWorkshop.api.common.skin.Rectangle3D;
import riskyken.armourersWorkshop.api.common.skin.data.ISkinPointer;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinPartType;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinPartTypeTextured;
import riskyken.armourersWorkshop.api.common.skin.type.ISkinType;
import riskyken.armourersWorkshop.client.model.skin.IEquipmentModel;
import riskyken.armourersWorkshop.client.skin.cache.ClientSkinCache;
import riskyken.armourersWorkshop.common.skin.data.Skin;
import riskyken.armourersWorkshop.common.skin.data.SkinPart;
import riskyken.armourersWorkshop.common.skin.data.SkinPointer;
import riskyken.armourersWorkshop.common.skin.type.SkinTypeRegistry;
import riskyken.armourersWorkshop.utils.SkinNBTHelper;

@SideOnly(Side.CLIENT)
public final class SkinItemRenderHelper {

    public static boolean debugShowFullBounds;
    public static boolean debugShowPartBounds;
    public static boolean debugShowTextureBounds;
    public static boolean debugShowTargetBounds;
    public static boolean debugSpin;

    private SkinItemRenderHelper() {
    }

    public static void renderSkinAsItem(ItemStack stack, boolean showSkinPaint, int targetWidth, int targetHeight) {
        if (SkinNBTHelper.stackHasSkinData(stack)) {
            SkinPointer skinPointer = SkinNBTHelper.getSkinPointerFromStack(stack);
            renderSkinAsItem(skinPointer, showSkinPaint, false, targetWidth, targetHeight);
        }
    }

    public static void renderSkinAsItem(ISkinPointer skinPointer, boolean showSkinPaint, boolean doLodLoading, int targetWidth, int targetHeight) {
        renderSkinAsItem(ClientSkinCache.INSTANCE.getSkin(skinPointer), skinPointer, showSkinPaint, doLodLoading, targetWidth, targetHeight);
    }

    public static void renderSkinAsItem(Skin skin, ISkinPointer skinPointer, boolean showSkinPaint, boolean doLodLoading, int targetWidth, int targetHeight) {
        if (skin == null) {
            return;
        }

        float blockScale = 16F;
        float mcScale = 1F / blockScale;

        ArrayList<IRectangle3D> boundsParts = new ArrayList<IRectangle3D>();
        ArrayList<IRectangle3D> boundsTextures = new ArrayList<IRectangle3D>();

        IRectangle3D boundsTexture = null;

        for (int i = 0; i < skin.getPartCount(); i++) {
            SkinPart skinPart = skin.getParts().get(i);
            if (!(skin.getSkinType() == SkinTypeRegistry.skinBow && i > 0)) {
                Rectangle3D bounds = skinPart.getPartBounds();
                IPoint3D offset = skinPart.getPartType().getItemRenderOffset();
                Rectangle3D rec = new Rectangle3D(bounds.getX() + offset.getX(), bounds.getY() + offset.getY(), bounds.getZ() + offset.getZ(), bounds.getWidth(), bounds.getHeight(), bounds.getDepth());
                boundsParts.add(rec);
            }
        }

        if (skin.hasPaintData()) {
            ArrayList<ISkinPartType> parts = skin.getSkinType().getSkinParts();
            for (int i = 0; i < parts.size(); i++) {
                ISkinPartType part = parts.get(i);
                if (part instanceof ISkinPartTypeTextured) {
                    if (part.getItemRenderTextureBounds() != null) {
                        boundsTextures.add(part.getItemRenderTextureBounds());
                    }
                }
            }
        }

        int minX = 256;
        int minY = 256;
        int minZ = 256;
        int maxX = -256;
        int maxY = -256;
        int maxZ = -256;

        for (int i = 0; i < boundsParts.size(); i++) {
            IRectangle3D rec = boundsParts.get(i);
            minX = Math.min(minX, rec.getX());
            minY = Math.min(minY, rec.getY());
            minZ = Math.min(minZ, rec.getZ());

            maxX = Math.max(maxX, rec.getWidth() + rec.getX());
            maxY = Math.max(maxY, rec.getHeight() + rec.getY());
            maxZ = Math.max(maxZ, rec.getDepth() + rec.getZ());
        }
        for (int i = 0; i < boundsTextures.size(); i++) {
            IRectangle3D rec = boundsTextures.get(i);
            minX = Math.min(minX, rec.getX());
            minY = Math.min(minY, rec.getY());
            minZ = Math.min(minZ, rec.getZ());

            maxX = Math.max(maxX, rec.getWidth() + rec.getX());
            maxY = Math.max(maxY, rec.getHeight() + rec.getY());
            maxZ = Math.max(maxZ, rec.getDepth() + rec.getZ());
        }

        Rectangle3D maxBounds = new Rectangle3D(minX, minY, minZ, maxX - minX, maxY - minY, maxZ - minZ);

        GL11.glPushMatrix();

        // GL11.glRotatef(-45, 0, 1, 0);
        // GL11.glRotatef(-30, 1, 0, 0);

        if (debugSpin) {
            float angle = (((System.currentTimeMillis() / 10) % 360));

            GL11.glRotatef(angle, 0, 1, 0);
        }

        // Target bounds
        if (debugShowTargetBounds) {
            drawBounds(new Rectangle3D(-targetWidth / 2, -targetHeight / 2, -targetWidth / 2, targetWidth, targetHeight, targetWidth), 0, 0, 255);
        }

        int biggerSize = Math.max(maxBounds.getWidth(), maxBounds.getHeight());
        biggerSize = Math.max(biggerSize, maxBounds.getDepth());

        float newScaleW = (float) targetWidth / Math.max(maxBounds.getWidth(), maxBounds.getDepth());
        float newScaleH = (float) targetHeight / maxBounds.getHeight();

        float newScale = Math.min(newScaleW, newScaleH);

        GL11.glScalef(newScale, newScale, newScale);

        // Center the skin
        GL11.glTranslated(-(maxBounds.getWidth() / 2F + maxBounds.getX()) * mcScale, 0, 0);
        GL11.glTranslated(0, -(maxBounds.getHeight() / 2F + maxBounds.getY()) * mcScale, 0);
        GL11.glTranslated(0, 0, -(maxBounds.getDepth() / 2F + maxBounds.getZ()) * mcScale);

        renderSkinWithHelper(skin, skinPointer, showSkinPaint, doLodLoading);

        if (debugShowFullBounds) {
            drawBounds(maxBounds, 255, 255, 0);
        }
        if (debugShowPartBounds) {
            for (int i = 0; i < boundsParts.size(); i++) {
                drawBounds(boundsParts.get(i), 255, 0, 0);
            }
        }
        if (debugShowTextureBounds) {
            for (int i = 0; i < boundsTextures.size(); i++) {
                drawBounds(boundsTextures.get(i), 0, 255, 0);
            }
        }

        GL11.glPopMatrix();
    }

    public static void drawBounds(IRectangle3D rec, int r, int g, int b) {
        float scale = 1F / 16F;
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(rec.getX() * scale, rec.getY() * scale, rec.getZ() * scale, (rec.getX() + rec.getWidth()) * scale, (rec.getY() + rec.getHeight()) * scale, (rec.getZ() + rec.getDepth()) * scale);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f((float) r / 255F, (float) g / 255F, (float) b / 255F, 1F);
        GL11.glLineWidth(1.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        // GL11.glDepthMask(false);
        RenderGlobal.drawOutlinedBoundingBox(aabb, -1);
        // GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void renderSkinWithHelper(Skin skin, ISkinPointer skinPointer, boolean showSkinPaint, boolean doLodLoading) {
        ISkinType skinType = skinPointer.getIdentifier().getSkinType();
        if (skinType == null) {
            skinType = skin.getSkinType();
        }

        IEquipmentModel targetModel = SkinModelRenderer.INSTANCE.getModelForEquipmentType(skinType);

        if (targetModel == null) {
            renderSkinWithoutHelper(skinPointer, doLodLoading);
            return;
        }

        targetModel.render(null, null, skin, showSkinPaint, skinPointer.getSkinDye(), null, true, 0, doLodLoading);
    }

    public static void renderSkinWithoutHelper(ISkinPointer skinPointer, boolean doLodLoading) {
        Skin skin = ClientSkinCache.INSTANCE.getSkin(skinPointer);
        if (skin == null) {
            return;
        }
        float scale = 1F / 16F;
        for (int i = 0; i < skin.getParts().size(); i++) {
            GL11.glPushMatrix();
            SkinPart skinPart = skin.getParts().get(i);
            IPoint3D offset = skinPart.getPartType().getOffset();
            GL11.glTranslated(offset.getX() * scale, (offset.getY() + 1) * scale, offset.getZ() * scale);
            SkinPartRenderData renderData = new SkinPartRenderData(skinPart, 0.0625F, skinPointer.getSkinDye(), null, 0, true, true, true, null);
            SkinPartRenderer.INSTANCE.renderPart(renderData);
            GL11.glPopMatrix();
        }

    }
}
