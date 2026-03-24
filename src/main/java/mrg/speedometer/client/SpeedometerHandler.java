package mrg.speedometer.client;

import mrg.speedometer.Speedometer;
import mrg.speedometer.util.ConfigValues;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.font.TextRenderer;
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
            int color = countColorWithSpeed(speed);

            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
            String speedText = String.valueOf(speed);
            String metricsText = "m|s";

            float scale = ConfigValues.INSTANCE.scale;
            float scaleSpeed = scale * 1.5f;
            float scaleMetrics = scale * 0.5f;
            int textureWight = 54;
            int textureHeight = 21;

            int fontHeight = 7;
            int startX = ConfigValues.INSTANCE.x;
            int startY = ConfigValues.INSTANCE.y;
            float speedX = startX + (textureWight*scale - getWidth(speedText)*scaleSpeed)/2f;
            float speedY = startY + (textureHeight*scale - fontHeight*scaleSpeed)/2f;
            float metricsX = speedX + getWidth(speedText)*scaleSpeed + 1;
            float metricsY = speedY + fontHeight*scaleSpeed - fontHeight*scaleMetrics;


            MatrixStack ms = dc.getMatrices();

            ms.push();
            ms.scale(scale,scale,1);
            ms.translate(startX/scale, startY/scale, 1);
            dc.drawTexture(RenderLayer::getGuiTextured, FRAME, 0, 0, 0, 0, textureWight, textureHeight, textureWight, textureHeight, color);
            ms.pop();

            ms.push();
            ms.scale(scaleSpeed,scaleSpeed,1);
            ms.translate(speedX/scaleSpeed, speedY/scaleSpeed, 1);
            dc.drawText(renderer, speedText, 0, 0, color - 0x1000000, false);
            ms.pop();

            ms.push();
            ms.scale(scaleMetrics,scaleMetrics,1);
            ms.translate(metricsX/scaleMetrics, metricsY/scaleMetrics, 1);
            dc.drawText(renderer, metricsText, 0, 0,color - 0x1000000, false);
            ms.pop();
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

    private int getWidth(String str) {
        return str.length()*6-1;
    }
}