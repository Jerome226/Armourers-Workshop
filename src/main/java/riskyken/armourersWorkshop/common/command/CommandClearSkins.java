package riskyken.armourersWorkshop.common.command;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import riskyken.armourersWorkshop.common.wardrobe.ExPropsPlayerSkinData;

public class CommandClearSkins extends ModCommand {

    @Override
    public String getCommandName() {
        return "clearSkins";
    }
    
    @Override
    public List addTabCompletionOptions(ICommandSender commandSender, String[] currentCommand) {
        if (currentCommand.length == 2) {
            return getListOfStringsMatchingLastWord(currentCommand, getPlayers());
        }
        return null;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] currentCommand) {
        if (currentCommand.length != 2) {
            throw new WrongUsageException(getCommandUsage(commandSender), (Object)currentCommand);
        }
        String playerName = currentCommand[1];
        EntityPlayerMP player = getPlayer(commandSender, playerName);
        if (player == null) {
            return;
        }
        ExPropsPlayerSkinData.get(player).clearAllEquipmentStacks();
    }
}
