package net.phasemc.phasecosmetics.gui;

import lombok.Getter;
import net.phasemc.phasecosmetics.PhaseCosmetics;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiParser {
    private final FileConfiguration config;
    private final List<String> layout;
    @Getter private static Inventory inventory = null;
    @Getter private static Map<String, GuiItem> items;

    public GuiParser(FileConfiguration config) throws IllegalArgumentException {
        this.config = config;
        layout = config.getStringList("gui.layout");

        if(layout.size() > 0 && (layout.size() + 1) % 9 == 0) {
            throw new IllegalArgumentException("Layouts in minecraft can only be multiples of 9");
        }

        items = new HashMap<>();
    }

    public void makeInventory() throws NullPointerException,IllegalArgumentException {
        for(String item : layout) {
            ConfigurationSection itemConfig = config.getConfigurationSection("gui.items." + item);

            if(!items.containsKey(item) && !item.isEmpty()) {
                // Checking that the item is defined in items (config.yml)
                if(itemConfig == null) {
                    throw new NullPointerException("Item \"" + item + "\" was referenced in layout but was not defined in items, unable to proceed!");
                }

                // Checking that the item's material name exists
                if(itemConfig.get("name") != null) {
                    String name = config.getString(itemConfig + ".name");

                    // Checking if the item has both commands and an executor specified (on click command execution support in gui)
                    if(itemConfig.get( "executor") != null && itemConfig.get("commands") != null) {
                        //items.put(item, new GuiItem(name, config.getString(itemPath + ".executor"), config.getStringList(itemPath + ".commands")));
                    } else {
                        //items.put(item, new GuiItem(name, null, null));
                    }
                } else {
                    throw new IllegalArgumentException("Item \"" + item +  "\" must have a material name!");
                }
            }
        }

        Inventory inventory = PhaseCosmetics.server.createInventory(null, layout.size(), config.getString("gui.name"));

        for(int i = 0; i < layout.size(); i++) {
            GuiItem item = items.get(layout.get(i));

            try {
                inventory.setItem(i, item.toItemStack());
            } catch (NullPointerException e) {
                throw new NullPointerException("Item \"" + layout.get(i) + "\" has an invalid material name \"" + item.getName() + "\"!");
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Item \"" + layout.get(i) + "\" has an invalid material name \"" + item.getName() + "\"!");
            }
        }

        GuiParser.inventory = inventory;
    }
}