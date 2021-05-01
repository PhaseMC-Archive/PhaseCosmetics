package net.phasemc.phasecosmetics.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CommandFly implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            p.setAllowFlight(!p.getAllowFlight());
            p.sendMessage(ChatColor.GREEN + "You can" + (p.getAllowFlight() ? "" : "'t") + " fly now!");
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Only the player can use this command!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
