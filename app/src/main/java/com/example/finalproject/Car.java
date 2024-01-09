package com.example.finalproject;

import androidx.annotation.NonNull;

public class Car {
    private String type;
    private int ID;

    public Car(String type, int ID) {
        this.type = type;
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public int getID() {
        return ID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @NonNull
    @Override
    public String toString() {
        return "Car{" +
                "type='" + type + '\'' +
                ", ID=" + ID +
                '}';
    }
}
