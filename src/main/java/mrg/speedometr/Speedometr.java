package mrg.speedometr;

import net.fabricmc.api.ModInitializer;

import net.minecraft.client.gui.screen.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Speedometr implements ModInitializer {
	public static final String MOD_ID = "speedometr";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private SpeedometerDraw speedometerDraw;
	private static final ConfigScreen cnfScreen = new ConfigScreen();

	public static Screen getCnfScreen(Screen screen) {
		cnfScreen.setLastScreen(screen);
		return cnfScreen;
	}

	@Override
	public void onInitialize() {

		speedometerDraw = new SpeedometerDraw();
		LOGGER.info("Hello Fabric world!");
	}
}