package mrg.speedometer.client;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import mrg.speedometer.util.ConfigScreenBuilder;
import mrg.speedometer.util.ConfigValues;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class KeyBindsHandler {
    public static KeyBindsHandler INSTANCE;
    public static final KeyMapping.Category CATEGORY = new KeyMapping.Category(Identifier.parse("speedometer:button.menu_title"));
    public static void init() {
        INSTANCE = new KeyBindsHandler();
    }

    private final KeyMapping openMenu;
    private final KeyMapping toggleHUD;

    private KeyBindsHandler() {
        openMenu = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "speedometer:button.open_menu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_BRACKET,
                CATEGORY
        ));
        toggleHUD = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "speedometer:button.toggle_hud",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_BRACKET,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register((v) -> {
            if (Minecraft.getInstance().isRunning()) {
                while (openMenu.consumeClick()) {
                    Minecraft.getInstance().setScreen(ConfigScreenBuilder
                            .getScreen(Minecraft.getInstance().screen));
                }
                while (toggleHUD.consumeClick()) {
                    ConfigValues.INSTANCE.setEnabled(!ConfigValues.INSTANCE.isEnabled());
                    ConfigHolder<?> data = AutoConfig.getConfigHolder(ConfigValues.class);
                    data.save();
                }
            }
        });
    }
}