package net.phasemc.phasecosmetics;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.phasemc.phasecosmetics.commands.CommandPlugin;
import net.phasemc.phasecosmetics.commands.CommandTags;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PhaseCosmetics extends JavaPlugin {

    public static LuckPerms luckPerms;
    public static FileConfiguration config;
    public static Server server;
    public static JavaPlugin plugin;

    @Override
    public void onEnable() {

        plugin = this;

        server = getServer();

        config = getConfig();
        config.addDefault("hide-item.enabled", "&bPlayers &7(&aShown&7)");
        config.addDefault("hide-item.disabled", "&bPlayers &7(&cHidden&7)");
        config.addDefault("chat-format", "%vault_prefix%<PLAYER> &8» &f<MESSAGE>");
        config.addDefault("tags.empty", "&cNo Tags");
        config.addDefault("messages.join", "%vault_prefix%%player_name% %vault_suffix% &ehopped into the lobby!");
        config.addDefault("messages.leave", "%vault_prefix%%player_name% %vault_suffix% &ehopped out of the lobby!");
        config.options().copyDefaults(true);
        saveDefaultConfig();

        getCommand("tags").setExecutor(new CommandTags());
        getCommand("phasecosmetics").setExecutor(new CommandPlugin());
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
