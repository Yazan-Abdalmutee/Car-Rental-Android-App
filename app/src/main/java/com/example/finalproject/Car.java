package com.example.finalproject;

//make,model,drive,fueltype,year,vclass,createdon,modifiedon

//https://public.opendatasoft.com/api/explore/v2.1/catalog/datasets/all-vehicles-model/records?select=make%2Cmodel%2Cdrive%2Cfueltype%2Cyear%2Cvclass%2Ccreatedon%2Cmodifiedon&limit=10

public class Car {

    private int ID;
    private String type;
    private String factoryName;
    private double price;
    private int productionYear;
    private String color;
    private String fuelType;

    public Car(int ID, String type, String factoryName, double price, int productionYear, String color, String fuelType) {
        this.ID = ID;
        this.type = type;
        this.factoryName = factoryName;
        this.price = price;
        this.productionYear = productionYear;
        this.color = color;
        this.fuelType = fuelType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
}