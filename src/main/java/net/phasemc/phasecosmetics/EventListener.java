package net.phasemc.phasecosmetics;

import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class EventListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {

        if (e.getInventory().getName().equals("Tags Menu") && e.getInventory().getItem(0).getItemMeta().getDisplayName().equals(" ")) {

            if (e.getClick().isLeftClick() && e.getCurrentItem().getType() == Material.NAME_TAG) {

                User user = PhaseCosmetics.luckPerms.getUserManager().getUser(e.getWhoClicked().getUniqueId());
                String suffix = user.getCachedData().getMetaData().getSuffix();
                if (suffix == null) suffix = "";
                user.data().remove(SuffixNode.builder(suffix, 1).build());
                user.data().add(SuffixNode.builder(e.getCurrentItem().getItemMeta().getDisplayName(), 1).build());
                PhaseCosmetics.luckPerms.getUserManager().saveUser(user);


            } else if (e.getClick().isLeftClick() && e.getCurrentItem().getType() == Material.STAINED_GLASS) {

                User user = PhaseCosmetics.luckPerms.getUserManager().getUser(e.getWhoClicked().getUniqueId());
                String suffix = user.getCachedData().getMetaData().getSuffix();
                if (suffix == null) suffix = "";
                user.data().remove(SuffixNode.builder(suffix, 1).build());
                PhaseCosmetics.luckPerms.getUserManager().saveUser(user);

            } else if (e.getCurrentItem().getType() == Material.BARRIER) {

                e.getWhoClicked().closeInventory();

            }

            e.setCancelled(true);

            Player p = (Player) e.getWhoClicked();
            p.updateInventory();

        } else if (Utils.isHideItem(e.getCurrentItem())) {

            e.setCancelled(true);

        }

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {

        if (Utils.isHideItem(e.getItemDrop().getItemStack())) e.setCancelled(true);

    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {

        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && Utils.isHideItem(e.getItem())) {

            e.getItem().setDurability(Utils.arePlayersHidden(e.getPlayer()) ? (short) 10 : 8);
            ItemMeta itemMeta = e.getItem().getItemMeta();
            itemMeta.setDisplayName(Utils.arePlayersHidden(e.getPlayer()) ? Utils.hideItemEnabledName : Utils.hideItemDisabledName);
            e.getItem().setItemMeta(itemMeta);
            if (Utils.arePlayersHidden(e.getPlayer())) {

                Utils.removePlayerFromList(e.getPlayer(), "hide-item.player-uuids");
                for (Player p : PhaseCosmetics.server.getOnlinePlayers()) {

                    e.getPlayer().hidePlayer(p);

                }
                for (OfflinePlayer p : PhaseCosmetics.server.getOfflinePlayers()) {

                    e.getPlayer().hidePlayer(p.getPlayer());

                }

            } else {

                Utils.addPlayerToList(e.getPlayer(), "hide-item.player-uuids");
                for (Player p : PhaseCosmetics.server.getOnlinePlayers()) {

                    e.getPlayer().showPlayer(p);

                }
                for (OfflinePlayer p : PhaseCosmetics.server.getOfflinePlayers()) {

                    e.getPlayer().showPlayer(p.getPlayer());

                }

            }

        }

    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();
        String format = PhaseCosmetics.config.getString("chat-format").replace("<PLAYER>", "%1$s").replace("<MESSAGE>", "%2$s");
        format = PlaceholderAPI.setPlaceholders(e.getPlayer(), format);
        e.setFormat(format);
        e.setMessage(e.getMessage());

    }

}
