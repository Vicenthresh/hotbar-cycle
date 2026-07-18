package com.shiftscroll;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiftScroll implements ClientModInitializer {
    public static final String MOD_ID = "shift-scroll";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static KeyBinding modifierKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Shift Scroll loaded");

        modifierKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.shift-scroll.modifier",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "category.shift-scroll"
        ));
    }

    public static boolean isModifierHeld() {
        return modifierKey != null && modifierKey.isPressed();
    }
}
