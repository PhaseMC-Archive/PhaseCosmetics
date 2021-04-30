package net.phasemc.phasecosmetics;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String hideItemEnabledName = ChatColor.translateAlternateColorCodes('&', PhaseCosmetics.config.getString("hide-item.enabled"));
    public static String hideItemDisabledName = ChatColor.translateAlternateColorCodes('&', PhaseCosmetics.config.getString("hide-item.disabled"));
    public static String emptyTagsName = ChatColor.translateAlternateColorCodes('&', PhaseCosmetics.config.getString("tags.empty"));

    public static boolean isHideItem(String name) {

        return name.equals(hideItemDisabledName) || name.equals(hideItemEnabledName);

    }

    public static boolean isHideItem(ItemStack item) {

        return isHideItem(item.getItemMeta().getDisplayName()) && item.getType() == Material.INK_SACK;

    }

    public static boolean arePlayersHidden(Player p) {

        return PhaseCosmetics.config.getStringList("hide-item.player-uuids").contains(p.getUniqueId().toString());

    }

    public static void addPlayerToList(Player p, String path) {

        List<String> list = PhaseCosmetics.config.getStringList(path);
        if (list == null) list = new ArrayList<String>();
        list.add(p.getUniqueId().toString());
        PhaseCosmetics.config.set(path, list);

    }

    public static void removePlayerFromList(Player p, String path) {

        List<String> list = PhaseCosmetics.config.getStringList(path);
        list.remove(p.getUniqueId().toString());
        PhaseCosmetics.config.set(path, list);

    }

    public static void giveJoinItems(Player p) {

        if (p.getInventory().getItem(8) == null) {

            ItemStack hideItem = new ItemStack(Material.INK_SACK, 1, Utils.arePlayersHidden(p) ? (short) 10 : 8);
            ItemMeta hideItemMeta = hideItem.getItemMeta();
            hideItemMeta.setDisplayName(arePlayersHidden(p) ? hideItemDisabledName : hideItemEnabledName);
            hideItem.setItemMeta(hideItemMeta);

            p.getInventory().setItem(8, hideItem);

        }

    }

}
