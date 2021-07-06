package net.phasemc.phasecosmetics.gui;

import de.likewhat.customheads.CustomHeads;
import de.likewhat.customheads.category.Category;
import de.likewhat.customheads.category.CustomHead;
import lombok.Getter;
import net.phasemc.phasecosmetics.PhaseCosmetics;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiParser {
    private final FileConfiguration config;
    private final ConfigurationSection guiConfig;
    private final Map<String, List<String>> layout;
    @Getter private static Map<String, Inventory> inventory;
    @Getter private static Map<String, Map<String, GuiItem>> items;
    @Getter private static Map<ItemStack, GuiItem> guiItems;

    public GuiParser(FileConfiguration config) throws IllegalArgumentException {
        this.config = config;
        guiConfig = config.getConfigurationSection("gui");

        layout = new HashMap<>();
        for(String key : guiConfig.getKeys(false)) {
            List<String> layoutGui = guiConfig.getStringList(key + ".layout");

            int layoutSize = layoutGui.size();
            if(layoutSize > 0 && (layoutSize + 1) % 9 != 0) {
                throw new IllegalArgumentException("Layouts in minecraft can only be multiples of 9");
            } else {
                layout.put(key, layoutGui);
            }
        }

        inventory = new HashMap<>();
        items = new HashMap<>();
        guiItems = new HashMap<>();
    }

    public void makeInventory() throws NullPointerException, IllegalArgumentException, GuiParseException {
        List<String> keys = new ArrayList<>();

        for(String key : guiConfig.getKeys(false)) {
            ConfigurationSection guiKeyConfig = guiConfig.getConfigurationSection(key);
            ConfigurationSection guiKeyItemsConfig = guiKeyConfig.getConfigurationSection("items");

            for (String item : layout.get(key)) {
                ConfigurationSection itemConfig = guiKeyItemsConfig.getConfigurationSection(item);

                items.computeIfAbsent(key, k -> new HashMap<>());

                if (!items.get(key).containsKey(item) && !item.isEmpty()) {
                    // Checking that the item is defined in items (config.yml)
                    if (itemConfig == null) {
                        throw new NullPointerException("Item \"" + item + "\" was referenced in layout but was not defined in items, unable to proceed!");
                    }

                    // Checking that the item's material name exists
                    if (itemConfig.get("name") != null) {
                        String name = itemConfig.getString("name");
                        String material = itemConfig.getString("material");

                        // Checking if the item has both commands and an executor specified (on click command execution support in gui)
                        if (itemConfig.get("executor") != null && itemConfig.get("commands") != null) {
                            items.get(key).put(item, new GuiItem(name, material, itemConfig.getString("executor"), itemConfig.getStringList("commands")));
                        } else {
                            items.get(key).put(item, new GuiItem(name, material, null, null));
                        }
                    } else {
                        throw new GuiParseException(key, item, itemConfig.getString("material"));
                    }
                }
            }

            keys.add(key);
        }

        for(int i = 0; i < layout.size(); i++) {
            String key = keys.get(i);
            Inventory inventory = PhaseCosmetics.server.createInventory(null, layout.get(key).size(), key);

            for (int index = 0; index < layout.size(); index++) {
                for(int layoutSize = 0; layoutSize < layout.get(key).size(); layoutSize++) {
                    GuiItem item = items.get(key).get(layout.get(key).get(layoutSize));
                    
                    if(item.getName().equalsIgnoreCase("air")) {
                        continue;
                    }

                    if(item.getMaterial().toLowerCase().startsWith("heads:")) {
                        String[] args = item.getMaterial().split(":");

                        Category category = CustomHeads.getCategoryManager().getCategory(args[1]);
                        CustomHead customHead = CustomHeads.getApi().getHead(category, Integer.parseInt(args[2]));

                        ItemMeta itemMeta = customHead.getItemMeta();
                        itemMeta.setDisplayName(item.getName());
                        customHead.setItemMeta(itemMeta);

                        inventory.setItem(layoutSize, customHead);
                        guiItems.put(customHead, item);
                        continue;
                    }

                    try {
                        // Why does bukkit have to be so stupid, why not give the current item meta rather than a clone, it adds so many unnecessary lines of code, god why...
                        ItemStack itemStack = item.toItemStack();
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        itemMeta.setDisplayName(item.getName());
                        itemStack.setItemMeta(itemMeta);

                        inventory.setItem(layoutSize, itemStack);
                        guiItems.put(itemStack, item);
                    } catch (NullPointerException e) {
                        throw new GuiParseException(key, item);
                    }
                }
            }

            GuiParser.inventory.put(key, inventory);
        }
    }
}