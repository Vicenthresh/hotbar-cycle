package com.shiftscroll.mixin;

import com.shiftscroll.RowShifter;
import com.shiftscroll.ShiftScroll;
import net.minecraft.entity.player.PlayerInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Inject(method = "scrollInHotbar", at = @At("HEAD"), cancellable = true)
    public void onScrollInHotbar(double scrollAmount, CallbackInfo ci) {
        if (!ShiftScroll.isModifierHeld()) return;

        PlayerInventory self = (PlayerInventory) (Object) this;

        if (scrollAmount > 0.0) {
            RowShifter.shiftUp(self);
        } else if (scrollAmount < 0.0) {
            RowShifter.shiftDown(self);
        }

        ci.cancel();
    }
}
