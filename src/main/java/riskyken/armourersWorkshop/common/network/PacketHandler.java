package riskyken.armourersWorkshop.common.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import riskyken.armourersWorkshop.common.lib.LibModInfo;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiAdminPanel;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiArmourerBlockUtil;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiBipedRotations;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiButton;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiColourUpdate;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiHologramProjector;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiLoadSaveArmour;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiMannequinData;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiMiniArmourerCubeEdit;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiOutfitMakerUpdate;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiSetArmourerSkinProps;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiSetArmourerSkinType;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiSetSkin;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiSkinLibraryCommand;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientGuiToolOptionUpdate;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientKeyPress;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientLoadArmour;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientRequestGameProfile;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientRequestSkinData;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientSkinPart;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientSkinWardrobeUpdate;
import riskyken.armourersWorkshop.common.network.messages.client.MessageClientToolPaintBlock;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerClientCommand;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerEntitySkinData;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerGameProfile;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerLibraryFileList;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerLibrarySendSkin;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerMiniArmourerCubeEdit;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerMiniArmourerSkinData;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerPlayerLeftTrackingRange;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerSendSkinData;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerSkinInfoUpdate;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerSkinWardrobeUpdate;
import riskyken.armourersWorkshop.common.network.messages.server.MessageServerSyncConfig;

public class PacketHandler {

    public static final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(LibModInfo.CHANNEL);
    private static int packetId = 0;
    
    public static void init() {
        // Client messages.
        registerMessage(MessageClientLoadArmour.class, MessageClientLoadArmour.class, Side.SERVER);
        registerMessage(MessageClientKeyPress.class, MessageClientKeyPress.class, Side.SERVER);
        registerMessage(MessageClientSkinWardrobeUpdate.class, MessageClientSkinWardrobeUpdate.class, Side.SERVER);
        registerMessage(MessageClientRequestSkinData.class, MessageClientRequestSkinData.class, Side.SERVER);
        registerMessage(MessageClientSkinPart.class, MessageClientSkinPart.class, Side.SERVER);
        registerMessage(MessageClientToolPaintBlock.class, MessageClientToolPaintBlock.class, Side.SERVER);
        registerMessage(MessageClientRequestGameProfile.class, MessageClientRequestGameProfile.class, Side.SERVER);
        
        // Client GUI messages.
        registerMessage(MessageClientGuiLoadSaveArmour.class, MessageClientGuiLoadSaveArmour.class, Side.SERVER);
        registerMessage(MessageClientGuiColourUpdate.class, MessageClientGuiColourUpdate.class, Side.SERVER);
        registerMessage(MessageClientGuiButton.class, MessageClientGuiButton.class, Side.SERVER);
        registerMessage(MessageClientGuiSetSkin.class, MessageClientGuiSetSkin.class, Side.SERVER);
        registerMessage(MessageClientGuiToolOptionUpdate.class, MessageClientGuiToolOptionUpdate.class, Side.SERVER);
        registerMessage(MessageClientGuiSetArmourerSkinProps.class, MessageClientGuiSetArmourerSkinProps.class, Side.SERVER);
        registerMessage(MessageClientGuiBipedRotations.class, MessageClientGuiBipedRotations.class, Side.SERVER);
        registerMessage(MessageClientGuiSetArmourerSkinType.class, MessageClientGuiSetArmourerSkinType.class, Side.SERVER);
        registerMessage(MessageClientGuiMiniArmourerCubeEdit.class, MessageClientGuiMiniArmourerCubeEdit.class, Side.SERVER);
        registerMessage(MessageClientGuiMannequinData.class, MessageClientGuiMannequinData.class, Side.SERVER);
        registerMessage(MessageClientGuiAdminPanel.class, MessageClientGuiAdminPanel.class, Side.SERVER);
        registerMessage(MessageClientGuiSkinLibraryCommand.class, MessageClientGuiSkinLibraryCommand.class, Side.SERVER);
        registerMessage(MessageClientGuiArmourerBlockUtil.class, MessageClientGuiArmourerBlockUtil.class, Side.SERVER);
        registerMessage(MessageClientGuiHologramProjector.class, MessageClientGuiHologramProjector.class, Side.SERVER);
        registerMessage(MessageClientGuiOutfitMakerUpdate.Handler.class, MessageClientGuiOutfitMakerUpdate.class, Side.SERVER);
        
        //Server messages.
        registerMessage(MessageServerSkinInfoUpdate.class, MessageServerSkinInfoUpdate.class, Side.CLIENT);
        registerMessage(MessageServerPlayerLeftTrackingRange.class, MessageServerPlayerLeftTrackingRange.class, Side.CLIENT);
        registerMessage(MessageServerLibraryFileList.class, MessageServerLibraryFileList.class, Side.CLIENT);
        registerMessage(MessageServerSkinWardrobeUpdate.class, MessageServerSkinWardrobeUpdate.class, Side.CLIENT);
        registerMessage(MessageServerSendSkinData.class, MessageServerSendSkinData.class, Side.CLIENT);
        registerMessage(MessageServerClientCommand.class, MessageServerClientCommand.class, Side.CLIENT);
        registerMessage(MessageServerEntitySkinData.class, MessageServerEntitySkinData.class, Side.CLIENT);
        registerMessage(MessageServerLibrarySendSkin.class, MessageServerLibrarySendSkin.class, Side.CLIENT);
        registerMessage(MessageServerMiniArmourerSkinData.class, MessageServerMiniArmourerSkinData.class, Side.CLIENT);
        registerMessage(MessageServerMiniArmourerCubeEdit.class, MessageServerMiniArmourerCubeEdit.class, Side.CLIENT);
        registerMessage(MessageServerSyncConfig.class, MessageServerSyncConfig.class, Side.CLIENT);
        registerMessage(MessageServerGameProfile.class, MessageServerGameProfile.class, Side.CLIENT);
    }
    
    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
        networkWrapper.registerMessage(messageHandler, requestMessageType, packetId, side);
        packetId++;
    }
}
