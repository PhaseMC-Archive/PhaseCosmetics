package net.phasemc.phasecosmetics;

import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

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

            } else if (e.getClick().isLeftClick() && e.getCurrentItem().getType() == Material.BARRIER) {

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

                    if (p == null) continue;
                    e.getPlayer().showPlayer(p);

                }

            } else {

                Utils.addPlayerToList(e.getPlayer(), "hide-item.player-uuids");
                for (Player p : PhaseCosmetics.server.getOnlinePlayers()) {

                    if (p == null) continue;
                    e.getPlayer().hidePlayer(p);

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
        e.setMessage(p.hasPermission("cosmetic.chat.color") ? ChatColor.translateAlternateColorCodes('&', e.getMessage()) : e.getMessage());

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        String joinMessage = PlaceholderAPI.setPlaceholders(e.getPlayer(), Utils.joinMessage);
        e.setJoinMessage(joinMessage);

        if (e.getPlayer().hasPermission("cosmetic.fly")) e.getPlayer().setAllowFlight(true);

        for (String uuid : Utils.uuidList) {

            PhaseCosmetics.server.getPlayer(uuid).hidePlayer(e.getPlayer());

        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {

        String leaveMessage = PlaceholderAPI.setPlaceholders(e.getPlayer(), Utils.leaveMessage);
        e.setQuitMessage(leaveMessage);

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {

        if (e.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.YELLOW + "Smooth Stone")) {

            e.getBlockPlaced().setType(Material.DOUBLE_STEP);
            e.getBlockPlaced().setData((byte) 8);

        }

    }

    @EventHandler
    public void onFishing(PlayerFishEvent e) {

        if (!Utils.isGrapplingHookCooldownOver(e.getPlayer().getUniqueId())) {

            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "You have cooldown on this item for a second!");

        } else if (e.getState() == PlayerFishEvent.State.FAILED_ATTEMPT || e.getState() == PlayerFishEvent.State.CAUGHT_FISH || e.getState() == PlayerFishEvent.State.IN_GROUND) {

            Location hookLoc = e.getHook().getLocation();
            Location playerLoc = e.getPlayer().getLocation();

            e.getPlayer().setVelocity(new Vector(hookLoc.getX() - playerLoc.getX(), 1.0D, hookLoc.getZ() - playerLoc.getZ()));
            Utils.addGrapplingHookCooldown(e.getPlayer().getUniqueId(), 1000L);

        }

    }

}
