package mrg.speedometer.client;

import mrg.speedometer.Speedometer;
import mrg.speedometer.util.ConfigValues;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.util.ColorCode;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3x2fStack;

public class SpeedometerHandler {
    public static SpeedometerHandler INSTANCE;

    public static void init() {
        INSTANCE = new SpeedometerHandler();
    }

    private final Identifier texture;

    private double speed;
    private double count;

    private long lastNanoTime;
    private Vec3d lastPos;

    public SpeedometerHandler() {
        speed = 0;
        count = 0;

        texture = Identifier.of(Speedometer.MOD_ID, "/textures/gui/frame.png");

        lastNanoTime = System.nanoTime();
        lastPos = Vec3d.ZERO;

        HudElementRegistry.addLast(Identifier.of(Speedometer.MOD_ID, "speedometer"), this::Handler);
        ClientTickEvents.START_CLIENT_TICK.register(this::setSpeed);
    }

    private void Handler(DrawContext dc, RenderTickCounter rtc) {
        if (ConfigValues.INSTANCE.enabled && !MinecraftClient.getInstance().options.hudHidden) {
            int speed = (int) Math.round(this.speed);
            int speedSize = speed == 0 ? 1 : (int) Math.log10(speed) + 1;
            int speedXSize = (int) ((6 * speedSize - 1) * (ConfigValues.INSTANCE.scale * 1.5f));
            int speedYSize = (int) (7 * (ConfigValues.INSTANCE.scale * 1.5f));
            int TextureXSize = (int) (107 * (ConfigValues.INSTANCE.scale * 0.5f));
            int TextureYSize = (int) (42 * (ConfigValues.INSTANCE.scale * 0.5f));

            int x = (ConfigValues.INSTANCE.x + (TextureXSize - speedXSize) / 2),
                    y = (ConfigValues.INSTANCE.y + (TextureYSize - speedYSize) / 2);


            Matrix3x2fStack ms = dc.getMatrices();

            ms.pushMatrix();
            ms.scale(ConfigValues.INSTANCE.scale * 1.5f, ConfigValues.INSTANCE.scale * 1.5f);
            dc.drawText(MinecraftClient.getInstance().textRenderer, "" + speed,
                    (int) (x / (ConfigValues.INSTANCE.scale * 1.5f)), (int) (y / (ConfigValues.INSTANCE.scale * 1.5f)), ConfigValues.INSTANCE.color - 0x1000000, false);
            ms.popMatrix();

            ms.pushMatrix();
            ms.scale((ConfigValues.INSTANCE.scale * 0.75f), (ConfigValues.INSTANCE.scale * 0.75f));
            dc.drawText(MinecraftClient.getInstance().textRenderer, "m|s",
                    (int) ((x + speedXSize + 2) / (ConfigValues.INSTANCE.scale * 0.75f)),
                    (int) ((y + speedYSize) / (ConfigValues.INSTANCE.scale * 0.75f)) - 7,
                    ConfigValues.INSTANCE.color - 0x1000000, false);
            ms.popMatrix();

            dc.drawTexture(RenderPipelines.GUI_TEXTURED, texture, ConfigValues.INSTANCE.x, ConfigValues.INSTANCE.y,
                    0, 0, TextureXSize, TextureYSize,
                    TextureXSize, TextureYSize);
        }
    }

    public void setSpeed(MinecraftClient mc) {
        if (ConfigValues.INSTANCE.enabled) {
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