package net.phasemc.phasecosmetics;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Utils {
    public static String hideItemEnabledName = ChatColor.translateAlternateColorCodes('&', PhaseCosmetics.plugin.getConfig().getString("hide-item.enabled"));
    public static String hideItemDisabledName = ChatColor.translateAlternateColorCodes('&', PhaseCosmetics.plugin.getConfig().getString("hide-item.disabled"));
    public static String emptyTagsName = ChatColor.translateAlternateColorCodes('&', PhaseCosmetics.plugin.getConfig().getString("tags.empty"));
    public static String joinMessage = ChatColor.translateAlternateColorCodes('&', PhaseCosmetics.plugin.getConfig().getString("messages.join"));
    public static String leaveMessage = ChatColor.translateAlternateColorCodes('&', PhaseCosmetics.plugin.getConfig().getString("messages.leave"));
    public static List<String> uuidList = PhaseCosmetics.plugin.getConfig().getStringList("hide-item.player-uuids");
    public static Map<UUID, Long> grapplingHook = new HashMap<>();

    public static boolean isHideItem(String name) {
        return name.equals(hideItemDisabledName) || name.equals(hideItemEnabledName);
    }

    public static boolean isHideItem(ItemStack item) {
        if (item == null || item.getItemMeta().getDisplayName() == null) return false;
        return isHideItem(item.getItemMeta().getDisplayName()) && item.getType() == Material.INK_SACK;
    }

    public static boolean arePlayersHidden(Player p) {
        return uuidList.contains(p.getUniqueId().toString());
    }

    public static void addPlayerToList(Player p, String path) {
        List<String> list = uuidList;
        if (list == null) list = new ArrayList<>();
        list.add(p.getUniqueId().toString());
        PhaseCosmetics.plugin.getConfig().set(path, list);
        uuidList = PhaseCosmetics.plugin.getConfig().getStringList("hide-item.player-uuids");
    }

    public static void removePlayerFromList(Player p, String path) {
        List<String> list = uuidList;
        list.remove(p.getUniqueId().toString());
        PhaseCosmetics.plugin.getConfig().set(path, list);
        uuidList = PhaseCosmetics.plugin.getConfig().getStringList("hide-item.player-uuids");
    }

    public static void giveJoinItems(Player p) {
        if (p.getInventory().getItem(8) == null) {
            ItemStack hideItem = new ItemStack(Material.INK_SACK, 1, arePlayersHidden(p) ? (short) 8 : 10);
            ItemMeta hideItemMeta = hideItem.getItemMeta();
            hideItemMeta.setDisplayName(arePlayersHidden(p) ? hideItemDisabledName : hideItemEnabledName);
            hideItem.setItemMeta(hideItemMeta);

            p.getInventory().setItem(8, hideItem);
        }
    }

    public static void addGrapplingHookCooldown(UUID uuid, long cooldown) {
        grapplingHook.put(uuid, System.currentTimeMillis() + cooldown);
    }

    public static boolean isGrapplingHookCooldownOver(UUID uuid) {
        if (!grapplingHook.containsKey(uuid)) return true;
        else if (grapplingHook.get(uuid) < System.currentTimeMillis()) {
            grapplingHook.remove(uuid);
            return true;
        } else return false;
    }

    public static void scheduleMultipleDelayedTasks(long delay, Runnable... tasks) {
        for (int i = 0; i < tasks.length; i++) {
            PhaseCosmetics.server.getScheduler().scheduleSyncDelayedTask(PhaseCosmetics.plugin, tasks[i], delay * (i + 1));
        }
    }
}