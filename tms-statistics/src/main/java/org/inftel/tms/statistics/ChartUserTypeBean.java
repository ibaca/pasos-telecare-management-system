/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.statistics;

import java.io.Serializable;
import javax.ejb.Stateless;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author agumpg
 */
@Stateless
public class ChartUserTypeBean implements Serializable {
 
    private PieChartModel pieModel;  
  
    public void ChartBean() {  
        createPieModel();  
    }  
  
    public PieChartModel getPieModel() {  
        return pieModel;  
    }  
  
    private void createPieModel() {  
        pieModel = new PieChartModel();  
  
         
        pieModel.set("Mayor de 65 años", 45);  
        pieModel.set("Discapacidad Física", 7);  
        pieModel.set("Discapacidad Psíquica", 1);
        pieModel.set("Discapacidad Sensorial",2);
        pieModel.set("Enfermos crónicos", 4);
        pieModel.set("Violencia de Género", 17);
        pieModel.set("Otros", 24); 
    }  
        
}

