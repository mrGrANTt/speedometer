package mrg.speedometer;

import mrg.speedometer.client.KeyBindsHandler;
import mrg.speedometer.client.SpeedometerHandler;
import mrg.speedometer.util.ConfigValues;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Speedometer implements ModInitializer {
    public static final String MOD_ID = "speedometer";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        SpeedometerHandler.init();
        ConfigValues.init();
        KeyBindsHandler.init();
        LOGGER.info("Hello Fabric world!");
    }
}

/*TODO Added:
* Config
* Redirect
* Lang fix
* Button to menu and toggle btn
*/