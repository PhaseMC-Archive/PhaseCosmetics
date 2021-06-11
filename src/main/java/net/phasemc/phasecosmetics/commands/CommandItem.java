package net.phasemc.phasecosmetics.commands;

import net.phasemc.phasecosmetics.Items;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandItem implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            p.getInventory().addItem(Items.valueOf(args[0].toUpperCase()).getItemStack());
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Only the player can use this command!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (args.length == 1) {

            //return Arrays.asList("teleport_bow", "rideable_bow");
            return Items.getItemIds().stream().filter(name -> name.startsWith(args[0])).collect(Collectors.toList());

        }

        return Collections.emptyList();
    }
}
