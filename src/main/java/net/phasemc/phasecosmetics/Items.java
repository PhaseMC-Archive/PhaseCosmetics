package net.phasemc.phasecosmetics;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public enum Items {

    TELEPORT_WAND(ChatColor.BLUE + "Teleport Wand", Material.BLAZE_ROD),
    RIDEABLE_BOW(ChatColor.GREEN + "Rideable Bow", Material.BOW),
    TELEPORT_BOW(ChatColor.LIGHT_PURPLE + "Teleport Bow", Material.BOW);

    Items(String name, Material material) {

        this.name = name;
        this.material = material;

    }

    private final String name;
    private final Material material;

    public ItemStack getItemStack() {

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;

    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public static List<String> getItemIds() {

        List<String> itemNames = new ArrayList<String>();
        for (Items item : values()) {

            itemNames.add(item.name().toLowerCase());

        }
        return itemNames;

    }

}
