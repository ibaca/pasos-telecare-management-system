/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.tms.simulator.model;

import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author migueqm
 */
public class Parameters {
  private String key = "";
  private String call = "";
  private String sms = "";
  private String id = "";
  private String transport = "";
  private String ip = "";
  private String date="";
  private String time="";
  private String URLservlet="";
  private String senderMobileNumber="";
  private String temperature;
  private String battery;

  public String getURLservlet() {
        return URLservlet;
    }

  public void setURLservlet(String URLservlet) {
        this.URLservlet = URLservlet;
    }

  public String getBattery() {
        return battery;
    }

  public void setBattery(String battery) {
        this.battery = StringUtils.leftPad(battery, 2, '0');
    }

  public String getSenderMobileNumber() {
        return senderMobileNumber;
    }

  public void setSenderMobileNumber(String senderMobileNumber) {
        this.senderMobileNumber = senderMobileNumber;
    }

  public String getTemperature() {
        return temperature;
    }

  public void setTemperature(String temperature) {
        this.temperature = StringUtils.leftPad(temperature, 2, '0');
    }
  
  
  public Parameters() {
                              
    }

    public void setDateandTime(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minutes=calendar.get(Calendar.MINUTE);
        int seconds=calendar.get(Calendar.SECOND);
        String yearString="";
        String dayString="";
        String monthString="";
        String hourString="";
        String minsString="";
        String secsString="";
        
        yearString = String.valueOf(year);
        if(day<10)      dayString = String.format("%02d",day);
            else        dayString = String.valueOf(day);
        
        if(month<10)    monthString = String.format("%02d",month);
            else        monthString = String.valueOf(month);
        
        if(hour<10)     hourString = String.format("%02d",hour);
            else        hourString = String.valueOf(hour);
        
        if(minutes<10)  minsString = String.format("%02d",minutes);
            else        minsString =  String.valueOf(minutes);
        if(seconds<10)  secsString = String.format("%02d",seconds);
            else        secsString =  String.valueOf(seconds);
                
        this.date = "&LD" + yearString + monthString + dayString;
        this.time = "&LH" + hourString + minsString + secsString;
    }
    
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    @Override
    public String toString() {
        return "Parameters{" + "key=" + key + ", call=" + call + ", sms=" + sms + ", id=" + id + ", transport=" + transport + ", ip=" + ip + ", date=" + date + ", time=" + time + ", URLservlet=" + URLservlet + ", senderMobileNumber=" + senderMobileNumber + ", temperature=" + temperature + ", battery=" + battery + '}';
    }

    


}
