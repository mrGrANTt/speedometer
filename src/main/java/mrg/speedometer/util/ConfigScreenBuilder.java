package mrg.speedometer.util;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
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
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.enabled = newValue)
                .setYesNoTextSupplier(b -> Text.translatable(b ? "speedometer:config.enabled" : "speedometer:config.disabled"))
                .build());
        general.addEntry(entryBuilder.startColorField(Text.translatable("speedometer:config.general.color"), ConfigValues.INSTANCE.color)
                .setDefaultValue(0xFFFFFF)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.color = newValue)
                .setTooltip(Text.translatable("speedometer:config.general.color.desr"))
                .build());
        general.addEntry(entryBuilder.startColorField(Text.translatable("speedometer:config.general.color1"), ConfigValues.INSTANCE.color1)
                .setDefaultValue(0xFF9500)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.color1 = newValue)
                .setTooltip(Text.translatable("speedometer:config.general.color1.desr"))
                .build());
        general.addEntry(entryBuilder.startColorField(Text.translatable("speedometer:config.general.color2"), ConfigValues.INSTANCE.color2)
                .setDefaultValue(0xFF2F00)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.color2 = newValue)
                .setTooltip(Text.translatable("speedometer:config.general.color2.desr"))
                .build());
        general.addEntry(entryBuilder.startIntField(Text.translatable("speedometer:config.general.x"), ConfigValues.INSTANCE.x)
                .setDefaultValue(10)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.x = newValue)
                .build());
        general.addEntry(entryBuilder.startIntField(Text.translatable("speedometer:config.general.y"), ConfigValues.INSTANCE.y)
                .setDefaultValue(15)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.y = newValue)
                .build());
        general.addEntry(entryBuilder.startFloatField(Text.translatable("speedometer:config.general.scale"), ConfigValues.INSTANCE.scale)
                .setDefaultValue(1)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.scale = newValue)
                .build());
        general.addEntry(entryBuilder.startIntSlider(Text.translatable("speedometer:config.general.dilay"), ConfigValues.INSTANCE.dilay, 1, 20)
                .setDefaultValue(10)
                .setSaveConsumer(newValue -> ConfigValues.INSTANCE.dilay = newValue)
                .setTooltip(Text.translatable("speedometer:config.general.dilay.desr"))
                .build());
    }
}
