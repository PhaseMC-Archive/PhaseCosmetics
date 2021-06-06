package net.phasemc.phasecosmetics.gui;

import net.phasemc.phasecosmetics.PhaseCosmetics;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GuiItemCommandListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(GuiParser.getInventory() == null || !GuiParser.getInventory().equals(event.getInventory())) {
            return;
        }

        for(GuiItem item : GuiParser.getItems().values()) {
            if(item.toItemStack().equals(event.getCurrentItem())) {
                if(item.getCommands() != null) {
                    Player player = (Player) event.getWhoClicked();

                    if(item.getExecutor().equalsIgnoreCase("console")) {
                        ConsoleCommandSender consoleCommandSender = PhaseCosmetics.server.getConsoleSender();

                        for(String command : item.getCommands()) {
                            command = command.replace("%player%", player.getName());

                            Bukkit.dispatchCommand(consoleCommandSender, command);
                        }
                    }

                    if(item.getExecutor().equalsIgnoreCase("player")) {
                        for(String command : item.getCommands()) {
                            command = command.replace("%player%", player.getName());

                            player.performCommand(command);
                        }
                    }
                }
                break;
            }
        }

        event.setCancelled(true);
    }
}
