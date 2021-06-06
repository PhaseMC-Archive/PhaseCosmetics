package net.phasemc.phasecosmetics;

import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
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
        String format = PhaseCosmetics.plugin.getConfig().getString("chat-format").replace("<PLAYER>", "%1$s").replace("<MESSAGE>", "%2$s");
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

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player p = (Player) e.getEntity().getShooter();

            if (e.getEntityType() == EntityType.ARROW) {
                if (e.getEntity().getCustomName().equals(ChatColor.LIGHT_PURPLE + "Teleport Arrow")) {
                    Location arrowLoc = e.getEntity().getLocation();
                    arrowLoc.setPitch(p.getLocation().getPitch());
                    arrowLoc.setYaw(p.getLocation().getYaw());
                    p.teleport(arrowLoc);
                    p.getWorld().playSound(arrowLoc, Sound.ENDERMAN_TELEPORT, 0.75F, 2F);
                    p.getWorld().spigot().playEffect(arrowLoc, Effect.ENDER_SIGNAL);
                    e.getEntity().remove();
                }

                e.getEntity().remove();
            } else if (e.getEntityType() == EntityType.SNOWBALL) {
                TNTPrimed tnt = (TNTPrimed) e.getEntity().getWorld().spawnEntity(e.getEntity().getLocation(), EntityType.PRIMED_TNT);
                tnt.setFuseTicks(61);
                tnt.setCustomName(ChatColor.GREEN + "3");
                tnt.setCustomNameVisible(true);
                Utils.scheduleMultipleDelayedTasks(20L, () -> tnt.setCustomName(ChatColor.YELLOW + "2"), () -> tnt.setCustomName(ChatColor.RED + "1"), () -> {
                    Location tntLoc = e.getEntity().getLocation();

                    for (Player player : PhaseCosmetics.server.getOnlinePlayers()) {
                        Location playerLoc = player.getLocation();

                        if (playerLoc.getWorld() != tntLoc.getWorld() || Math.abs(playerLoc.distance(tntLoc)) > 10) continue;

                        player.setVelocity(new Vector(1 / (playerLoc.getX() - tntLoc.getX()), 1.0D, 1 / (playerLoc.getZ() - tntLoc.getZ())));
                    }
                    tnt.getWorld().createExplosion(tntLoc, 0F);
                    tnt.remove();
                });
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            Player p = (Player) e.getEntity().getShooter();

            if (e.getEntityType() == EntityType.ARROW) {
                if (p.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Teleport Bow")) {
                    e.getEntity().setCustomName(ChatColor.LIGHT_PURPLE + "Teleport Arrow");
                    e.getEntity().setCustomNameVisible(true);
                } else if (p.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Rideable Bow")) {
                    e.getEntity().setCustomName(ChatColor.GREEN + "Rideable Arrow");
                    e.getEntity().setCustomNameVisible(true);
                    Utils.scheduleMultipleDelayedTasks(1L, () -> e.getEntity().setPassenger(p));
                }
            }
        }
    }
}
