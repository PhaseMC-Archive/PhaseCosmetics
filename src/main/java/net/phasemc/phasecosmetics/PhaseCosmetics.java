package net.phasemc.phasecosmetics;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.phasemc.phasecosmetics.commands.TagsCommands;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PhaseCosmetics extends JavaPlugin {

    public static LuckPerms luckPerms;
    public static FileConfiguration config;
    public static Server server;

    @Override
    public void onEnable() {

        server = getServer();

        config = getConfig();
        config.addDefault("hide-item.enabled", "&bPlayers &7(&aShown&7)");
        config.addDefault("hide-item.disabled", "&bPlayers &7(&cHidden&7)");
        config.options().copyDefaults(true);
        saveDefaultConfig();

        getCommand("tags").setExecutor(new TagsCommands());
        server.getPluginManager().registerEvents(new EventListener(), this);
        luckPerms = LuckPermsProvider.get();

        server.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player p : server.getOnlinePlayers()) {

                Utils.giveJoinItems(p);

            }
        }, 0L, 20);

    }

    @Override
    public void onDisable() {
        saveConfig();
    }

}
