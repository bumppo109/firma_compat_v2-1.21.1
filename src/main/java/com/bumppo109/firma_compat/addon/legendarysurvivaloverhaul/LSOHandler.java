package com.bumppo109.firma_compat.addon.legendarysurvivaloverhaul;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry;



public class LSOHandler {


    private static final Logger LOGGER =
            LoggerFactory.getLogger(LSOHandler.class);



    public static void registerModifiers() {

        try {

            TemperatureModifierRegistry.MODIFIERS.register(
                    "firmalife_greenhouse",
                    FirmalifeGreenhouseModifier::new
            );



            LOGGER.info(
                    "Registered Firmalife and TFC LSO temperature modifiers"
            );


        } catch(Exception e) {

            LOGGER.error(
                    "Failed registering LSO modifiers",
                    e
            );

        }

    }

}