package mrg.speedometr;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {

    private Screen lastScreen = null;

    protected ConfigScreen() {
        super(Text.translatable("config.title"));
    }

    @Override
    protected void init() {
        ButtonWidget enabled = ButtonWidget.builder(Text.translatable(ConfigValues.enabled ? "config.enabled" : "config.disabled"), (btn) -> {
                    ConfigValues.enabled = !ConfigValues.enabled;
                    btn.setMessage(Text.translatable(ConfigValues.enabled ? "config.enabled" : "config.disabled"));
                }).dimensions(this.width - 140, 20, 120, 20)
                .build();
        ButtonWidget closeBtn = ButtonWidget.builder(Text.translatable("config.close"), (btn) -> {
                    close();
                }).dimensions(this.width - 140, this.height - 40, 120, 20)
                .build();
        TextWidget enabledText = new TextWidget(20, 20,120, 20, Text.translatable("config.set_enabled"), textRenderer);

        addDrawableChild(enabledText);
        addDrawableChild(enabled);
        addDrawableChild(closeBtn);
    }

    public void setLastScreen (Screen screen) {
        lastScreen = screen;
    }

    @Override
    public void close() {
        if (client != null)
            client.setScreen(lastScreen);
    }
}