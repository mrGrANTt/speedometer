 package mrg.speedometr;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3x2fStack;

 public class SpeedometerDraw {
    private double dilay;
    private int x;
    private int y;
    private float numScale;
    private float textureScale;
    private float metricScale;
    private int color;
    private boolean shadow;

    private Identifier texture;

    private double speed;
    private double count;

    private long lastNanoTime;
    private Vec3d lastPos;

    public SpeedometerDraw() {
        speed = 0;
        count = 0;

        dilay = 10;
        x = 10;
        y = 15;
        numScale = 1.5f;
        textureScale = 0.5f;
        metricScale = 0.75f;
        color = 0xFFFFFFFF;
        shadow = false;
        texture = Identifier.of(Speedometr.MOD_ID, "/textures/gui/frame.png");

        HudElementRegistry.addLast(Identifier.of(Speedometr.MOD_ID, "speedomert"), this::Handler);

        lastNanoTime = System.nanoTime();
        lastPos = Vec3d.ZERO;
        ClientTickEvents.START_CLIENT_TICK.register(this::setSpeed);
    }

    private void Handler(DrawContext dc, RenderTickCounter rtc) {
        if (ConfigValues.enabled) {
            int speed = (int) Math.round(this.speed);
            int speedSize = speed == 0 ? 1 : (int) Math.log10(speed) + 1;
            int speedXSize = (int) ((6 * speedSize - 1) * numScale);
            int speedYSize = (int) (7 * numScale);
            int TextureXSize = (int) (107 * textureScale);
            int TextureYSize = (int) (42 * textureScale);

            int x = (this.x + (TextureXSize - speedXSize) / 2),
                    y = (this.y + (TextureYSize - speedYSize) / 2);


            Matrix3x2fStack ms = dc.getMatrices();

            ms.pushMatrix();
            ms.scale(numScale, numScale);
            dc.drawText(MinecraftClient.getInstance().textRenderer, "" + speed,
                    (int) (x / numScale), (int) (y / numScale), color, shadow);
            ms.popMatrix();

            ms.pushMatrix();
            ms.scale(metricScale, metricScale);
            dc.drawText(MinecraftClient.getInstance().textRenderer, "m|s",
                    (int) ((x + speedXSize + 2) / metricScale), (int) ((y + speedYSize) / metricScale) - 7, color, shadow);
            ms.popMatrix();

            dc.drawTexture(RenderPipelines.GUI_TEXTURED, texture, this.x, this.y,
                    0, 0, TextureXSize, TextureYSize,
                    TextureXSize, TextureYSize);
        }
    }

    public void setSpeed(MinecraftClient mc) {
        if (ConfigValues.enabled) {
            if(count++ >= dilay) {
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
