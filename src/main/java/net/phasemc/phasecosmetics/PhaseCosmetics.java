package net.phasemc.phasecosmetics;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.phasemc.phasecosmetics.commands.TagsCommands;
import org.bukkit.plugin.java.JavaPlugin;

public final class PhaseCosmetics extends JavaPlugin {

    public static LuckPerms luckPerms;

    @Override
    public void onEnable() {

        getCommand("tags").setExecutor(new TagsCommands());
        getServer().getPluginManager().registerEvents(new EventListener(), this);
        luckPerms = LuckPermsProvider.get();

    }

    @Override
    public void onDisable() {

    }
}
