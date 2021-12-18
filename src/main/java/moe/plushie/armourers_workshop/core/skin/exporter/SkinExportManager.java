package moe.plushie.armourers_workshop.core.skin.exporter;//package moe.plushie.armourers_workshop.core.skin.exporter;
//
//import moe.plushie.armourers_workshop.core.api.common.skin.ISkinExporter;
//import moe.plushie.armourers_workshop.core.skin.data.Skin;
//import moe.plushie.armourers_workshop.core.utils.SkinLogger;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.logging.log4j.Level;
//
//import java.io.File;
//import java.util.ArrayList;
//
//public final class SkinExportManager {
//
//    private static final ArrayList<ISkinExporter> SKIN_EXPORTERS;
//
//    private SkinExportManager() {}
//
//    static {
//        SKIN_EXPORTERS = new ArrayList<ISkinExporter>();
//        SKIN_EXPORTERS.add(new SkinExporterWavefrontObj());
//        SKIN_EXPORTERS.add(new SkinExporterPolygon());
//    }
//
//
//    public static ISkinExporter getSkinExporter(String fileExtension) {
//        if (StringUtils.isEmpty(fileExtension)) {
//            return null;
//        }
//        for (ISkinExporter skinExporter : SKIN_EXPORTERS) {
//            for (String ext : skinExporter.getFileExtensions()) {
//                if (ext.equalsIgnoreCase(fileExtension)) {
//                    return skinExporter;
//                }
//            }
//        }
//        return null;
//    }
//
//    public static void exportSkin(Skin skin, String fileExtension, File filePath, String filename, float scale) {
//        ISkinExporter skinExporter = getSkinExporter(fileExtension);
//        if (skinExporter != null) {
//            exportSkin(skin, skinExporter, filePath, filename, scale);
//        } else {
//            SkinLogger.error(String.format("Could not export to %s format.", fileExtension));
//        }
//    }
//
//    public static void exportSkin(Skin skin, ISkinExporter skinExporter, File filePath, String filename, float scale) {
//        skinExporter.exportSkin(skin, filePath, filename, scale);
//    }
//
//    public static String[] getExporters() {
//        ArrayList<String> exporters = new ArrayList<String>();
//        for (ISkinExporter skinExporter : SKIN_EXPORTERS) {
//            for (String ext : skinExporter.getFileExtensions()) {
//                exporters.add(ext);
//            }
//        }
//        return exporters.toArray(new String[exporters.size()]);
//    }
//}
