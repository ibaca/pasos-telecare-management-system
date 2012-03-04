package org.inftel.tms.mobile.pasos;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class PasosHTTPTransmitter extends AbstractPasosTrasmitter implements PasosTransmitter{	
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}	

	
	/**
	 * Manda un mensaje vacío, para pedir los remote parameters.Por si lo usamos.
	 * 
	 * @return HttpResponse
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public HttpResponse sendEmptyMessage() throws URISyntaxException,
		IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost();
		post.setHeader("senderNumber", this.senderNumber);
		URI uri = new URI(this.url);
		post.setURI(uri);
		return client.execute(post);
	}

	
	/**
	 * Envia una alerta de usuario (AU)
	 * 
	 * @return HttpResponse
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public HttpResponse sendUserAlarm(String location) throws URISyntaxException, ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost();
		post.setHeader("sender-mobile-number", this.senderNumber);
		URI uri = new URI(this.url);
		post.setURI(uri);
		setDateandTime();
		post.setEntity(new StringEntity("*$AU11&" + date + time + location
			+ "#"));
		return client.execute(post);
	}

	/**
	 * Envia una alerta de dispositivo (AD), por temperatura.
	 * 
	 * @return HttpResponse
	 * @throws URISyntaxException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public HttpResponse enviarDeviceAlarm(String location, String temp)
		throws URISyntaxException, UnsupportedEncodingException,
		IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost();
		post.setHeader("sender-mobile-number", senderNumber);
		URI uri = new URI(url);
		post.setURI(uri);
		setDateandTime();
		post.setEntity(new StringEntity("*$AD31&" + date + time + location
			+ "&DT" + temp + "#"));
		return client.execute(post);
	}

	/**
	 * Envia una alarma técnica (AT), por el nivel bajo de batería, indicando además si el terminal
	 * está conectado al cargador o no.
	 * 
	 * @return HttpRespose
	 * @throws URISyntaxException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public HttpResponse enviarTechnicalAlarm(String location, String batteryLevel, boolean charging)
		throws URISyntaxException,
		UnsupportedEncodingException,
		IOException {
		String charger = "&PC000";
		if (charging)
			charger = "&PC999";

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost();
		post.setHeader("sender-mobile-number", senderNumber);
		URI uri = new URI(url);
		post.setURI(uri);
		setDateandTime();
		post.setEntity(new StringEntity("*$AT2&"
			+ date
			+ time
			+ location
			+ "&PB" + batteryLevel
			+ charger
			+ "#"));
		return client.execute(post);
	}
}
