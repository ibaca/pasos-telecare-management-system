
package org.inftel.tms.mobile.pasos;

import java.util.Calendar;

public class PasosMessage {
    private Type type;
    private int temperature;
    private int batteryLevel;
    private String longitude;
    private String latitude;
    private boolean charging;
    private String date;
    private String time;

    // Generic constructor
    public PasosMessage(Type type) {
        super();
        setDateandTime();
        this.type = type;
    }

    // User Alarm constructor
    public PasosMessage(Type type, String latitude, String longitude) {
        super();
        this.type = type;
        setDateandTime();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Device Alarm constructor
    public PasosMessage(Type type, int temperature, String latitude, String longitude) {
        super();
        this.type = type;
        setDateandTime();
        this.temperature = temperature;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Technical Alarm constructor
    public PasosMessage(Type type, int batteryLevel, String latitude, String longitude,
            boolean charging) {
        super();
        this.type = type;
        setDateandTime();
        this.batteryLevel = batteryLevel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.charging = charging;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
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
    public void setDateandTime() {
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

        if (type.equals(Type.USER_ALARM)) {
            return type + date + time + "&LN" + longitude + "&LT" + latitude + "#";
        }
        else if (type.equals(Type.DEVICE_ALARM_LOWTEMP)) {
            return type + date + time + longitude + "&LT" + latitude + "&DT" + temperature + "#";
        }
        else if (type.equals(Type.DEVICE_ALARM_HIGHTEMP)) {
            return type + date + time + longitude + "&LT" + latitude + "&DT" + temperature + "#";
        }
        // otherwise is technical alarm
        else {
            return type + date + time + longitude + "&LT" + latitude + "&PB" + batteryLevel
                    + charger + "#";
        }
    }
}
