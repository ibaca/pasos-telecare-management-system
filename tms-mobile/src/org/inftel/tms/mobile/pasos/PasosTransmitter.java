package org.inftel.tms.mobile.pasos;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

public interface PasosTransmitter {
	
	public HttpResponse sendTechnicalAlarm(String location, String batteryLevel, boolean charging) throws URISyntaxException, UnsupportedEncodingException, IOException;
	
	public HttpResponse sendDeviceAlarm(String location, String temp) throws URISyntaxException, UnsupportedEncodingException, IOException;
	
	public HttpResponse sendUserAlarm(String location) throws URISyntaxException, UnsupportedEncodingException, ClientProtocolException, IOException;
	
	public HttpResponse sendEmptyMessage() throws URISyntaxException, IOException;
}
