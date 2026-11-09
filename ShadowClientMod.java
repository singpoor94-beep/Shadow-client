package com.shadowclient;

import com.shadowclient.ui.ClickGuiScreen;
import com.shadowclient.ui.ClickGuiScreen.ModuleEntry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShadowClientMod implements ClientModInitializer {
    public static KeyBinding openGuiKey;
    private final Map<Integer, Boolean> keyWasDown = new HashMap<>();

    @Override
    public void onInitializeClient() {
        ClickGuiScreen.initModules();

        // Register RIGHT_SHIFT keybind for physical keyboard
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.shadowclient.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                KeyBinding.Category.MISC
        ));

        // Poll physical keyboard keybinds on each client tick
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ClickGuiScreen());
                } else if (client.currentScreen instanceof ClickGuiScreen) {
                    client.setScreen(null);
                }
            }

            if (client.player == null || client.currentScreen != null) return;
            long windowHandle = client.getWindow().getHandle();

            for (List<ModuleEntry> modList : ClickGuiScreen.CATEGORIES.values()) {
                for (ModuleEntry mod : modList) {
                    if (mod.keyCode != GLFW.GLFW_KEY_UNKNOWN) {
                        boolean isDown = InputUtil.isKeyPressed(windowHandle, mod.keyCode);
                        boolean wasDown = keyWasDown.getOrDefault(mod.keyCode, false);
                        if (isDown && !wasDown) {
                            mod.enabled = !mod.enabled;
                        }
                        keyWasDown.put(mod.keyCode, isDown);
                    }
                }
            }

            // AutoSprint active logic when enabled
            ModuleEntry autoSprint = ClickGuiScreen.getModule("AutoSprint");
            if (autoSprint != null && autoSprint.enabled && client.player.forwardSpeed > 0) {
                client.player.setSprinting(true);
            }
        });

        // Render HUD Overlay (Watermark, Coordinates, FPS, and Active Module ArrayList)
        HudElementRegistry.addLast(Identifier.of("shadowclient", "hud_overlay"), (drawContext, tickCounter) -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.options.hudHidden) return;

            // Watermark
            drawContext.fill(8, 8, 192, 22, 0x900A0A12);
            drawContext.fill(8, 8, 10, 22, 0xFF00F5D4);
            drawContext.drawTextWithShadow(mc.textRenderer, "SHADOW CLIENT v1.0 [1.21.11]", 14, 11, 0xFF00F5D4);

            // FPS & Coordinates
            String fpsText = "FPS: " + mc.getCurrentFps();
            drawContext.drawTextWithShadow(mc.textRenderer, fpsText, 10, 26, 0xFFAAAAAA);

            String coords = String.format("XYZ: %.1f, %.1f, %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ());
            int screenHeight = mc.getWindow().getScaledHeight();
            drawContext.drawTextWithShadow(mc.textRenderer, coords, 10, screenHeight - 14, 0xFF00F5D4);

            // Gradient Active Module ArrayList (Top-Right)
            int yOffset = 10;
            int screenWidth = mc.getWindow().getScaledWidth();
            for (List<ModuleEntry> mods : ClickGuiScreen.CATEGORIES.values()) {
                for (ModuleEntry mod : mods) {
                    if (mod.enabled && mod.visibleOnArrayList) {
                        String label = mod.name;
                        int textWidth = mc.textRenderer.getWidth(label);
                        int boxX = screenWidth - textWidth - 14;
                        drawContext.fill(boxX - 4, yOffset - 2, screenWidth - 6, yOffset + 10, 0x880A0A12);
                        drawContext.fill(screenWidth - 8, yOffset - 2, screenWidth - 6, yOffset + 10, 0xFF7B2CBF);
                        drawContext.drawTextWithShadow(mc.textRenderer, label, boxX, yOffset, 0xFF00F5D4);
                        yOffset += 12;
                    }
                }
            }
        });
    }
}
