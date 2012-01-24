/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.web.jspbean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Administrador
 */
public class CarBean {

private List<Car> cars;

    public CarBean() {
        cars = new ArrayList<Car>();
        cars.add(new Car("myModel",2005,"ManufacturerX","blue"));
    }
    public List<Car> getCars() {
        return cars;
    }
}
