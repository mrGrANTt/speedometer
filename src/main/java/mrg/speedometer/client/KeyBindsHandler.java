package mrg.speedometer.client;

import me.shedaniel.autoconfig.AutoConfig;
import mrg.speedometer.util.ConfigScreenBuilder;
import mrg.speedometer.util.ConfigValues;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyBindsHandler {
    public static KeyBindsHandler INSTANCE;
    public static void init() {
        INSTANCE = new KeyBindsHandler();
    }

    private final KeyBinding openMenu;
    private final KeyBinding toggleHUD;

    private KeyBindsHandler() {
        openMenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "speedometer:button.open_menu",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_BRACKET,
                "speedometer:button.menu_title"
        ));
        toggleHUD = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "speedometer:button.toggle_hud",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_BRACKET,
                "speedometer:button.menu_title"
        ));

        ClientTickEvents.END_CLIENT_TICK.register((v) -> {
            if (MinecraftClient.getInstance().isRunning()) {
                while (openMenu.wasPressed()) {
                    MinecraftClient.getInstance().setScreen(ConfigScreenBuilder
                            .getScreen(MinecraftClient.getInstance().currentScreen));
                }
                while (toggleHUD.wasPressed()) {
                    ConfigValues.INSTANCE.enabled = !ConfigValues.INSTANCE.enabled;
                    AutoConfig.getConfigHolder(ConfigValues.class).save();
                }
            }
        });
    }
}
