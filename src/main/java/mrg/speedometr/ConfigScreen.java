package mrg.speedometr;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    private Screen lastScreen = null;

    public void setLastScreen (Screen screen) {
        lastScreen = screen;
    }

    protected ConfigScreen() {
        super(Text.of("Speedometer config"));
    }

    @Override
    public void close() {
        if (client != null)
            client.setScreen(lastScreen);
    }
}
