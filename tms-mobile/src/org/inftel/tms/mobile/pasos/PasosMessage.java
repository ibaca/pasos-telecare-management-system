
package org.inftel.tms.mobile.pasos;

import java.util.Calendar;

public class PasosMessage {
    private String type;
    private int temperature;
    private int batteryLevel;
    private String longitude;
    private String latitude;
    private boolean charging;
    private String date;
    private String time;

    /**
     * Nueva instancia mensaje PaSOS con datos Data y Time inicializados.
     */
    public PasosMessage() {
        initializeDateTime();
    }

    /**
     * User Alarm constructor
     * 
     * @param latitude
     * @param longitude
     */
    public static PasosMessage userAlarm(String latitude, String longitude) {
        PasosMessage message = new PasosMessage();
        message.type = PasosMessageType.USER_ALARM;
        message.latitude = latitude;
        message.longitude = longitude;
        return message;
    }

    /**
     * Device Alarm Hight Temp constructor
     * 
     * @param temperature
     * @param latitude
     * @param longitude
     */
    public static PasosMessage deviceAlarmHighTemp(int temperature, String latitude,
            String longitude) {
        PasosMessage message = new PasosMessage();
        message.type = PasosMessageType.DEVICE_ALARM_HIGHTEMP;
        message.temperature = temperature;
        message.latitude = latitude;
        message.longitude = longitude;
        return message;
    }

    /**
     * Device Alarm Low Temp constructor
     * 
     * @param temperature
     * @param latitude
     * @param longitude
     */
    public static PasosMessage deviceAlarmLowTemp(int temperature, String latitude, String longitude) {
        PasosMessage message = new PasosMessage();
        message.type = PasosMessageType.DEVICE_ALARM_LOWTEMP;
        message.temperature = temperature;
        message.latitude = latitude;
        message.longitude = longitude;
        return message;
    }

    /**
     * Technical Alarm constructor
     * 
     * @param batteryLevel
     * @param latitude
     * @param longitude
     * @param charging
     */
    public static PasosMessage technicalAlarm(int batteryLevel, String latitude,
            String longitude,
            boolean charging) {
        PasosMessage message = new PasosMessage();
        message.type = PasosMessageType.TECHNICAL_ALARM;
        message.batteryLevel = batteryLevel;
        message.latitude = latitude;
        message.longitude = longitude;
        message.charging = charging;
        return message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public boolean isCharging() {
        return charging;
    }

    public void setCharging(boolean charging) {
        this.charging = charging;
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

    /**
     * Set Date and Time to actual, in PASOS format
     */
    public void initializeDateTime() {
        Calendar calendar = Calendar.getInstance();

        String dayString = String.format("%td", calendar);
        String monthString = String.format("%tm", calendar);
        String hourString = String.format("%tH", calendar);
        String minsString = String.format("%tM", calendar);
        String secsString = String.format("%tS", calendar);
        String yearString = String.valueOf(calendar.get(Calendar.YEAR));

        this.date = "&LD" + yearString + monthString + dayString;
        this.time = "&LH" + hourString + minsString + secsString;
    }

    @Override
    public String toString() {
        String charger = "&PC000";
        if (charging)
            charger = "&PC999";

        if (type.equals(PasosMessageType.USER_ALARM)) {
            return type + date + time + "&LN" + longitude + "&LT" + latitude + "#";
        }
        else if (type.equals(PasosMessageType.DEVICE_ALARM_LOWTEMP)) {
            return type + date + time + longitude + "&LT" + latitude + "&DT" + temperature + "#";
        }
        else if (type.equals(PasosMessageType.DEVICE_ALARM_HIGHTEMP)) {
            return type + date + time + longitude + "&LT" + latitude + "&DT" + temperature + "#";
        }
        // otherwise is technical alarm
        else {
            return type + date + time + longitude + "&LT" + latitude + "&PB" + batteryLevel
                    + charger + "#";
        }
    }
}
