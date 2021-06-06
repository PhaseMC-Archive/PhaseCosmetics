package net.phasemc.phasecosmetics.commands;

import net.phasemc.phasecosmetics.gui.GuiParser;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGui implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if(GuiParser.getInventory() != null) {
                Player player = (Player) sender;

                player.openInventory(GuiParser.getInventory());
            } else {
                sender.sendMessage(ChatColor.RED + "The inventory is null, check console for details on why the gui failed to parse!");
            }
        }

        return true;
    }
}
