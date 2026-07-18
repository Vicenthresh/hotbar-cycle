package com.hotbarcycle.mixin;

import com.hotbarcycle.HotbarCycle;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (client.currentScreen != null) return;
        if (!HotbarCycle.isModifierHeld()) return;

        ClientPlayerInteractionManager im = client.interactionManager;
        PlayerEntity player = client.player;
        int syncId = player.playerScreenHandler.syncId;

        if (vertical > 0.0) {
            for (int col = 0; col < 9; col++) {
                im.clickSlot(syncId, col + 27, col, SlotActionType.SWAP, player);
                im.clickSlot(syncId, col + 18, col, SlotActionType.SWAP, player);
                im.clickSlot(syncId, col + 9,  col, SlotActionType.SWAP, player);
            }
        } else if (vertical < 0.0) {
            for (int col = 0; col < 9; col++) {
                im.clickSlot(syncId, col + 9,  col, SlotActionType.SWAP, player);
                im.clickSlot(syncId, col + 18, col, SlotActionType.SWAP, player);
                im.clickSlot(syncId, col + 27, col, SlotActionType.SWAP, player);
            }
        }

        ci.cancel();
    }
}
