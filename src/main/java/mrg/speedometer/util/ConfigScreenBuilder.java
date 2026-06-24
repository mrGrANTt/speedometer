package mrg.speedometer.util;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreenBuilder {
    public static Screen getScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle(Component.translatable("speedometer:config.title"))
                .setSavingRunnable(() -> AutoConfig.getConfigHolder(ConfigValues.class).save());

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("speedometer:config.category.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        generateButtons(general, entryBuilder);

        return builder.setParentScreen(parent).build();
    }

    private static void generateButtons(ConfigCategory general, ConfigEntryBuilder entryBuilder) {
        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("speedometer:config.general.set_enabled"), ConfigValues.INSTANCE.isEnabled())
                .setDefaultValue(ConfigValues.DEF_ENABLED)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setEnabled(newValue))
                .setYesNoTextSupplier(b -> Component.translatable(b ? "speedometer:config.enabled" : "speedometer:config.disabled"))
                .build());
        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("speedometer:config.general.set_enabled_speedometer"), ConfigValues.INSTANCE.isEnabledSpeedometer())
                .setDefaultValue(ConfigValues.DEF_ENABLED_SPEEDOMETER)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setEnabledSpeedometer(newValue))
                .setYesNoTextSupplier(b -> Component.translatable(b ? "speedometer:config.enabled" : "speedometer:config.disabled"))
                .build());
        general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("speedometer:config.general.set_enabled_angle"), ConfigValues.INSTANCE.isEnabledAngle())
                .setDefaultValue(ConfigValues.DEF_ENABLED_ANGLE)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setEnabledAngle(newValue))
                .setYesNoTextSupplier(b -> Component.translatable(b ? "speedometer:config.enabled" : "speedometer:config.disabled"))
                .build());

        SubCategoryBuilder color = entryBuilder.startSubCategory(Component.translatable("speedometer:config.category.color"));
        color.setExpanded(true);

        color.add(entryBuilder.startColorField(Component.translatable("speedometer:config.general.color"), ConfigValues.INSTANCE.getColor())
                .setDefaultValue(ConfigValues.DEF_COLOR)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setColor(newValue))
                .setTooltip(Component.translatable("speedometer:config.general.color.desr"))
                .build());
        color.add(entryBuilder.startColorField(Component.translatable("speedometer:config.general.color1"), ConfigValues.INSTANCE.getColor1())
                .setDefaultValue(ConfigValues.DEF_COLOR_1)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setColor1(newValue))
                .setTooltip(Component.translatable("speedometer:config.general.color1.desr"))
                .build());
        color.add(entryBuilder.startColorField(Component.translatable("speedometer:config.general.color2"), ConfigValues.INSTANCE.getColor2())
                .setDefaultValue(ConfigValues.DEF_COLOR_2)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setColor2(newValue))
                .setTooltip(Component.translatable("speedometer:config.general.color2.desr"))
                .build());

        general.addEntry(color.build());
        SubCategoryBuilder shown = entryBuilder.startSubCategory(Component.translatable("speedometer:config.category.shown"));
        shown.setExpanded(true);

        shown.add(entryBuilder.startFloatField(Component.translatable("speedometer:config.general.scale"), ConfigValues.INSTANCE.getScale())
                .setDefaultValue(ConfigValues.DEF_SCALE)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setScale(newValue))
                .build());
        shown.add(entryBuilder.startIntSlider(Component.translatable("speedometer:config.general.dilay"), ConfigValues.INSTANCE.getDilay(), 1, 20)
                .setDefaultValue(ConfigValues.DEF_DILAY)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setDilay(newValue))
                .setTooltip(Component.translatable("speedometer:config.general.dilay.desr"))
                .build());
        shown.add(entryBuilder.startEnumSelector(Component.translatable("speedometer:config.general.speed_relate"), UIRelateTypes.class, ConfigValues.INSTANCE.getSpeedRelate())
                .setDefaultValue(ConfigValues.DEF_SPEED_RELATE)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setSpeedRelate(newValue))
                .build());
        shown.add(entryBuilder.startIntField(Component.translatable("speedometer:config.general.speed_x"), ConfigValues.INSTANCE.getSpeedX())
                .setDefaultValue(ConfigValues.DEF_SPEED_X)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setSpeedX(newValue))
                .build());
        shown.add(entryBuilder.startIntField(Component.translatable("speedometer:config.general.speed_y"), ConfigValues.INSTANCE.getSpeedY())
                .setDefaultValue(ConfigValues.DEF_SPEED_Y)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setSpeedY(newValue))
                .build());
        shown.add(entryBuilder.startEnumSelector(Component.translatable("speedometer:config.general.yaw_relate"), UIRelateTypes.class, ConfigValues.INSTANCE.getYawRelate())
                .setDefaultValue(ConfigValues.DEF_YAW_RELATE)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setYawRelate(newValue))
                .build());
        shown.add(entryBuilder.startIntField(Component.translatable("speedometer:config.general.yaw_x"), ConfigValues.INSTANCE.getYawX())
                .setDefaultValue(ConfigValues.DEF_YAW_X)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setYawX(newValue))
                .build());
        shown.add(entryBuilder.startIntField(Component.translatable("speedometer:config.general.yaw_y"), ConfigValues.INSTANCE.getYawY())
                .setDefaultValue(ConfigValues.DEF_YAW_Y)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setYawY(newValue))
                .build());
        shown.add(entryBuilder.startEnumSelector(Component.translatable("speedometer:config.general.pitch_relate"), UIRelateTypes.class ,ConfigValues.INSTANCE.getPitchRelate())
                .setDefaultValue(ConfigValues.DEF_PITCH_RELATE)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setPitchRelate(newValue))
                .build());
        shown.add(entryBuilder.startIntField(Component.translatable("speedometer:config.general.pitch_x"), ConfigValues.INSTANCE.getPitchX())
                .setDefaultValue(ConfigValues.DEF_PITCH_X)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setPitchX(newValue))
                .build());
        shown.add(entryBuilder.startIntField(Component.translatable("speedometer:config.general.pitch_y"), ConfigValues.INSTANCE.getPitchY())
                .setDefaultValue(ConfigValues.DEF_PITCH_Y)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.setPitchY(newValue))
                .build());

        general.addEntry(shown.build());
    }
}