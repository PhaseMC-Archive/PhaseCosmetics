package net.phasemc.phasecosmetics.gui;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GuiItem {
    @Getter private final String name;
    @Getter private final String executor;
    @Getter private final List<String> commands;

    public GuiItem(@NotNull String name, String executor, List<String> commands) {
        this.name = name;
        this.executor = executor;

        if(commands != null) {
            this.commands = commands;
        } else {
            this.commands = new ArrayList<>();
        }
    }

    public Material toMaterial() {
        return Material.matchMaterial(name);
    }

    public ItemStack toItemStack() {
        return new ItemStack(toMaterial());
    }
}
