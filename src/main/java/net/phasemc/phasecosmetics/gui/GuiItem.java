package net.phasemc.phasecosmetics.gui;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GuiItem {
    @Getter private final String name;
    @Getter private final String material;
    @Getter private final String executor;
    @Getter private final List<String> commands;

    public GuiItem(@NotNull String name, @NotNull String material, String executor, List<String> commands) {
        this.name = name;
        this.material = material;
        this.executor = executor;

        if(commands != null) {
            this.commands = commands;
        } else {
            this.commands = new ArrayList<>();
        }
    }

    public Material toMaterial() {
        if(material.contains(":")) {
            return Material.matchMaterial(material.split(":")[0]);
        }

        return Material.matchMaterial(material);
    }

    public ItemStack toItemStack() {
        if(material.contains(":")) {
            return new ItemStack(toMaterial(), 1, Short.parseShort(material.split(":")[1]));
        }

        return new ItemStack(toMaterial());
    }

    @Override
    public String toString() {
        return "GuiItem{" +
                "name='" + name + '\'' +
                ", material='" + material + '\'' +
                ", executor='" + executor + '\'' +
                ", commands=" + commands +
                '}';
    }
}
