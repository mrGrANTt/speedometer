package mrg.speedometer.util;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreenBuilder {
    public static Screen getScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(Text.translatable("speedometer:config.title"))
                .setSavingRunnable(() -> AutoConfig.getConfigHolder(ConfigValues.class).save());

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("speedometer:config.category.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        generateButtons(general, entryBuilder);

        return builder.setParentScreen(parent).build();
    }

    private static void generateButtons(ConfigCategory general, ConfigEntryBuilder entryBuilder) {
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("speedometer:config.general.set_enabled"), ConfigValues.INSTANCE.enabled)
                .setDefaultValue(ConfigValues.ENABLED)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.enabled = newValue)
                .setYesNoTextSupplier(b -> Text.translatable(b ? "speedometer:config.enabled" : "speedometer:config.disabled"))
                .build());
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("speedometer:config.general.set_enabled_speedometer"), ConfigValues.INSTANCE.enabledSpeedometer)
                .setDefaultValue(ConfigValues.ENABLED_SPEEDOMETER)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.enabledSpeedometer = newValue)
                .setYesNoTextSupplier(b -> Text.translatable(b ? "speedometer:config.enabled" : "speedometer:config.disabled"))
                .build());
        general.addEntry(entryBuilder.startBooleanToggle(Text.translatable("speedometer:config.general.set_enabled_angle"), ConfigValues.INSTANCE.enabledAngle)
                .setDefaultValue(ConfigValues.ENABLED_ANGLE)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.enabledAngle = newValue)
                .setYesNoTextSupplier(b -> Text.translatable(b ? "speedometer:config.enabled" : "speedometer:config.disabled"))
                .build());

        SubCategoryBuilder color = entryBuilder.startSubCategory(Text.translatable("speedometer:config.category.color"));
        color.setExpanded(true);

        color.add(entryBuilder.startColorField(Text.translatable("speedometer:config.general.color"), ConfigValues.INSTANCE.color)
                .setDefaultValue(ConfigValues.COLOR)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.color = newValue)
                .setTooltip(Text.translatable("speedometer:config.general.color.desr"))
                .build());
        color.add(entryBuilder.startColorField(Text.translatable("speedometer:config.general.color1"), ConfigValues.INSTANCE.color1)
                .setDefaultValue(ConfigValues.COLOR_1)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.color1 = newValue)
                .setTooltip(Text.translatable("speedometer:config.general.color1.desr"))
                .build());
        color.add(entryBuilder.startColorField(Text.translatable("speedometer:config.general.color2"), ConfigValues.INSTANCE.color2)
                .setDefaultValue(ConfigValues.COLOR_2)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.color2 = newValue)
                .setTooltip(Text.translatable("speedometer:config.general.color2.desr"))
                .build());

        general.addEntry(color.build());
        SubCategoryBuilder shown = entryBuilder.startSubCategory(Text.translatable("speedometer:config.category.shown"));
        shown.setExpanded(true);

        shown.add(entryBuilder.startFloatField(Text.translatable("speedometer:config.general.scale"), ConfigValues.INSTANCE.scale)
                .setDefaultValue(ConfigValues.SCALE)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.scale = newValue)
                .build());
        shown.add(entryBuilder.startIntSlider(Text.translatable("speedometer:config.general.dilay"), ConfigValues.INSTANCE.dilay, 1, 20)
                .setDefaultValue(ConfigValues.DILAY)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.dilay = newValue)
                .setTooltip(Text.translatable("speedometer:config.general.dilay.desr"))
                .build());
        shown.add(entryBuilder.startIntField(Text.translatable("speedometer:config.general.speed_x"), ConfigValues.INSTANCE.speedX)
                .setDefaultValue(ConfigValues.SPEED_X)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.speedX = newValue)
                .build());
        shown.add(entryBuilder.startIntField(Text.translatable("speedometer:config.general.speed_y"), ConfigValues.INSTANCE.speedY)
                .setDefaultValue(ConfigValues.SPEED_Y)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.speedY = newValue)
                .build());
        shown.add(entryBuilder.startIntField(Text.translatable("speedometer:config.general.yaw_x"), ConfigValues.INSTANCE.yawX)
                .setDefaultValue(ConfigValues.YAW_X)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.yawX = newValue)
                .build());
        shown.add(entryBuilder.startIntField(Text.translatable("speedometer:config.general.yaw_y"), ConfigValues.INSTANCE.yawY)
                .setDefaultValue(ConfigValues.YAW_Y)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.yawY = newValue)
                .build());
        shown.add(entryBuilder.startIntField(Text.translatable("speedometer:config.general.pitch_x"), ConfigValues.INSTANCE.pitchX)
                .setDefaultValue(ConfigValues.PITCH_X)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.pitchX = newValue)
                .build());
        shown.add(entryBuilder.startIntField(Text.translatable("speedometer:config.general.pitch_y"), ConfigValues.INSTANCE.pitchY)
                .setDefaultValue(ConfigValues.PITCH_Y)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.pitchY = newValue)
                .build());

        general.addEntry(shown.build());
    }
}