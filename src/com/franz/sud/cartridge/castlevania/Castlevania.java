package com.franz.sud.cartridge.castlevania;

import com.franz.sud.cartridge.Cartridge;
import com.franz.sud.cartridge.castlevania.service.GameMapProgress;
import com.franz.sud.cartridge.castlevania.service.InventoryService;
import com.franz.sud.engine.components.geography.GameMap;
import com.franz.sud.engine.components.geography.Room;
import com.franz.sud.engine.components.narrative.GameNarrative;
import com.franz.sud.engine.components.unit.Unit;

public class Castlevania implements Cartridge {
    private CastlevaniaInitializer initializer = new CastlevaniaInitializer();
    private boolean isFinished;
    private GameNarrative gameNarrative;
    private Unit finalBoss;
    private Room secondLocation;
    private GameMap map;
    private GameMapProgress gameMapProgress;
    private Unit hero;
    private InventoryService inventoryService;
    private String name;

    public Castlevania() {
        isFinished = false;
        name = "Castlevania";
    }

    @Override
    public void init() {
        initializer.initialize();
        gameNarrative = initializer.getGameNarrative();
        finalBoss = initializer.getFinalBoss();
        secondLocation = initializer.getSecondLocation();
        map = initializer.getGameMap();
        gameMapProgress = initializer.getGameMapProgress();
        hero = initializer.getHero();
        inventoryService = initializer.getInventoryService();
    }

    @Override
    public void start() {

    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public String getName() {
        return name;
    }
}
