package mrg.speedometer.util;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import mrg.speedometer.Speedometer;

@Config(name = Speedometer.MOD_ID)
public class ConfigValues implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public static ConfigValues INSTANCE;

    public static void init() {
        AutoConfig.register(ConfigValues.class, GsonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ConfigValues.class).getConfig();
    }

    public boolean enabled = true;

    public int dilay = 10;
    public int x = 10;
    public int y = 15;
    public float scale = 1;
    public int color = 0xFFFFFF;
    public int color1 = 0xFF9500;
    public int color2 = 0xFF2F00;
}