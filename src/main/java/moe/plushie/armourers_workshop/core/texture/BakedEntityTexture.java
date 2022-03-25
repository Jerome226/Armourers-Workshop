package moe.plushie.armourers_workshop.core.texture;

import moe.plushie.armourers_workshop.api.skin.ISkinPartType;
import moe.plushie.armourers_workshop.core.utils.color.PaintColor;
import moe.plushie.armourers_workshop.core.model.PlayerTextureModel;
import moe.plushie.armourers_workshop.core.skin.painting.SkinPaintTypes;
import moe.plushie.armourers_workshop.core.utils.Rectangle3i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.resources.IResource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
public class BakedEntityTexture {

    private final boolean slim;
    private final ResourceLocation location;

    private final HashMap<Integer, PaintColor> allColors = new HashMap<>();
    private final HashMap<ISkinPartType, HashMap<Integer, PaintColor>> allParts = new HashMap<>();

    private final HashMap<ISkinPartType, Rectangle3i> allBounds = new HashMap<>();

    public BakedEntityTexture(ResourceLocation location, boolean slim) {
        this.slim = slim;
        this.location = location;
        BufferedImage bufferedImage;
        try {
            IResource resource = Minecraft.getInstance().getResourceManager().getResource(location);
            bufferedImage = ImageIO.read(resource.getInputStream());
            if (bufferedImage != null) {
//                slim = (bufferedImage.getRGB(54, 20) & 0xff000000) == 0;
                this.loadColors(bufferedImage.getWidth(), bufferedImage.getHeight(), slim, bufferedImage::getRGB);
            }
        } catch (IOException ignored) {
        }
    }

    public BakedEntityTexture(ResourceLocation location, NativeImage image, boolean slim) {
        this.slim = slim;
        this.location = location;
        this.loadColors(image.getWidth(), image.getHeight(), slim, (x, y) -> {
            int color = image.getPixelRGBA(x, y);
            int red = (color << 16) & 0xff0000;
            int blue = (color >> 16) & 0x0000ff;
            return (color & 0xff00ff00) | red | blue;
        });
    }

    public boolean isSlim() {
        return slim;
    }

    private void loadColors(int width, int height, boolean slim, IColorAccessor accessor) {
        for (PlayerTextureModel model : PlayerTextureModel.getPlayerModels(width, height, slim)) {
            HashMap<Integer, PaintColor> part = allParts.computeIfAbsent(model.getPartType(), k -> new HashMap<>());
            allBounds.put(model.getPartType(), model.getBounds());
            model.forEach((u, v, x, y, z, dir) -> {
                int color = accessor.getRGB(u, v);
                if ((color & 0xff000000) == 0) {
                    return; // ignore transparent color
                }
                PaintColor paintColor = PaintColor.of(color, SkinPaintTypes.NORMAL);
                part.put(getPosKey(x, y, z, dir), paintColor);
                allColors.put(getUVKey(u, v), paintColor);
            });
        }
    }


    public PaintColor getColor(int u, int v) {
        return allColors.get(getUVKey(u, v));
    }

    public PaintColor getColor(int x, int y, int z, Direction dir, ISkinPartType partType) {
        HashMap<Integer, PaintColor> part = allParts.get(partType);
        Rectangle3i bounds = allBounds.get(partType);
        if (part == null || bounds == null) {
            return null;
        }
        x = MathHelper.clamp(x, bounds.getMinX(), bounds.getMaxX() - 1);
        y = MathHelper.clamp(y, bounds.getMinY(), bounds.getMaxY() - 1);
        z = MathHelper.clamp(z, bounds.getMinZ(), bounds.getMaxZ() - 1);
        return part.get(getPosKey(x, y, z, dir));
    }

    public ResourceLocation getLocation() {
        return location;
    }

    private int getPosKey(int x, int y, int z, Direction dir) {
        return (dir.get3DDataValue() & 0xff) << 24 | (z & 0xff) << 16 | (y & 0xff) << 8 | (x & 0xff);
    }

    private int getUVKey(int u, int v) {
        return (v & 0xffff) << 16 | (u & 0xffff);
    }

    interface IColorAccessor {
        int getRGB(int x, int y);
    }
}
