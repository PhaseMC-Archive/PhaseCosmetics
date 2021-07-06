package net.phasemc.phasecosmetics.gui;

public class GuiParseException extends Exception {
    public GuiParseException(String gui, String item, String material) {
        super("GUI " + gui + ", failed to parse item " + item + ", with material name " + material);
    }

    public GuiParseException(String gui, GuiItem item) {
        super("GUI " + gui + ", failed to parse item " + item);
    }
}
