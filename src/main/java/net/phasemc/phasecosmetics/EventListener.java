package net.phasemc.phasecosmetics;

import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.SuffixNode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

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

        }

    }

}
