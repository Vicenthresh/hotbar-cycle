package com.hotbarcycle;

import com.hotbarcycle.mixin.KeyBindingAccessor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HotbarCycle implements ClientModInitializer {
    public static final String MOD_ID = "hotbar-cycle";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static KeyBinding modifierKey;

    @Override
    public void onInitializeClient() {
        LOGGER.info("[HotbarCycle] initializing");

        modifierKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.hotbar-cycle.modifier",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "category.hotbar-cycle"
        ));

        LOGGER.info("[HotbarCycle] ready");
    }

    public static boolean isModifierHeld() {
        if (modifierKey == null) return false;
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        int keyCode = ((KeyBindingAccessor) modifierKey).getBoundKey().getCode();
        return GLFW.glfwGetKey(handle, keyCode) == GLFW.GLFW_PRESS;
    }
}
