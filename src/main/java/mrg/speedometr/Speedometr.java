package mrg.speedometr;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Speedometr implements ModInitializer {
	public static final String MOD_ID = "speedometr";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private SpeedometerDraw speedometerDraw;

	@Override
	public void onInitialize() {

		speedometerDraw = new SpeedometerDraw();
		LOGGER.info("Hello Fabric world!");
	}
}