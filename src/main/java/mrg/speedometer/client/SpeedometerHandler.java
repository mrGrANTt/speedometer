package mrg.speedometer.client;

import mrg.speedometer.Speedometer;
import mrg.speedometer.util.ConfigValues;
import mrg.speedometer.util.UIRelateTypes;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3x2fStack;

public class SpeedometerHandler {
    public static SpeedometerHandler INSTANCE;
    public static final Identifier FRAME = Identifier.fromNamespaceAndPath(Speedometer.MOD_ID, "/textures/gui/frame.png");

    public static void init() {
        INSTANCE = new SpeedometerHandler();
    }

    private double speed;
    private double count;

    private long lastNanoTime;
    private Vec3 lastPos;

    public SpeedometerHandler() {
        speed = 0;
        count = 0;

        lastNanoTime = System.nanoTime();
        lastPos = Vec3.ZERO;

        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(Speedometer.MOD_ID, "speedometer"), this::Handler);
        ClientTickEvents.START_CLIENT_TICK.register(this::setSpeed);
    }

    private int countSplitColors(int c1, int c2, int count, int splitCount) {
        return c1 + ((c2 - c1) / splitCount) * count;
    }

    private int countColors(int c1, int c2, int count, int splitCount) {

        return countSplitColors(ARGB.red(c1), ARGB.red(c2), count, splitCount) * 0x10000
                + countSplitColors(ARGB.green(c1), ARGB.green(c2), count, splitCount) * 0x100
                + countSplitColors(ARGB.blue(c1), ARGB.blue(c2), count, splitCount);
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

    private void Handler(GuiGraphicsExtractor dc, DeltaTracker rtc) {
        if (ConfigValues.INSTANCE.isEnabled() && !Minecraft.getInstance().options.hideGui) {
            int speed = (int) Math.round(this.speed);
            int color = countColorWithSpeed(speed);
            Font renderer = Minecraft.getInstance().font;
            Matrix3x2fStack ms = dc.pose();

            if (ConfigValues.INSTANCE.isEnabledSpeedometer())
                renderSpeedometer(renderer, ms, dc, color, speed);

            if (ConfigValues.INSTANCE.isEnabledAngle())
                renderAngle(renderer, ms, dc, color);
        }
    }

    private void renderSpeedometer(Font renderer, Matrix3x2fStack ms, GuiGraphicsExtractor dc, int color, int speed) {
        int fontHeight = 7;
        String speedText = String.valueOf(speed);
        String metricsText = "m|s";

        float scale = ConfigValues.INSTANCE.getScale();
        float scaleSpeed = scale * 1.5f;
        float scaleMetrics = scale * 0.5f;
        int textureWight = 54;
        int textureHeight = 21;
        UIRelateTypes relateType =  ConfigValues.INSTANCE.getSpeedRelate();

        float startX = ConfigValues.INSTANCE.getSpeedX() + relateType.getHorizontal()*(dc.guiWidth()-textureWight * scale)/2f;
        float startY = ConfigValues.INSTANCE.getSpeedY() + relateType.getVertical()*(dc.guiHeight()-textureHeight * scale)/2f;
        float speedX = startX + (textureWight * scale - getWidth(speedText) * scaleSpeed) / 2f;
        float speedY = startY + (textureHeight * scale - fontHeight * scaleSpeed) / 2f;
        float metricsX = speedX + getWidth(speedText) * scaleSpeed + 1;
        float metricsY = speedY + fontHeight * scaleSpeed - fontHeight * scaleMetrics;

        ms.pushMatrix().scale(scale).translate(startX / scale, startY / scale);
        dc.blit(RenderPipelines.GUI_TEXTURED, FRAME, 0, 0, 0, 0, textureWight, textureHeight, textureWight, textureHeight, color);
        ms.popMatrix();

        ms.pushMatrix().scale(scaleSpeed).translate(speedX / scaleSpeed, speedY / scaleSpeed);
        dc.text(renderer, speedText, 0, 0, color - 0x1000000, false);
        ms.popMatrix();

        ms.pushMatrix().scale(scaleMetrics).translate(metricsX / scaleMetrics, metricsY / scaleMetrics);
        dc.text(renderer, metricsText, 0, 0, color - 0x1000000, false);
        ms.popMatrix();
    }

    private void renderAngle(Font renderer, Matrix3x2fStack ms, GuiGraphicsExtractor dc, int color) {
        LocalPlayer plr = Minecraft.getInstance().player;
        int fontHeight = 7;
        float yaw = 0;
        float pitch = 0;
        UIRelateTypes yawRelateType =  ConfigValues.INSTANCE.getYawRelate();
        UIRelateTypes pitchRelateType =  ConfigValues.INSTANCE.getPitchRelate();

        if (plr != null) {
            yaw = plr.getYHeadRot() % 360;
            if (yaw < 0) yaw += 360;
            yaw = Math.round(yaw * 10) / 10f;
            pitch = Math.round(plr.getXRot() * 10) / 10f;
        }

        String yawText = String.valueOf(yaw);
        String pitchText = String.valueOf(pitch);



        float scale = ConfigValues.INSTANCE.getScale();
        float yawX = ConfigValues.INSTANCE.getYawX() + yawRelateType.getHorizontal()*(dc.guiWidth()-(getWidth(yawText)-4)*scale)/2f;
        float yawY = ConfigValues.INSTANCE.getYawY() + yawRelateType.getVertical()*(dc.guiHeight()-fontHeight*scale)/2f;
        float pitchX = ConfigValues.INSTANCE.getPitchX() + pitchRelateType.getHorizontal()*(dc.guiWidth()-(getWidth(pitchText)-4)*scale)/2f;
        float pitchY = ConfigValues.INSTANCE.getPitchY() + pitchRelateType.getVertical()*(dc.guiHeight()-fontHeight*scale)/2f;

        ms.pushMatrix().scale(scale).translate(yawX / scale, yawY / scale);
        dc.text(renderer, yawText, 0, 0, color, false);
        ms.popMatrix();

        ms.pushMatrix().scale(scale).translate(pitchX / scale, pitchY / scale);
        dc.text(renderer, pitchText, 0, 0, color, false);
        ms.popMatrix();
    }

    public void setSpeed(Minecraft mc) {
        if (ConfigValues.INSTANCE.isEnabled() && ConfigValues.INSTANCE.isEnabledSpeedometer() && !Minecraft.getInstance().isPaused()) {
            if(count++ >= ConfigValues.INSTANCE.getDilay()) {
                LocalPlayer cpe = mc.player;
                if (cpe != null) {
                    long now = System.nanoTime();
                    double deltaTime = (now - lastNanoTime) / 1000000000d;

                    if (lastNanoTime != 0) {
                        Vec3 pos = cpe.position();
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