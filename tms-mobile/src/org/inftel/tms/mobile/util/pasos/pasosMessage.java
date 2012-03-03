package org.inftel.tms.mobile.util.pasos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.StringTokenizer;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.R.bool;

public class pasosMessage {
	private String ip;
	private int port;
	private String url;
	private String senderNumber;
	private String callCenterNumber;
	private String protocol;
	private String sms;
	private String date = "";
	private String time = "";

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

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSenderNumber() {
		return senderNumber;
	}

	public void setSenderNumber(String senderNumber) {
		this.senderNumber = senderNumber;
	}

	public String getCallCenterNumber() {
		return callCenterNumber;
	}

	public void setCallCenterNumber(String callCenterNumber) {
		this.callCenterNumber = callCenterNumber;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	/**
	 * Establece "Date" y "Time" en el formato que indica el protocolo PASOS.
	 * Les asigna la fecha y hora actuales.
	 */
	public void setDateandTime() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		String yearString;
		String dayString;
		String monthString;
		String hourString;
		String minsString;
		String secsString;

		dayString = StringUtils.leftPad(String.valueOf(day), 2, '0');
		monthString = StringUtils.leftPad(String.valueOf(month), 2, '0');
		hourString = StringUtils.leftPad(String.valueOf(hour), 2, '0');
		minsString = StringUtils.leftPad(String.valueOf(minutes), 2, '0');
		secsString = StringUtils.leftPad(String.valueOf(seconds), 2, '0');
		yearString = String.valueOf(year);

		this.date = "&LD" + yearString + monthString + dayString;
		this.time = "&LH" + hourString + minsString + secsString;
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
	 * Convierte un InputStream a String
	 * 
	 * @param in
	 * @return
	 */
	public String readStreamAsString(InputStream in) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
			byte[] buffer = new byte[1024];
			int count;
			do {
				count = in.read(buffer);
				if (count > 0) {
					out.write(buffer, 0, count);
				}
			} while (count >= 0);
			return out.toString("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"La maquina JVM no soporta codificación UTF-8", e);
		} catch (IOException e) {
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException ignored) {
			}
		}
	}

//	Parsea los parametros iniciales en caso de que los usemos.
	private void parseTrama(String trama) {
		StringTokenizer tokens = new StringTokenizer(trama.trim(), "&#*$");
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			if (token.contains("RV"))
				this.callCenterNumber = token;
			else if (token.contains("RT"))
				this.protocol = token;
			else if (token.contains("RI"))
				this.ip = token;
			else if (token.contains("RS"))
				sms = token;
			else if (token.contains("RP")) {
				;
			} else
				throw new RuntimeException("Trama incorrecta: " + token);
		}
	}

	/**
	 * Envia una alerta de usuario (AU)
	 * 
	 * @return HttpResponse
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public HttpResponse sendUserAlarm(String location)
			throws URISyntaxException, IOException {
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
	 * Envia una alarma técnica (AT), por el nivel bajo de batería, indicando
	 * además si el terminal está conectado al cargador o no.
	 * 
	 * @return HttpRespose
	 * @throws URISyntaxException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public HttpResponse enviarTechnicalAlarm(String location,String batteryLevel,boolean charging) 
			throws URISyntaxException, 
			UnsupportedEncodingException, 
			IOException {
        String charger="&PC000";
        if (charging) charger="&PC999";
        
    	HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost();    
        post.setHeader("sender-mobile-number", senderNumber);
        URI uri = new URI(url);
        post.setURI(uri);
        setDateandTime();
        post.setEntity(new StringEntity("*$AT2&"
                +date
                +time
                +location                
                +"&PB"+batteryLevel
                + charger
        		+"#"));
        return client.execute(post);  
    }
}
