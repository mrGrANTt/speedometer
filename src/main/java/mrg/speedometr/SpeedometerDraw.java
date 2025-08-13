 package mrg.speedometr;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

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

    public SpeedometerDraw() {
        speed = 0;
        count = 0;

        dilay = 10;
        x = 10;
        y = 15;
        numScale = 1.5f;
        textureScale = 0.5f;
        metricScale = 0.75f;
        color = 0xFFFFFF;
        shadow = false;
        texture = Identifier.of(Speedometr.MOD_ID, "/textures/gui/frame.png");

        HudRenderCallback.EVENT.register(this::Handler);
        ClientTickEvents.START_CLIENT_TICK.register(this::setSpeed);
    }

    private void Handler(DrawContext dc, RenderTickCounter rtc) {
        if (ConfigValues.enabled) {
            //Speedometr.LOGGER.info("Counting");
            int speed = (int) Math.ceil(this.speed * 20);
            int speedSize = speed == 0 ? 1 : (int) Math.log10(speed) + 1;
            int speedXSize = (int) ((6 * speedSize - 1) * numScale);
            int speedYSize = (int) (7 * numScale);
            int TextureXSize = (int) (107 * textureScale);
            int TextureYSize = (int) (42 * textureScale);

            int x = (this.x + (TextureXSize - speedXSize) / 2),
                    y = (this.y + (TextureYSize - speedYSize) / 2);


            //Speedometr.LOGGER.info("MatrixStack");
            MatrixStack ms = dc.getMatrices();
            ms.push();
            ms.scale(numScale, numScale, 1f);
            dc.drawText(MinecraftClient.getInstance().textRenderer, "" + speed,
                    (int) (x / numScale), (int) (y / numScale), color, shadow);
            //dc.drawVerticalLine(45,0,100,0xFF00FF00);
            ms.pop();

            ms.push();
            ms.scale(metricScale, metricScale, 1f);
            dc.drawText(MinecraftClient.getInstance().textRenderer, "m|s",
                    (int) ((x + speedXSize + 2) / metricScale), (int) ((y + speedYSize) / metricScale) - 7, color, shadow);
            //dc.drawVerticalLine(15,0,100,0xFF00FF00);
            ms.pop();

            //Speedometr.LOGGER.info("draw Texture");
            dc.drawTexture(RenderLayer::getGuiTextured, texture, this.x, this.y,
                    0, 0, TextureXSize, TextureYSize,
                    TextureXSize, TextureYSize);

            //dc.drawVerticalLine(90,0,100,0xFF00FF00);
        }
    }

    public void setSpeed(MinecraftClient mc) {
        if (ConfigValues.enabled) {
            ClientPlayerEntity cpe = mc.player;
            if (cpe != null && count == 0) {
                //Speedometr.LOGGER.info("new speed");
                speed = (cpe.isOnGround() ? cpe.getVelocity().getHorizontal() : cpe.getVelocity()).distanceTo(Vec3d.ZERO);
            }
            count++;
            if(count >= dilay) count = 0;
        }
    }
}
