package com.franz.sud.cartridge.castlevania.service;

import com.franz.sud.engine.components.geography.Room;

import java.util.ArrayList;

public class GameMapProgress {
    ArrayList<ArrayList<Room>> roomsOpened = new ArrayList<>();

    public GameMapProgress() {
    }

    public void addRoomsOpened(ArrayList<Room> rooms) {
        roomsOpened.add(rooms);
    }

    public ArrayList<Room> getRoomsOpened(int index) {
        return roomsOpened.get(index);
    }

    public int roomsOpenedSize() {
        return roomsOpened.size();
    }
}