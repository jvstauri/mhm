package com.jvstauri.gui;

import com.jvstauri.hooks.ChatFilter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public static final Map<String, Integer> colorMap = Map.of(
    "ALL", 0xFFAAAAAA,
    "PARTY", 0xFF5454fc,
    "GUILD", 0xFFFFAA00,
    "COOP", 0xFF51f2f2,
    "PRIVATE", 0xFFEA4EEA
);
public void renderTabs(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    String[] tabNames = {"ALL", "PARTY", "GUILD", "COOP", "PRIVATE"};
    int xOffset = 10;
    for (String name : tabNames) {
        boolean isActive = ChatFilter.getInstance().currentTab.equals(name);
        int categoryColor = colorMap.getOrDefault(name, 0xFFAA0000);
        int boxColor = isActive ? (categoryColor | 0xBB000000) : 0x44000000;
        int textColor = isActive ? 0xFFFFFFFF : categoryColor;
        guiGraphics.fill(xOffset, 10, xOffset + 45, 25, 0xAA000000);
        guiGraphics.drawString(Minecraft.getInstance().font, name, xOffset + 5, 14, textColor);
        xOffset += 50;
    }
}
public boolean handleMouseClick(double mouseX, double mouseY, int button) {
    if(button != 0) return false;
    if (mouseY < 10 || mouseY > 25) return;
    String[] tabNames = {"ALL", "PARTY", "GUILD", "COOP", "PRIVATE"};
    int xOffset = 10;
    for (String name : tabNames) {
        if (mouseX >= xOffset && mouseX <= xOffset + 45 && mouseY >= 10 && mouseY <= 25) {
            ChatFilter.getInstance().currentTab = name;
            updateChatDisplay(name);
            return true;
        }
        xOffset += 50;
    }
    return false;
}
private void updateChatDisplay(String tabName) {
    ChatFilter filter = ChatFilter.getInstance();
    ChatComponent chat = Minecraft.getInstance().gui.getChat();
    filter.isRefreshing = true;
    chat.clearMessages(false);
    List<Component> toDisplay;
    switch (tabName) {
        case "PARTY":   toDisplay = filter.partyMessages; break;
        case "GUILD":   toDisplay = filter.guildMessages; break;
        case "COOP":    toDisplay = filter.coopMessages; break;
        case "PRIVATE": toDisplay = filter.privateMessages; break;
        default:        toDisplay = filter.allMessages; break;
    }
    for (Component msg : toDisplay) {
        chat.addMessage(msg);
    }
    filter.isRefreshing = false;
}