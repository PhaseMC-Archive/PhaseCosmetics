package net.phasemc.phasecosmetics.gui;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
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
        if(!GuiParser.getInventory().containsValue(event.getInventory())) {
            return;
        }

        for(GuiItem item : GuiParser.getItems().get(event.getInventory().getTitle()).values()) {
            if(item.toItemStack().equals(event.getCurrentItem())) {
                if(item.getCommands() != null && item.getExecutor() != null) {
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

                            if(command.contains("bungee:")) {
                                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                                out.writeUTF("Connect");
                                out.writeUTF(command.split(":")[1]);

                                // If you don't care about the player
                                // Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
                                // Else, specify them
                                //Player player = Bukkit.getPlayerExact("Example");

                                player.sendPluginMessage(PhaseCosmetics.plugin, "BungeeCord", out.toByteArray());

                            } else {
                                player.performCommand(command);
                            }
                        }
                    }
                }
                break;
            }
        }

        event.setCancelled(true);
    }
}
