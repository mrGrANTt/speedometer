package mrg.speedometer.client;

import com.mojang.blaze3d.systems.RenderSystem;
import mrg.speedometer.Speedometer;
import mrg.speedometer.util.ConfigValues;
import mrg.speedometer.util.UIRelateTypes;
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
        return countSplitColors(ColorHelper.Argb.getRed(c1), ColorHelper.Argb.getRed(c2), count, splitCount) * 0x10000
                + countSplitColors(ColorHelper.Argb.getGreen(c1), ColorHelper.Argb.getGreen(c2), count, splitCount) * 0x100
                + countSplitColors(ColorHelper.Argb.getBlue(c1), ColorHelper.Argb.getBlue(c2), count, splitCount);
    }

    private int countColorWithSpeed(int speed) {
        int color = ConfigValues.INSTANCE.getColor();

        if(speed >= 7 && speed < 30)
            color = countColors(ConfigValues.INSTANCE.getColor(), ConfigValues.INSTANCE.getColor1(), speed - 7, 27);
        else if(speed >= 30 && speed < 80)
            color = countColors(ConfigValues.INSTANCE.getColor1(), ConfigValues.INSTANCE.getColor2(), speed - 30, 49);
        else if(speed >= 80)
            color = ConfigValues.INSTANCE.getColor2();

        return color + 0xFF000000;
    }

    private void Handler(DrawContext dc, RenderTickCounter rtc) {
        if (ConfigValues.INSTANCE.isEnabled() && !MinecraftClient.getInstance().options.hudHidden) {
            int speed = (int) Math.round(this.speed);
            int color = countColorWithSpeed(speed);
            TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
            MatrixStack ms = dc.getMatrices();

            if (ConfigValues.INSTANCE.isEnabledSpeedometer())
                renderSpeedometer(renderer, ms, dc, color, speed);

            if (ConfigValues.INSTANCE.isEnabledAngle())
                renderAngle(renderer, ms, dc, color);
        }
    }

    private void renderSpeedometer(TextRenderer renderer, MatrixStack ms, DrawContext dc, int color, int speed) {
        int fontHeight = 7;
        String speedText = String.valueOf(speed);
        String metricsText = "m|s";

        float scale = ConfigValues.INSTANCE.getScale();
        float scaleSpeed = scale * 1.5f;
        float scaleMetrics = scale * 0.5f;
        int textureWight = 54;
        int textureHeight = 21;
        UIRelateTypes relateType =  ConfigValues.INSTANCE.getSpeedRelate();

        float startX = ConfigValues.INSTANCE.getSpeedX() + relateType.getHorizontal()*(dc.getScaledWindowWidth()-textureWight * scale)/2f;
        float startY = ConfigValues.INSTANCE.getSpeedY() + relateType.getVertical()*(dc.getScaledWindowHeight()-textureHeight * scale)/2f;
        float speedX = startX + (textureWight * scale - getWidth(speedText) * scaleSpeed) / 2f;
        float speedY = startY + (textureHeight * scale - fontHeight * scaleSpeed) / 2f;
        float metricsX = speedX + getWidth(speedText) * scaleSpeed + 1;
        float metricsY = speedY + fontHeight * scaleSpeed - fontHeight * scaleMetrics;

        RenderSystem.setShaderColor( (float) ColorHelper.Argb.getRed(color) /255,
                (float) ColorHelper.Argb.getGreen(color) /255,
                (float) ColorHelper.Argb.getBlue(color) /255,
                (float) ColorHelper.Argb.fullAlpha(color) /255);

        ms.push();
        ms.scale(scale, scale, 1);
        ms.translate(startX/scale, startY/scale, 1);
        dc.drawTexture(FRAME, 0, 0, 0, 0, textureWight, textureHeight, textureWight, textureHeight);
        ms.pop();

        RenderSystem.setShaderColor(1,1,1,1);

        ms.push();
        ms.scale(scaleSpeed, scaleSpeed, 1);
        ms.translate(speedX/scaleSpeed, speedY/scaleSpeed, 1);
        dc.drawText(renderer, speedText, 0, 0, color - 0x1000000, false);
        ms.pop();

        ms.push();
        ms.scale(scaleMetrics, scaleMetrics, 1);
        ms.translate(metricsX/scaleMetrics, metricsY/scaleMetrics, 1);
        dc.drawText(renderer, metricsText, 0, 0,color - 0x1000000, false);
        ms.pop();
    }

    private void renderAngle(TextRenderer renderer, MatrixStack ms, DrawContext dc, int color) {
        ClientPlayerEntity plr = MinecraftClient.getInstance().player;
        int fontHeight = 7;
        float yaw = 0;
        float pitch = 0;
        UIRelateTypes yawRelateType =  ConfigValues.INSTANCE.getYawRelate();
        UIRelateTypes pitchRelateType =  ConfigValues.INSTANCE.getPitchRelate();

        if (plr != null) {
            yaw = plr.headYaw % 360;
            if (yaw < 0) yaw += 360;
            yaw = Math.round(yaw * 10) / 10f;
            pitch = Math.round(plr.getPitch() * 10) / 10f;
        }

        String yawText = String.valueOf(yaw);
        String pitchText = String.valueOf(pitch);



        float scale = ConfigValues.INSTANCE.getScale();
        float yawX = ConfigValues.INSTANCE.getYawX() + yawRelateType.getHorizontal()*(dc.getScaledWindowWidth()-(getWidth(yawText)-4)*scale)/2f;
        float yawY = ConfigValues.INSTANCE.getYawY() + yawRelateType.getVertical()*(dc.getScaledWindowHeight()-fontHeight*scale)/2f;
        float pitchX = ConfigValues.INSTANCE.getPitchX() + pitchRelateType.getHorizontal()*(dc.getScaledWindowWidth()-(getWidth(pitchText)-4)*scale)/2f;
        float pitchY = ConfigValues.INSTANCE.getPitchY() + pitchRelateType.getVertical()*(dc.getScaledWindowHeight()-fontHeight*scale)/2f;

        ms.push();
        ms.scale(scale, scale, 1);
        ms.translate(yawX / scale, yawY / scale, 1);
        dc.drawText(renderer, yawText, 0, 0, color - 0x1000000, false);
        ms.pop();

        ms.push();
        ms.scale(scale, scale, 1);
        ms.translate(pitchX / scale, pitchY / scale, 1);
        dc.drawText(renderer, pitchText, 0, 0, color - 0x1000000, false);
        ms.pop();
    }

    public void setSpeed(MinecraftClient mc) {
        if (ConfigValues.INSTANCE.isEnabled() && ConfigValues.INSTANCE.isEnabledSpeedometer() && !MinecraftClient.getInstance().isPaused()) {
            if(count++ >= ConfigValues.INSTANCE.getDilay()) {
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