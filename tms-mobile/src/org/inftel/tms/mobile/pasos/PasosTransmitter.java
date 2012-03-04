package org.inftel.tms.mobile.pasos;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;

public interface PasosTransmitter {
	
	public HttpResponse enviarTechnicalAlarm(String location, String batteryLevel, boolean charging) throws URISyntaxException, UnsupportedEncodingException, IOException;
	
	public HttpResponse enviarDeviceAlarm(String location, String temp) throws URISyntaxException, UnsupportedEncodingException, IOException;
	
	public HttpResponse sendUserAlarm(String location) throws URISyntaxException, UnsupportedEncodingException, ClientProtocolException, IOException;
	
	public HttpResponse sendEmptyMessage() throws URISyntaxException, IOException;
}
