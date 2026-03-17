package com.jvstauri.hooks;

import net.minecraft.network.chat.Component;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;

public class ChatFilter {
    private static final ChatFilter INSTANCE = new ChatFilter();
    public List<Component> allMessages = new ArrayList<>();
    public List<Component> partyMessages = new ArrayList<>();
    public List<Component> guildMessages = new ArrayList<>();
    public List<Component> coopMessages = new ArrayList<>();
    public List<Component> privateMessages = new ArrayList<>();
    public String currentMessager = "";
    public String currentTab = "ALL";
    public boolean isRefreshing = false;

    public static ChatFilter getInstance() { return INSTANCE; }
    
    public void onMessageReceived(Component message) {
        if (isRefreshing) return;
        String rawText = message.getString();
        String messageType = "ALL";
        addLimited(allMessages, message);
        if (rawText.startsWith("Party >")) {
            addLimited(partyMessages, message);
            messageType = "PARTY";
        }
        else if (rawText.startsWith("Guild >")) {
            addLimited(guildMessages, message);
            messageType = "GUILD";
        }
        else if (rawText.startsWith("Co-op >")) {
            addLimited(coopMessages, message);
            messageType = "COOP";
        }
        else if (rawText.startsWith("From ") || rawText.startsWith("To ")) {
            addLimited(privateMessages, message);
            messageType = "PRIVATE";
            String parts[] = rawText.split(" ");
            if(parts.length >= 2) {
                String currentMessager = parts[1];
                if (currentMessager.startsWith("[") && parts.length >= 3) {
                    currentMessager = parts[2];
                }
                currentMessager = currentMessager.replace(":", "");
            }
        }
        if(currentTab.equals(messageType) && !currentTab.equals("ALL")) {
            this.isRefreshing = true;
            Minecraft.getInstance().gui.getChat().addMessage(message);
            this.isRefreshing = false;
        }
    }
    private void addLimited(List<Component> list, Component msg) {
        list.add(msg);
        if (list.size() > 100) {
            list.remove(0);
        }
    }
}