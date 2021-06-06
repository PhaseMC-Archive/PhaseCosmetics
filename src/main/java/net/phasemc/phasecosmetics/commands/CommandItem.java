package net.phasemc.phasecosmetics.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandItem implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            switch (args[0].toLowerCase()) {
                case "teleport_bow":
                    ItemStack teleportBow = new ItemStack(Material.BOW);
                    ItemMeta teleportBowMeta = teleportBow.getItemMeta();
                    teleportBowMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Teleport Bow");
                    teleportBow.setItemMeta(teleportBowMeta);
                    p.getInventory().addItem(teleportBow);
                    break;
                case "rideable_bow":
                    ItemStack rideableBow = new ItemStack(Material.BOW);
                    ItemMeta rideableBowMeta = rideableBow.getItemMeta();
                    rideableBowMeta.setDisplayName(ChatColor.GREEN + "Rideable Bow");
                    rideableBow.setItemMeta(rideableBowMeta);
                    p.getInventory().addItem(rideableBow);
                    break;
            }
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Only the player can use this command!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("teleport_bow", "rideable_bow");
        }

        return Collections.emptyList();
    }
}
