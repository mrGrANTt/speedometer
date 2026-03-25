package mrg.speedometer.util;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import mrg.speedometer.Speedometer;
import net.minecraft.client.MinecraftClient;

@Config(name = Speedometer.MOD_ID)
public class ConfigValues implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public static ConfigValues INSTANCE;

    public static final boolean ENABLED = true;
    public static boolean ENABLED_SPEEDOMETER = true;
    public static boolean ENABLED_ANGLE = true;
    public static final int DILAY = 5;
    public static final int SPEED_X = 10;
    public static final int SPEED_Y = 15;
    public static final int YAW_X = 10;
    public static final int YAW_Y = 40;
    public static final int PITCH_X = 10;
    public static final int PITCH_Y = 50;
    public static final float SCALE = 1;
    public static final int COLOR = 0xFFFFFF;
    public static final int COLOR_1 = 0xFF9500;
    public static final int COLOR_2 = 0xFF2F00;

    public static void init() {
        AutoConfig.register(ConfigValues.class, GsonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ConfigValues.class).getConfig();
    }

    public ConfigValues() {
         this.enabled = ENABLED;
         this.enabledSpeedometer = ENABLED_SPEEDOMETER;
         this.enabledAngle = ENABLED_ANGLE;
         this.dilay = DILAY;
         this.speedX = SPEED_X;
         this.speedY = SPEED_Y;
         this.yawX = YAW_X;
         this.yawY = YAW_Y;
         this.pitchX = PITCH_X;
         this.pitchY = PITCH_Y;
         this.scale = SCALE;
         this.color = COLOR;
         this.color1 = COLOR_1;
         this.color2 = COLOR_2;
    }

    public boolean enabled;
    public boolean enabledSpeedometer;
    public boolean enabledAngle;
    public int dilay;
    public int speedX;
    public int speedY;
    public int yawX;
    public int yawY;
    public int pitchX;
    public int pitchY;
    public float scale;
    public int color;
    public int color1;
    public int color2;
}
