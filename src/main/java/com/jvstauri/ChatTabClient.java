package com.jvstauri;

import net.fabricmc.api.ModInitializer;

public class BasicModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        System.out.println("Skyblock ChatTab initialized!");
    }
}
