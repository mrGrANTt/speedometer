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

    public static final boolean DEF_ENABLED = true;
    public static boolean DEF_ENABLED_SPEEDOMETER = true;
    public static boolean DEF_ENABLED_ANGLE = true;
    public static final int DEF_DILAY = 5;
    public static final UIRelateTypes DEF_SPEED_RELATE = UIRelateTypes.LEFT_UP;
    public static final int DEF_SPEED_X = 10;
    public static final int DEF_SPEED_Y = 15;
    public static final UIRelateTypes DEF_YAW_RELATE = UIRelateTypes.LEFT_UP;
    public static final int DEF_YAW_X = 10;
    public static final int DEF_YAW_Y = 40;
    public static final UIRelateTypes DEF_PITCH_RELATE = UIRelateTypes.LEFT_UP;
    public static final int DEF_PITCH_X = 10;
    public static final int DEF_PITCH_Y = 50;
    public static final float DEF_SCALE = 1;
    public static final int DEF_COLOR = 0xFFFFFF;
    public static final int DEF_COLOR_1 = 0xFF9500;
    public static final int DEF_COLOR_2 = 0xFF2F00;

    public static void init() {
        AutoConfig.register(ConfigValues.class, GsonConfigSerializer::new);
        INSTANCE = AutoConfig.getConfigHolder(ConfigValues.class).getConfig();
    }

    public ConfigValues() {
        this.enabled = DEF_ENABLED;
        this.enabledSpeedometer = DEF_ENABLED_SPEEDOMETER;
        this.enabledAngle = DEF_ENABLED_ANGLE;
        this.dilay = DEF_DILAY;
        this.speedRelate = DEF_SPEED_RELATE;
        this.speedX = DEF_SPEED_X;
        this.speedY = DEF_SPEED_Y;
        this.yawRelate = DEF_YAW_RELATE;
        this.yawX = DEF_YAW_X;
        this.yawY = DEF_YAW_Y;
        this.pitchX = DEF_PITCH_X;
        this.pitchRelate = DEF_PITCH_RELATE;
        this.pitchY = DEF_PITCH_Y;
        this.scale = DEF_SCALE;
        this.color = DEF_COLOR;
        this.color1 = DEF_COLOR_1;
        this.color2 = DEF_COLOR_2;
    }

    private boolean enabled;
    private boolean enabledSpeedometer;
    private boolean enabledAngle;
    private int dilay;
    private UIRelateTypes speedRelate;
    private int speedX;
    private int speedY;
    private UIRelateTypes yawRelate;
    private int yawX;
    private int yawY;
    private UIRelateTypes pitchRelate;
    private int pitchX;
    private int pitchY;
    private float scale;
    private int color;
    private int color1;
    private int color2;

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean value) { enabled = value; }

    public boolean isEnabledSpeedometer() { return enabledSpeedometer; }
    public void setEnabledSpeedometer(boolean value) { enabledSpeedometer = value; }

    public boolean isEnabledAngle() { return enabledAngle; }
    public void setEnabledAngle(boolean value) { enabledAngle = value; }

    public int getDilay() { return dilay; }
    public void setDilay(int value) { dilay = value; }

    public float getScale() { return scale; }
    public void setScale(float value) { scale = value; }

    public int getColor() { return color; }
    public void setColor(int value) { color = value; }

    public int getColor1() { return color1; }
    public void setColor1(int value) { color1 = value; }

    public int getColor2() { return color2; }
    public void setColor2(int value) { color2 = value; }

    public UIRelateTypes getPitchRelate() { return pitchRelate; }
    public void setPitchRelate(UIRelateTypes value) { pitchRelate = value; }

    public int getPitchX() { return pitchX; }
    public void setPitchX(int value) { pitchX = value; }

    public int getPitchY() { return pitchY; }
    public void setPitchY(int value) { pitchY = value; }

    public UIRelateTypes getSpeedRelate() { return speedRelate; }
    public void setSpeedRelate(UIRelateTypes value) { speedRelate = value; }

    public int getSpeedX() { return speedX; }
    public void setSpeedX(int value) { speedX = value; }

    public int getSpeedY() { return speedY; }
    public void setSpeedY(int value) { speedY = value; }

    public UIRelateTypes getYawRelate() { return yawRelate; }
    public void setYawRelate(UIRelateTypes value) { yawRelate = value; }

    public int getYawX() { return yawX; }
    public void setYawX(int value) { yawX = value; }

    public int getYawY() { return yawY; }
    public void setYawY(int value) { yawY = value; }
}