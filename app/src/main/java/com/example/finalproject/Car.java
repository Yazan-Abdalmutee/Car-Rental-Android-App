package com.example.finalproject;

public class Car {

    private int ID;
    private String type;
    private String factoryName;

    public Car(int ID, String factoryName, String type) {
        this.ID = ID;
        this.factoryName = factoryName;
        this.type = type;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public int getID() {
        return ID;
    }

    public String getType() {
        return type;
    }

    public String getFactoryName() {
        return factoryName;
    }

    @Override
    public String toString() {
        return "Car{" +
                "ID=" + ID +
                ", type='" + type + '\'' +
                ", factoryName='" + factoryName + '\'' +
                '}';
    }
}