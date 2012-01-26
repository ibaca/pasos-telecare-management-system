/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.web.jspbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.inftel.tms.domain.Alert;
import org.inftel.tms.services.AlertFacadeRemote;

/**
 *
 * @author inftel
 */
public class TableBean implements Serializable {
    @EJB
   static private AlertFacadeRemote alertFacade;
      
//    private final static Logger logger = Logger.getLogger(TableBean.class.getName());  
//      
//    private final static String[] colors;  
//      
//    private final static String[] manufacturers;  
//      
//    static {  
//        colors = new String[10];  
//        colors[0] = "Black";  
//        colors[1] = "White";  
//        colors[2] = "Green";  
//        colors[3] = "Red";  
//        colors[4] = "Blue";  
//        colors[5] = "Orange";  
//        colors[6] = "Silver";  
//        colors[7] = "Yellow";  
//        colors[8] = "Brown";  
//        colors[9] = "Maroon";  
//          
//        manufacturers = new String[10];  
//        manufacturers[0] = "Mercedes";  
//        manufacturers[1] = "BMW";  
//        manufacturers[2] = "Volvo";  
//        manufacturers[3] = "Audi";  
//        manufacturers[4] = "Renault";  
//        manufacturers[5] = "Opel";  
//        manufacturers[6] = "Volkswagen";  
//        manufacturers[7] = "Chrysler";  
//        manufacturers[8] = "Ferrari";  
//        manufacturers[9] = "Ford";  
//    }  
  
    private List<Alert> alerts;  
      
    private Alert selectedAlert;  
  
    public TableBean() {
        alerts=alertFacade.findActiveAlerts();
    }  
    
//    private void populateRandomCars(List<Car> list, int size) {  
//        for(int i = 0 ; i < size ; i++)  
//            list.add(new Car(getRandomModel(), getRandomYear(), getRandomManufacturer(), getRandomColor()));  
//    }  
  
//    private int getRandomYear() {  
//        return (int) (Math.random() * 50 + 1960);  
//    }  
//      
//    private String getRandomColor() {  
//        return colors[(int) (Math.random() * 10)];  
//    }  
//      
//    private String getRandomManufacturer() {  
//        return manufacturers[(int) (Math.random() * 10)];  
//    }  
//      
//    private String getRandomModel() {  
//        return UUID.randomUUID().toString().substring(0, 8);  
//    }    

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public Alert getSelectedAlert() {
        return selectedAlert;
    }

    public void setSelectedAlert(Alert selectedAlert) {
        this.selectedAlert = selectedAlert;
    }
    
//    public static void main(String[] args){
//        List<Alert> talerts = alertFacade.findActiveAlerts();
//        for(Alert i : talerts){
//            System.out.println(i.getAffected().getFirstName());
//        }
//    }
} 
