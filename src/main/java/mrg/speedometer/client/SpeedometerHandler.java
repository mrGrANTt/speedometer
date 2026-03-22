package mrg.speedometer.client;

import mrg.speedometer.Speedometer;
import mrg.speedometer.util.ConfigValues;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;

public class SpeedometerHandler {
    public static SpeedometerHandler INSTANCE;
    public static Identifier FRAME = Identifier.of(Speedometer.MOD_ID, "/textures/gui/frame.png");


    public static void init() {
        INSTANCE = new SpeedometerHandler();
    }

    private double speed;
    private double count;

    private long lastNanoTime;
    private Vec3d lastPos;

    public SpeedometerHandler() {
        speed = 0;
        count = 0;

        lastNanoTime = System.nanoTime();
        lastPos = Vec3d.ZERO;

        HudRenderCallback.EVENT.register(this::Handler);
        ClientTickEvents.START_CLIENT_TICK.register(this::setSpeed);
    }

    private int countSplitColors(int c1, int c2, int count, int splitCount) {
        return c1 + ((c2 - c1) / splitCount) * count;
    }

    private int countColors(int c1, int c2, int count, int splitCount) {
        return countSplitColors(ColorHelper.getRed(c1), ColorHelper.getRed(c2), count, splitCount) * 0x10000
                + countSplitColors(ColorHelper.getGreen(c1), ColorHelper.getGreen(c2), count, splitCount) * 0x100
                + countSplitColors(ColorHelper.getBlue(c1), ColorHelper.getBlue(c2), count, splitCount);
    }

    private int countColorWithSpeed(int speed) {
        int color = ConfigValues.INSTANCE.color;

        if(speed >= 7 && speed < 30)
            color = countColors(ConfigValues.INSTANCE.color, ConfigValues.INSTANCE.color1, speed - 7, 27);
        else if(speed >= 30 && speed < 80)
            color = countColors(ConfigValues.INSTANCE.color1, ConfigValues.INSTANCE.color2, speed - 30, 49);
        else if(speed >= 80)
            color = ConfigValues.INSTANCE.color2;

        return color + 0xFF000000;
    }

    private void Handler(DrawContext dc, RenderTickCounter rtc) {
        if (ConfigValues.INSTANCE.enabled && !MinecraftClient.getInstance().options.hudHidden) {
            int speed = (int) Math.round(this.speed);
            int speedSize = speed == 0 ? 1 : (int) Math.log10(speed) + 1;
            int speedXSize = (int) ((6 * speedSize - 1) * (ConfigValues.INSTANCE.scale * 1.5f));
            int speedYSize = (int) (7 * (ConfigValues.INSTANCE.scale * 1.5f));
            int TextureXSize = (int) (107 * (ConfigValues.INSTANCE.scale * 0.5f));
            int TextureYSize = (int) (42 * (ConfigValues.INSTANCE.scale * 0.5f));

            int textureXSize = (int) (107 * (ConfigValues.INSTANCE.scale * 0.5f));
            int textureYSize = (int) (42 * (ConfigValues.INSTANCE.scale * 0.5f));
            int color = countColorWithSpeed(speed);

            int x = (ConfigValues.INSTANCE.x + (textureXSize - speedXSize) / 2),
                    y = (ConfigValues.INSTANCE.y + (textureYSize - speedYSize) / 2);

            MatrixStack ms = dc.getMatrices();
            ms.push();
            ms.scale((ConfigValues.INSTANCE.scale * 1.5f), (ConfigValues.INSTANCE.scale * 1.5f), 1f);
            dc.drawText(MinecraftClient.getInstance().textRenderer, "" + speed,
                    (int) (x / (ConfigValues.INSTANCE.scale * 1.5f)), (int) (y / (ConfigValues.INSTANCE.scale * 1.5f)), color, false);
            ms.pop();

            ms.push();
            ms.scale((ConfigValues.INSTANCE.scale * 0.75f), (ConfigValues.INSTANCE.scale * 0.75f), 1f);
            dc.drawText(MinecraftClient.getInstance().textRenderer, "m|s",
                    (int) ((x + speedXSize + 2) / (ConfigValues.INSTANCE.scale * 0.75f)),
                    (int) ((y + speedYSize) / (ConfigValues.INSTANCE.scale * 0.75f)) - 7,
                    color, false);
            ms.pop();

            dc.drawTexture(RenderLayer::getGuiTextured, FRAME, ConfigValues.INSTANCE.x, ConfigValues.INSTANCE.y,
                    0, 0, TextureXSize, TextureYSize,
                    TextureXSize, TextureYSize, color);
        }
    }

    public void setSpeed(MinecraftClient mc) {
        if (ConfigValues.INSTANCE.enabled && !MinecraftClient.getInstance().isPaused()) {
            if(count++ >= ConfigValues.INSTANCE.dilay) {
                ClientPlayerEntity cpe = mc.player;
                if (cpe != null) {
                    long now = System.nanoTime();
                    double deltaTime = (now - lastNanoTime) / 1000000000d;

                    if (lastNanoTime != 0) {
                        Vec3d pos = cpe.getPos();
                        speed = lastPos.distanceTo(pos) / deltaTime;
                        lastPos = pos;
                    }

                    lastNanoTime = now;
                }
                count = 0;
            }
        }
    }
}