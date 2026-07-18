package com.shiftscroll;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class RowShifter {

    public static void shiftUp(PlayerInventory inv) {
        for (int col = 0; col < 9; col++) {
            ItemStack saved = inv.main.get(col);
            inv.main.set(col, inv.main.get(col + 9));
            inv.main.set(col + 9, inv.main.get(col + 18));
            inv.main.set(col + 18, inv.main.get(col + 27));
            inv.main.set(col + 27, saved);
        }
        inv.markDirty();
    }

    public static void shiftDown(PlayerInventory inv) {
        for (int col = 0; col < 9; col++) {
            ItemStack saved = inv.main.get(col + 27);
            inv.main.set(col + 27, inv.main.get(col + 18));
            inv.main.set(col + 18, inv.main.get(col + 9));
            inv.main.set(col + 9, inv.main.get(col));
            inv.main.set(col, saved);
        }
        inv.markDirty();
    }
}
