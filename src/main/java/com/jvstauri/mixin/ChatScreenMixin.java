package com.jvstauri.mixin;

import com.jvstauri.gui.ChatTabs;
import com.jvstauri.hooks.ChatFilter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.jvstauri.hooks.ChatFilter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;

@Mixin(ChatScreen.class)
public class ChatScreenGui {
    private final ChatTabs tabs = new ChatTabs();
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (tabs.handleMouseClick(mouseX, mouseY, button)) {
            cir.setReturnValue(true); 
        }
    }
    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        tabs.renderTabs(guiGraphics, mouseX, mouseY);
    }
}