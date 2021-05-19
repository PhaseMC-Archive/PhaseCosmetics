package net.phasemc.phasecosmetics.commands;

import net.phasemc.phasecosmetics.PhaseCosmetics;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandBlock implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;

            switch (args[0].toLowerCase()) {

                case "smooth_stone":
                    PhaseCosmetics.server.dispatchCommand(PhaseCosmetics.server.getConsoleSender(), "give " + p.getName() + " skull 1 3 {display:{Name:\"§eSmooth Stone\"},SkullOwner:{Id:\"8d1afea0-0ce4-46de-bd8e-d41963eb8dec\",Properties:{textures:[{Value:\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGRkMGNkMTU4YzJiYjY2MTg2NTBlMzk1NGIyZDI5MjM3ZjViNGMwZGRjN2QyNThlMTczODBhYjY5NzlmMDcxIn19fQ==\"}]}}}");

            }

            return true;

        }

        sender.sendMessage(ChatColor.RED + "Only the player can use this command!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {

            return Arrays.asList("smooth_stone");

        }
        return Collections.emptyList();
    }
}
