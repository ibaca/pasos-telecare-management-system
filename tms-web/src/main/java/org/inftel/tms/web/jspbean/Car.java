/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.web.jspbean;

/**
 *
 * @author Administrador
 */
public class Car {
    private String model;
    private int year;
    private String Manufacturer;
    private String color;
    public Car(){}
    public Car(String model, int year, String Manufacturer, String color) {
        this.model = model;
        this.year = year;
        this.Manufacturer = Manufacturer;
        this.color = color;
    }
    
    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String Manufacturer) {
        this.Manufacturer = Manufacturer;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }   
}
