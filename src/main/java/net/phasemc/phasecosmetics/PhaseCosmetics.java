package net.phasemc.phasecosmetics;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.phasemc.phasecosmetics.commands.*;
import net.phasemc.phasecosmetics.gui.GuiItemCommandListener;
import net.phasemc.phasecosmetics.gui.GuiParser;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public final class PhaseCosmetics extends JavaPlugin {
    public static LuckPerms luckPerms;
    public static Server server;
    public static JavaPlugin plugin;

    @Override
    public void onEnable() {
        luckPerms = LuckPermsProvider.get();
        server = getServer();
        plugin = this;

        getCommand("tags").setExecutor(new CommandTags());
        getCommand("phasecosmetics").setExecutor(new CommandPlugin());
        getCommand("fly").setExecutor(new CommandFly());
        getCommand("block").setExecutor(new CommandBlock());
        getCommand("item").setExecutor(new CommandItem());

        try {
            plugin.saveDefaultConfig();

            new GuiParser(getConfig()).makeInventory();

            getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        } catch (Exception e) {
            e.printStackTrace();
        }

        getCommand("gui").setExecutor(new CommandGui());

        server.getPluginManager().registerEvents(new EventListener(), this);
        server.getPluginManager().registerEvents(new GuiItemCommandListener(), this);
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterOutgoingPluginChannel(this);
    }
}