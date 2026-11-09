package com.shadowclient.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClickGuiScreen extends Screen {
    public static final Map<String, List<ModuleEntry>> CATEGORIES = new LinkedHashMap<>();
    private ModuleEntry listeningForKey = null;

    public static class SettingEntry {
        public String name;
        public String type; // "BOOL", "SLIDER", "MODE"
        public boolean boolVal;
        public double numVal, minVal, maxVal;
        public String[] modes;
        public int modeIndex;

        public static SettingEntry bool(String name, boolean def) {
            SettingEntry s = new SettingEntry();
            s.name = name;
            s.type = "BOOL";
            s.boolVal = def;
            return s;
        }

        public static SettingEntry slider(String name, double def, double min, double max) {
            SettingEntry s = new SettingEntry();
            s.name = name;
            s.type = "SLIDER";
            s.numVal = def;
            s.minVal = min;
            s.maxVal = max;
            return s;
        }

        public static SettingEntry mode(String name, int defIdx, String... modes) {
            SettingEntry s = new SettingEntry();
            s.name = name;
            s.type = "MODE";
            s.modes = modes;
            s.modeIndex = defIdx;
            return s;
        }
    }

    public static class ModuleEntry {
        public String name;
        public boolean enabled;
        public int keyCode;
        public String keyName;
        public boolean expanded = false;
        public boolean visibleOnArrayList = true;
        public List<SettingEntry> settings = new ArrayList<>();

        public ModuleEntry(String name, boolean enabled, int keyCode, String keyName) {
            this.name = name;
            this.enabled = enabled;
            this.keyCode = keyCode;
            this.keyName = keyName;
        }

        public ModuleEntry add(SettingEntry s) {
            this.settings.add(s);
            return this;
        }
    }

    public static void initModules() {
        if (!CATEGORIES.isEmpty()) return;

        List<ModuleEntry> combat = new ArrayList<>();
        combat.add(new ModuleEntry("SmoothAimAssist", true, GLFW.GLFW_KEY_UNKNOWN, "NONE")
                .add(SettingEntry.slider("Radius", 6.0, 3.0, 8.0))
                .add(SettingEntry.slider("Smooth", 0.22, 0.05, 0.50))
                .add(SettingEntry.bool("Weapon & Armor Check", true)));
        combat.add(new ModuleEntry("ElytraMaceDive", true, GLFW.GLFW_KEY_V, "V")
                .add(SettingEntry.slider("Swap Dist", 5.0, 3.0, 8.0))
                .add(SettingEntry.bool("Auto Chestplate", true))
                .add(SettingEntry.bool("Impact Smash", true)));
        combat.add(new ModuleEntry("MaceCombat", true, GLFW.GLFW_KEY_UNKNOWN, "NONE")
                .add(SettingEntry.slider("Min Fall", 2.5, 1.5, 10.0))
                .add(SettingEntry.bool("Auto WindCharge", true)));
        combat.add(new ModuleEntry("StunSlam", true, GLFW.GLFW_KEY_G, "G")
                .add(SettingEntry.bool("Normal StunSlam", true))
                .add(SettingEntry.bool("Elytra StunSlam", true))
                .add(SettingEntry.slider("Axe Slot", 2.0, 1.0, 9.0)));
        combat.add(new ModuleEntry("SpearCombat", true, GLFW.GLFW_KEY_R, "R")
                .add(SettingEntry.bool("Quick Lunge", true))
                .add(SettingEntry.bool("Keep 2-4.5m", true))
                .add(SettingEntry.mode("Lunge Lvl", 2, "I", "II", "III")));
        combat.add(new ModuleEntry("AutoShieldBreaker", true, GLFW.GLFW_KEY_UNKNOWN, "NONE")
                .add(SettingEntry.bool("Instant SnapBack", true))
                .add(SettingEntry.slider("Axe Slot", 2.0, 1.0, 9.0)));
        combat.add(new ModuleEntry("CrystalPvP", true, GLFW.GLFW_KEY_C, "C")
                .add(SettingEntry.bool("AutoCrystal", true))
                .add(SettingEntry.bool("AutoAnchor", true))
                .add(SettingEntry.bool("Surround Feet", true)));
        CATEGORIES.put("COMBAT", combat);

        List<ModuleEntry> utility = new ArrayList<>();
        utility.add(new ModuleEntry("AutoPotionMacro", true, GLFW.GLFW_KEY_X, "X")
                .add(SettingEntry.bool("Strength II", true))
                .add(SettingEntry.bool("Speed II", true))
                .add(SettingEntry.bool("Fire Res", true))
                .add(SettingEntry.bool("Skip Active", true)));
        utility.add(new ModuleEntry("AutoTotem", true, GLFW.GLFW_KEY_UNKNOWN, "NONE")
                .add(SettingEntry.slider("HP Hearts", 4.0, 1.0, 10.0))
                .add(SettingEntry.mode("Fallback", 0, "Totem", "GApple", "Shield")));
        utility.add(new ModuleEntry("FastPlace", true, GLFW.GLFW_KEY_UNKNOWN, "NONE")
                .add(SettingEntry.slider("Delay Ticks", 0.0, 0.0, 4.0)));
        utility.add(new ModuleEntry("FastEXP", true, GLFW.GLFW_KEY_UNKNOWN, "NONE")
                .add(SettingEntry.slider("Throw Delay", 0.0, 0.0, 4.0)));
        CATEGORIES.put("UTILITY", utility);

        List<ModuleEntry> render = new ArrayList<>();
        render.add(new ModuleEntry("PlayerESP", true, GLFW.GLFW_KEY_UNKNOWN, "NONE")
                .add(SettingEntry.bool("Health Bar", true))
                .add(SettingEntry.bool("2D/3D Box", true)));
        render.add(new ModuleEntry("ChestESP", true, GLFW.GLFW_KEY_UNKNOWN, "NONE")
                .add(SettingEntry.bool("Shulkers", true))
                .add(SettingEntry.bool("EnderChests", true)));
        render.add(new ModuleEntry("FreeLook", true, GLFW.GLFW_KEY_LEFT_ALT, "L_ALT")
                .add(SettingEntry.slider("Distance", 4.5, 2.0, 10.0)));
        render.add(new ModuleEntry("XRay", false, GLFW.GLFW_KEY_Z, "Z")
                .add(SettingEntry.slider("Opacity", 0.30, 0.10, 1.0)));
        render.add(new ModuleEntry("Fullbright", true, GLFW.GLFW_KEY_B, "B")
                .add(SettingEntry.slider("Gamma", 16.0, 1.0, 16.0)));
        CATEGORIES.put("RENDER", render);

        List<ModuleEntry> perf = new ArrayList<>();
        perf.add(new ModuleEntry("AutoSprint", true, GLFW.GLFW_KEY_UNKNOWN, "NONE")
                .add(SettingEntry.bool("Omni-Sprint", true)));
        perf.add(new ModuleEntry("FPSBooster", true, GLFW.GLFW_KEY_UNKNOWN, "NONE")
                .add(SettingEntry.bool("Entity Culling", true))
                .add(SettingEntry.bool("Fast Math", true))
                .add(SettingEntry.bool("Limit Particles", true)));
        CATEGORIES.put("PERFORMANCE", perf);
    }

    public static ModuleEntry getModule(String name) {
        for (List<ModuleEntry> list : CATEGORIES.values()) {
            for (ModuleEntry m : list) {
                if (m.name.equalsIgnoreCase(name)) return m;
            }
        }
        return null;
    }

    public ClickGuiScreen() {
        super(Text.literal("SHADOW CLIENT GUI"));
        initModules();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Smooth dark glassmorphism backdrop
        context.fill(0, 0, this.width, this.height, 0x95080810);

        // Top Header Bar
        context.drawTextWithShadow(this.textRenderer,
                "SHADOW CLIENT v1.0 [1.21.11]  |  Left-Click: Toggle   Right-Click: Config Settings   [R_SHIFT]: Close",
                16, 10, 0xFF00F5D4);

        int panelX = 16;
        int panelY = 28;
        int panelWidth = 148;

        for (Map.Entry<String, List<ModuleEntry>> cat : CATEGORIES.entrySet()) {
            // Category Header
            context.fill(panelX, panelY, panelX + panelWidth, panelY + 18, 0xFF7B2CBF);
            context.fill(panelX, panelY + 17, panelX + panelWidth, panelY + 18, 0xFF00F5D4);
            context.drawTextWithShadow(this.textRenderer, cat.getKey(), panelX + 6, panelY + 5, 0xFFFFFFFF);

            int curY = panelY + 18;
            for (ModuleEntry mod : cat.getValue()) {
                boolean hovered = mouseX >= panelX && mouseX <= panelX + panelWidth
                        && mouseY >= curY && mouseY < curY + 16;

                int bg = hovered ? 0xE8222236 : 0xD8141422;
                context.fill(panelX, curY, panelX + panelWidth, curY + 16, bg);

                if (mod.enabled) {
                    context.fill(panelX, curY, panelX + 3, curY + 16, 0xFF00F5D4);
                }

                String keyTag = (listeningForKey == mod) ? "[...]" : (mod.keyName.equals("NONE") ? "" : " [" + mod.keyName + "]");
                int textColor = mod.enabled ? 0xFF00F5D4 : 0xFFBBBBBB;
                context.drawTextWithShadow(this.textRenderer, mod.name + keyTag, panelX + 7, curY + 4, textColor);
                context.drawTextWithShadow(this.textRenderer, mod.expanded ? "-" : "+", panelX + panelWidth - 10, curY + 4, 0xFF888888);
                curY += 16;

                if (mod.expanded) {
                    for (SettingEntry s : mod.settings) {
                        context.fill(panelX, curY, panelX + panelWidth, curY + 15, 0xE00E0E18);
                        context.fill(panelX + 3, curY, panelX + 4, curY + 15, 0xFF7B2CBF);

                        if (s.type.equals("BOOL")) {
                            String label = s.name + ": " + (s.boolVal ? "ON" : "OFF");
                            context.drawTextWithShadow(this.textRenderer, label, panelX + 8, curY + 3, s.boolVal ? 0xFF00F5D4 : 0xFF888888);
                        } else if (s.type.equals("MODE")) {
                            String label = s.name + ": " + s.modes[s.modeIndex];
                            context.drawTextWithShadow(this.textRenderer, label, panelX + 8, curY + 3, 0xFFD0A8FF);
                        } else if (s.type.equals("SLIDER")) {
                            double pct = (s.numVal - s.minVal) / (s.maxVal - s.minVal);
                            int fillW = (int) ((panelWidth - 10) * pct);
                            context.fill(panelX + 6, curY + 11, panelX + 6 + fillW, curY + 14, 0xFF7B2CBF);
                            String label = String.format("%s: %.2f", s.name, s.numVal);
                            context.drawTextWithShadow(this.textRenderer, label, panelX + 8, curY + 2, 0xFFEEEEEE);
                        }
                        curY += 15;
                    }
                    // Keybind row inside expanded settings
                    context.fill(panelX, curY, panelX + panelWidth, curY + 15, 0xE00E0E18);
                    String bindLabel = (listeningForKey == mod) ? "Press any key (ESC clears)..." : "Keybind: [" + mod.keyName + "]";
                    context.drawTextWithShadow(this.textRenderer, bindLabel, panelX + 8, curY + 3, 0xFFFFCC00);
                    curY += 15;
                }
            }
            panelX += panelWidth + 10;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int panelX = 16;
        int panelY = 28;
        int panelWidth = 148;

        for (Map.Entry<String, List<ModuleEntry>> cat : CATEGORIES.entrySet()) {
            int curY = panelY + 18;
            for (ModuleEntry mod : cat.getValue()) {
                if (mouseX >= panelX && mouseX <= panelX + panelWidth && mouseY >= curY && mouseY < curY + 16) {
                    if (button == 0) {
                        mod.enabled = !mod.enabled;
                    } else if (button == 1) {
                        mod.expanded = !mod.expanded;
                    }
                    return true;
                }
                curY += 16;

                if (mod.expanded) {
                    for (SettingEntry s : mod.settings) {
                        if (mouseX >= panelX && mouseX <= panelX + panelWidth && mouseY >= curY && mouseY < curY + 15) {
                            if (s.type.equals("BOOL")) {
                                s.boolVal = !s.boolVal;
                            } else if (s.type.equals("MODE")) {
                                s.modeIndex = (s.modeIndex + 1) % s.modes.length;
                            } else if (s.type.equals("SLIDER")) {
                                double ratio = Math.max(0.0, Math.min(1.0, (mouseX - (panelX + 6)) / (double) (panelWidth - 12)));
                                s.numVal = s.minVal + ratio * (s.maxVal - s.minVal);
                            }
                            return true;
                        }
                        curY += 15;
                    }
                    // Keybind row click
                    if (mouseX >= panelX && mouseX <= panelX + panelWidth && mouseY >= curY && mouseY < curY + 15) {
                        listeningForKey = (listeningForKey == mod) ? null : mod;
                        return true;
                    }
                    curY += 15;
                }
            }
            panelX += panelWidth + 10;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (listeningForKey != null) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_DELETE) {
                listeningForKey.keyCode = GLFW.GLFW_KEY_UNKNOWN;
                listeningForKey.keyName = "NONE";
            } else {
                listeningForKey.keyCode = keyCode;
                String name = GLFW.glfwGetKeyName(keyCode, scanCode);
                listeningForKey.keyName = (name != null) ? name.toUpperCase() : ("KEY_" + keyCode);
            }
            listeningForKey = null;
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
