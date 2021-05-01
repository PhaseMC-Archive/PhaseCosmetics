package net.phasemc.phasecosmetics.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandPlugin implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Too few arguments!");
            return false;
        }
        switch (args[0].toLowerCase()) {

            default:
                sender.sendMessage(ChatColor.RED + "No " + args[0] + " subcommand.");
            case "reload":
                sender.sendMessage(ChatColor.GREEN + "PhaseCosmetics config reloaded!");

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList(
                    "reload"
                    );
        }
        return Collections.emptyList();
    }
}
