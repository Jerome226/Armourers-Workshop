package moe.plushie.armourers_workshop.core.skin.data.serialize.v14;

import moe.plushie.armourers_workshop.core.api.ISkinType;
import moe.plushie.armourers_workshop.core.skin.data.Skin;
import moe.plushie.armourers_workshop.core.skin.data.SkinPart;
import moe.plushie.armourers_workshop.core.skin.data.SkinTexture;
import moe.plushie.armourers_workshop.core.skin.data.property.SkinProperties;
import moe.plushie.armourers_workshop.core.skin.data.serialize.SkinSerializer;
import moe.plushie.armourers_workshop.core.skin.exception.InvalidCubeTypeException;
import moe.plushie.armourers_workshop.core.skin.SkinTypes;
import moe.plushie.armourers_workshop.core.utils.SkinLog;
import moe.plushie.armourers_workshop.core.utils.StreamUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public final class SkinSerializerV14 {

    /**
     * V14 Changes
     * <p>
     * Add protected skin option.
     * Move skin paint to each skin part. (can't have paint without a part)
     * Moved skin type to the start of the file.
     * <p>
     * <p>
     * Add skin properties to each skin part, also add more data types.
     */

    private static final int FILE_VERSION = 14;

    private static final String TAG_SKIN_HEADER = "AW-SKIN-START";

    private static final String TAG_SKIN_PROPS_HEADER = "PROPS-START";
    private static final String TAG_SKIN_PROPS_FOOTER = "PROPS-END";

    private static final String TAG_SKIN_TYPE_HEADER = "TYPE-START";
    private static final String TAG_SKIN_TYPE_FOOTER = "TYPE-END";

    private static final String TAG_SKIN_PAINT_HEADER = "PAINT-START";
    private static final String TAG_SKIN_PAINT_FOOTER = "PAINT-END";

    private static final String TAG_SKIN_PART_HEADER = "PART-START";
    private static final String TAG_SKIN_PART_FOOTER = "PART-END";

    private static final String TAG_SKIN_FOOTER = "AW-SKIN-END";

    private SkinSerializerV14() {
    }

    public static void writeToStream(Skin skin, DataOutputStream stream) throws IOException {
        // Write the skin file version.
        stream.writeInt(FILE_VERSION);
        // Write skin header.
        StreamUtils.writeString(stream, StandardCharsets.US_ASCII, TAG_SKIN_HEADER);
        // Write the skin type.
        StreamUtils.writeString(stream, StandardCharsets.US_ASCII, TAG_SKIN_TYPE_HEADER);
        stream.writeUTF(skin.getType().getRegistryName().toString());
        StreamUtils.writeString(stream, StandardCharsets.US_ASCII, TAG_SKIN_TYPE_FOOTER);
        // Write skin props.
        StreamUtils.writeString(stream, StandardCharsets.US_ASCII, TAG_SKIN_PROPS_HEADER);
        skin.getProperties().writeToStream(stream);
        StreamUtils.writeString(stream, StandardCharsets.US_ASCII, TAG_SKIN_PROPS_FOOTER);
        // Write paint data.
        StreamUtils.writeString(stream, StandardCharsets.US_ASCII, TAG_SKIN_PAINT_HEADER);
        if (skin.hasPaintData()) {
            stream.writeBoolean(true);
            for (int i = 0; i < SkinTexture.TEXTURE_SIZE; i++) {
                stream.writeInt(skin.getPaintData()[i]);
            }
        } else {
            stream.writeBoolean(false);
        }
        StreamUtils.writeString(stream, StandardCharsets.US_ASCII, TAG_SKIN_PAINT_FOOTER);
        // Write parts
        stream.writeByte(skin.getParts().size());
        for (SkinPart skinPart : skin.getParts()) {
            StreamUtils.writeString(stream, StandardCharsets.US_ASCII, TAG_SKIN_PART_HEADER);
            SkinPartSerializerV14.saveSkinPart(skinPart, stream);
            StreamUtils.writeString(stream, StandardCharsets.US_ASCII, TAG_SKIN_PART_FOOTER);
        }
        // Write skin footer.
        StreamUtils.writeString(stream, StandardCharsets.US_ASCII, TAG_SKIN_FOOTER);
    }

    public static Skin readSkinFromStream(DataInputStream stream, int fileVersion) throws IOException, InvalidCubeTypeException {
        if (!StreamUtils.readString(stream, StandardCharsets.US_ASCII).equals(TAG_SKIN_HEADER)) {
            SkinLog.error("Error loading skin header.");
        }

        if (!StreamUtils.readString(stream, StandardCharsets.US_ASCII).equals(TAG_SKIN_TYPE_HEADER)) {
            SkinLog.error("Error loading skin type header.");
        }

        String regName = stream.readUTF();
        ISkinType skinType = SkinTypes.byName(regName);

        if (!StreamUtils.readString(stream, StandardCharsets.US_ASCII).equals(TAG_SKIN_TYPE_FOOTER)) {
            SkinLog.error("Error loading skin type footer.");
        }

        if (skinType == null) {
            throw new InvalidCubeTypeException();
        }

        if (!StreamUtils.readString(stream, StandardCharsets.US_ASCII).equals(TAG_SKIN_PROPS_HEADER)) {
            SkinLog.error("Error loading skin props header.");
        }
        SkinProperties properties = new SkinProperties();
        IOException e = null;
        try {
            properties.readFromStream(stream, fileVersion);
        } catch (IOException propE) {
            SkinLog.error("prop load failed");
            e = propE;
        }

        if (!StreamUtils.readString(stream, StandardCharsets.US_ASCII).equals(TAG_SKIN_PROPS_FOOTER)) {
            SkinLog.error("Error loading skin props footer.");
        }

        if (!StreamUtils.readString(stream, StandardCharsets.US_ASCII).equals(TAG_SKIN_PAINT_HEADER)) {
            SkinLog.error("Error loading skin paint header.");
        }

        int[] paintData = null;
        boolean hasPaintData = stream.readBoolean();
        if (hasPaintData) {
            paintData = new int[SkinTexture.TEXTURE_SIZE];
            for (int i = 0; i < SkinTexture.TEXTURE_SIZE; i++) {
                paintData[i] = stream.readInt();
            }
        }

        if (!StreamUtils.readString(stream, StandardCharsets.US_ASCII).equals(TAG_SKIN_PAINT_FOOTER)) {
            SkinLog.error("Error loading skin paint footer.");
        }

        int size = stream.readByte();
        ArrayList<SkinPart> parts = new ArrayList<SkinPart>();
        for (int i = 0; i < size; i++) {
            if (!StreamUtils.readString(stream, StandardCharsets.US_ASCII).equals(TAG_SKIN_PART_HEADER)) {
                SkinLog.error("Error loading skin part header.");
            }
            SkinPart part = SkinPartSerializerV14.loadSkinPart(stream, fileVersion);
            parts.add(part);
            if (!StreamUtils.readString(stream, StandardCharsets.US_ASCII).equals(TAG_SKIN_PART_FOOTER)) {
                SkinLog.error("Error loading skin part footer.");
            }
        }

        if (!StreamUtils.readString(stream, StandardCharsets.US_ASCII).equals(TAG_SKIN_FOOTER)) {
            SkinLog.error("Error loading skin footer.");
        }

        return SkinSerializer.makeSkin(skinType, properties, paintData, parts);
    }

    public static ISkinType readSkinTypeNameFromStream(DataInputStream stream, int fileVersion) throws IOException {
        if (!StreamUtils.readString(stream, StandardCharsets.US_ASCII).equals(TAG_SKIN_HEADER)) {
            SkinLog.error("Error loading skin header.");
        }

        if (!StreamUtils.readString(stream, StandardCharsets.US_ASCII).equals(TAG_SKIN_TYPE_HEADER)) {
            SkinLog.error("Error loading skin type header.");
        }

        String regName = stream.readUTF();
        ISkinType skinType = SkinTypes.byName(regName);

        return skinType;
    }
}
