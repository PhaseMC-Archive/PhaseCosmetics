package net.phasemc.phasecosmetics.commands;

import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.query.QueryMode;
import net.luckperms.api.query.QueryOptions;
import net.phasemc.phasecosmetics.PhaseCosmetics;
import net.phasemc.phasecosmetics.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommandTags implements TabCompleter, CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            Player p = (Player) sender;

            Inventory tagsInv = Bukkit.createInventory(null, 54, "Tags Menu");
            setupBorders(tagsInv, Material.STAINED_GLASS_PANE, 15);

            User user = PhaseCosmetics.luckPerms.getUserManager().getUser(p.getUniqueId());
            Set<String> permissions = user.resolveInheritedNodes(QueryOptions.builder(QueryMode.CONTEXTUAL).build()).stream().map(Node::getKey).collect(Collectors.toSet());

            for (String perm : permissions) {

                if (perm.startsWith("cosmetic.suffix")) {

                    for (int i = 0; i < 54; i++) {

                        if (tagsInv.getItem(i) == null) {

                            ItemStack suffixItem = new ItemStack(Material.NAME_TAG);
                            ItemMeta suffixItemMeta = suffixItem.getItemMeta();
                            suffixItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', perm.replace("cosmetic.suffix.", "")));
                            suffixItem.setItemMeta(suffixItemMeta);

                            tagsInv.setItem(i, suffixItem);
                            break;
                        }

                    }

                }

            }
            if (!tagsInv.contains(Material.NAME_TAG)) {

                ItemStack noTagsPlaceholder = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
                ItemMeta noTagsPlaceholderMeta = noTagsPlaceholder.getItemMeta();
                noTagsPlaceholderMeta.setDisplayName(Utils.emptyTagsName);
                noTagsPlaceholder.setItemMeta(noTagsPlaceholderMeta);

                for (int i = 0; i < 54; i++) {

                    if (tagsInv.getItem(i) == null) {

                        tagsInv.setItem(i, noTagsPlaceholder);

                    }
                }

            }

            p.openInventory(tagsInv);

            return true;

        }

        sender.sendMessage(ChatColor.RED + "Only the player can use this command!");
        return true;
    }

    private void setupBorders(Inventory inv, Material mat, int durability) {

        ItemStack placeholder = new ItemStack(mat, 1, (short) durability);
        ItemMeta placeholderItemMeta = placeholder.getItemMeta();
        placeholderItemMeta.setDisplayName(" ");
        placeholder.setItemMeta(placeholderItemMeta);

        ItemStack resetItem = new ItemStack(Material.STAINED_GLASS, 1, (short) 14);
        ItemMeta resetItemMeta = resetItem.getItemMeta();
        resetItemMeta.setDisplayName(ChatColor.RED + "Reset");
        resetItem.setItemMeta(resetItemMeta);

        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeItemMeta = closeItem.getItemMeta();
        closeItemMeta.setDisplayName(ChatColor.RED + "Close");
        closeItem.setItemMeta(closeItemMeta);

        inv.setItem(0, placeholder);
        inv.setItem(1, placeholder);
        inv.setItem(2, placeholder);
        inv.setItem(3, placeholder);
        inv.setItem(4, placeholder);
        inv.setItem(5, placeholder);
        inv.setItem(6, placeholder);
        inv.setItem(7, placeholder);
        inv.setItem(8, placeholder);
        inv.setItem(9, placeholder);
        inv.setItem(17, placeholder);
        inv.setItem(18, placeholder);
        inv.setItem(26, placeholder);
        inv.setItem(27, placeholder);
        inv.setItem(35, placeholder);
        inv.setItem(36, placeholder);
        inv.setItem(44, placeholder);
        inv.setItem(45, placeholder);
        inv.setItem(46, placeholder);
        inv.setItem(47, placeholder);
        inv.setItem(48, placeholder);
        inv.setItem(49, closeItem);
        inv.setItem(50, resetItem);
        inv.setItem(51, placeholder);
        inv.setItem(52, placeholder);
        inv.setItem(53, placeholder);


    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }
}
